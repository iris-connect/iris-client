package iris.client_bff.iris_messages.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.*;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/iris-messages")
public class IrisMessageController {

    private static final String FOLDER_ID = "folderId";
    private static final String MESSAGE_ID = "messageId";

    private static final String FIELD_SEARCH = "search";

    private static final String FIELD_HD_RECIPIENT = "hdRecipient";
    private static final String FIELD_SUBJECT = "subject";
    private static final String FIELD_BODY = "body";

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IrisMessageDetailsDto> createMessage(@Valid @RequestBody IrisMessageInsert irisMessageInsert) {
        this.validateIrisMessageInsert(irisMessageInsert);
        IrisMessage message = irisMessageService.createMessage(irisMessageInsert);
        return ResponseEntity.ok(IrisMessageDetailsDto.fromEntity(message));
    }

    private void validateIrisMessageInsert(IrisMessageInsert irisMessageInsert) {
        if (irisMessageInsert == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
        }
        this.validateField(irisMessageInsert.getHdRecipient(), FIELD_HD_RECIPIENT);
        this.validateField(irisMessageInsert.getSubject(), FIELD_SUBJECT);
        this.validateField(irisMessageInsert.getBody(), FIELD_BODY);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<IrisMessageDetailsDto> getMessageDetails(
            @PathVariable UUID messageId
    ) {
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
            @RequestBody @Valid IrisMessageUpdate irisMessageUpdate
    ) {
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
        return ResponseEntity.ok(IrisMessageFolderDto.fromEntity(irisMessageFolders));
    }

    @GetMapping("/hd-contacts")
    public ResponseEntity<List<IrisMessageHdContact>> getMessageContacts() {
        List<IrisMessageHdContact> irisMessageContacts = irisMessageService.getHdContacts();
        return ResponseEntity.ok(irisMessageContacts);
    }

    @GetMapping("/count/unread")
    public ResponseEntity<Integer> getUnreadMessageCount(
            @RequestParam(required = false) UUID folder
    ) {
        if (folder == null) {
            return ResponseEntity.ok(irisMessageService.getCountUnread());
        }
        this.validateUUID(folder, FOLDER_ID, ErrorMessages.INVALID_IRIS_MESSAGE_FOLDER_ID);
        return ResponseEntity.ok(irisMessageService.getCountUnreadByFolderId(folder));
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
