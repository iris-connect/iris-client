package iris.client_bff.iris_messages.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.iris_messages.IrisMessageInsert;
import iris.client_bff.iris_messages.IrisMessageService;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/iris-messages")
public class IrisMessageController {

    private static final String FIELD_FOLDER = "folder";
    private static final String FIELD_SEARCH = "search";
    private static final String MESSAGE_ID = "messageId";

    private static final String FIELD_RECIPIENT_HD = "recipientHd";
    private static final String FIELD_SUBJECT = "subject";
    private static final String FIELD_BODY = "body";

    private IrisMessageService irisMessageService;
    private final ValidationHelper validationHelper;

    @GetMapping()
    public Page<IrisMessageListItemDto> getMessages(
            @RequestParam() String folder,
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        validateField(folder, FIELD_FOLDER);
        validateField(search, FIELD_SEARCH);
        return this.irisMessageService.search(folder, search, pageable).map(IrisMessageListItemDto::fromEntity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createMessage(@Valid @RequestBody IrisMessageInsert irisMessageInsert) {
        this.validateIrisMessageInsert(irisMessageInsert);

        irisMessageService.createMessage(irisMessageInsert);

        return ResponseEntity.ok(irisMessageInsert);
    }

    private void validateIrisMessageInsert(IrisMessageInsert irisMessageInsert) {
        if (irisMessageInsert == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
        }
        this.validateField(irisMessageInsert.getRecipientHd(), FIELD_RECIPIENT_HD);
        this.validateField(irisMessageInsert.getSubject(), FIELD_SUBJECT);
        this.validateField(irisMessageInsert.getBody(), FIELD_BODY);
    }

//    @GetMapping("/{messageId}")
//    public ResponseEntity<IrisMessage> getMessage(
//            @PathVariable UUID messageId
//    ) {
//        validateUUID(messageId, MESSAGE_ID);
//    }

    @GetMapping("/folders")
    public ResponseEntity<List<IrisMessageFolderDto>> getMessageFolders() {
        List<IrisMessageFolder> irisMessageFolders = irisMessageService.getFolders();
        return ResponseEntity.ok(IrisMessageFolderDto.fromEntity(irisMessageFolders));
    }

    @GetMapping("/count/unread")
    public ResponseEntity<Integer> getUnreadMessageCount(
            @RequestParam(required = false) String folder
    ) {
        validateField(folder, FIELD_FOLDER);
        return ResponseEntity.ok(irisMessageService.getCountUnread(folder));
    }

    private void validateField(String value, String field) {
        if (validationHelper.isPossibleAttack(value, field, false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
        }
    }

    private void validateUUID(UUID value, String field) {
        if (ValidationHelper.isUUIDInputValid(value.toString(), field)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
        }
    }

}
