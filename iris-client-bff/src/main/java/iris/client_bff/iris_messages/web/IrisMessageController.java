package iris.client_bff.iris_messages.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.*;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageFile;
import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.validation.FileTypeValidator;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/iris-messages")
public class IrisMessageController {

    private static final String FOLDER_ID = "folderId";
    private static final String MESSAGE_ID = "messageId";
    private static final String FILE_ID = "fileId";

    private static final String FIELD_SEARCH = "search";

    private static final String FIELD_HD_RECIPIENT = "hdRecipient";
    private static final String FIELD_SUBJECT = "subject";
    private static final String FIELD_BODY = "body";
    private static final String FIELD_FILE_ATTACHMENT = "fileAttachment";

    private IrisMessageService irisMessageService;
    private final ValidationHelper validationHelper;

    @GetMapping()
    public Page<IrisMessageListItemDto> getMessages(
            @RequestParam() UUID folder,
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        this.validateUUID(folder, FOLDER_ID, ErrorMessages.INVALID_IRIS_MESSAGE_FOLDER_ID);
        this.validateField(search, FIELD_SEARCH);
        return this.irisMessageService.search(folder, search, pageable).map(IrisMessageListItemDto::fromEntity);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<URI> createMessage(@Valid @ModelAttribute IrisMessageInsert irisMessageInsert, BindingResult bindingResult) {
        this.validateConstraints(bindingResult);
        this.validateIrisMessageInsert(irisMessageInsert);
        try {
            IrisMessage message = irisMessageService.sendMessage(irisMessageInsert);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(message.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (Throwable e) {
            String errorMessage = e instanceof IrisMessageException
                    ? ((IrisMessageException) e).getErrorMessage()
                    : ErrorMessages.IRIS_MESSAGE_SUBMISSION;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    private void validateIrisMessageInsert(IrisMessageInsert irisMessageInsert) {
        if (irisMessageInsert == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.IRIS_MESSAGE_SUBMISSION);
        }
        this.validateField(irisMessageInsert.getHdRecipient(), FIELD_HD_RECIPIENT);
        this.validateField(irisMessageInsert.getSubject(), FIELD_SUBJECT);
        this.validateField(irisMessageInsert.getBody(), FIELD_BODY);
        // disabled file attachments
        /*
        if (irisMessageInsert.getFileAttachments() != null) {
            for ( MultipartFile file : irisMessageInsert.getFileAttachments() ) {
                this.validateField(file.getOriginalFilename(), FIELD_FILE_ATTACHMENT);
            }
        }
         */
    }

    @GetMapping("/allowed-file-types")
    public ResponseEntity<String[]> getAllowedFileTypes() {
        return ResponseEntity.ok(FileTypeValidator.ALLOWED_TYPES);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<IrisMessageDetailsDto> getMessageDetails(@PathVariable UUID messageId) {
        this.validateUUID(messageId, MESSAGE_ID, ErrorMessages.INVALID_IRIS_MESSAGE_ID);
        Optional<IrisMessage> irisMessage = this.irisMessageService.findById(messageId);
        if (irisMessage.isPresent()) {
            IrisMessageDetailsDto messageDetailsDto = IrisMessageDetailsDto.fromEntity(irisMessage.get());
            return ResponseEntity.ok(messageDetailsDto);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<IrisMessageDetailsDto> updateMessage(
            @PathVariable UUID messageId,
            @RequestBody @Valid IrisMessageUpdate irisMessageUpdate,
            BindingResult bindingResult
    ) {
        this.validateConstraints(bindingResult);
        this.validateUUID(messageId, MESSAGE_ID, ErrorMessages.INVALID_IRIS_MESSAGE_ID);
        this.validateIrisMessageUpdate(irisMessageUpdate);
        Optional<IrisMessage> message = this.irisMessageService.findById(messageId);
        if (message.isPresent()) {
            IrisMessage updatedMessage = this.irisMessageService.updateMessage(message.get(), irisMessageUpdate);
            return ResponseEntity.ok(IrisMessageDetailsDto.fromEntity(updatedMessage));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private void validateIrisMessageUpdate(IrisMessageUpdate irisMessageUpdate) {
        if (irisMessageUpdate == null || irisMessageUpdate.getIsRead() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
        }
    }

    @GetMapping("/folders")
    public ResponseEntity<List<IrisMessageFolderDto>> getMessageFolders() {
        List<IrisMessageFolder> irisMessageFolders = irisMessageService.getFolders();
        // there should always be at least one inbox and one outbox folder. No folders at all = error
        if (irisMessageFolders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(IrisMessageFolderDto.fromEntity(irisMessageFolders));
    }

    // disabled file attachments
    /*
    @GetMapping("/files/{id}/download")
    public ResponseEntity<byte[]> downloadMessageFile(@PathVariable UUID id) {
        this.validateUUID(id, FILE_ID, ErrorMessages.INVALID_IRIS_MESSAGE_FILE_ID);
        Optional<IrisMessageFile> file = this.irisMessageService.findFileById(id);
        if (file.isPresent()) {
            try {
                IrisMessageFile messageFile = file.get();
                int contentLength = 0;
                MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
                if (messageFile.getContent() != null) {
                    contentLength = messageFile.getContent().length;
                    Tika tika = new Tika();
                    String contentType = tika.detect(messageFile.getContent(), messageFile.getName());
                    mediaType = MediaType.valueOf(contentType);
                }
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + messageFile.getName() + "\"")
                        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                        .contentLength(contentLength)
                        .contentType(mediaType)
                        .body(messageFile.getContent());
            } catch(Throwable e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ErrorMessages.INVALID_IRIS_MESSAGE_FILE);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
     */

    @GetMapping("/hd-contacts")
    public ResponseEntity<List<IrisMessageHdContact>> getMessageHdContacts(
            @Valid @Size(max = 100) @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "false") boolean includeOwn
    ) {
        validateField(search, FIELD_SEARCH);
        try {
            ArrayList<IrisMessageHdContact> irisMessageContacts = new ArrayList<>(irisMessageService.getHdContacts(search));
            if (includeOwn) {
                irisMessageContacts.add(irisMessageService.getOwnHdContact());
            }
            return ResponseEntity.ok(irisMessageContacts);
        } catch (Throwable e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.MISSING_IRIS_MESSAGE_HD_CONTACTS);
        }
    }

    @GetMapping("/count/unread")
    public ResponseEntity<Integer> getUnreadMessageCount(@RequestParam(required = false) UUID folder) {
        if (folder == null) {
            return ResponseEntity.ok(irisMessageService.getCountUnread());
        }
        this.validateUUID(folder, FOLDER_ID, ErrorMessages.INVALID_IRIS_MESSAGE_FOLDER_ID);
        return ResponseEntity.ok(irisMessageService.getCountUnreadByFolderId(folder));
    }

    private void validateConstraints(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ErrorMessages.INVALID_INPUT + ": " + bindingResult.getFieldErrors().stream()
                    .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage())).collect(Collectors.joining(", "));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private void validateField(String value, String field) {
        if (validationHelper.isPossibleAttack(value, field, false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
        }
    }

    private void validateUUID(UUID value, String field, String errorMessage) {
        if (value == null || !ValidationHelper.isUUIDInputValid(value.toString(), field)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

}
