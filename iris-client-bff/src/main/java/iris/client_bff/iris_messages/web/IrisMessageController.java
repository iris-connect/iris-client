package iris.client_bff.iris_messages.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.iris_messages.IrisMessageService;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/iris-messages")
public class IrisMessageController {

    private static final String FIELD_FOLDER = "folder";
    private static final String FIELD_SEARCH = "search";
    private static final String MESSAGE_ID = "messageId";

    private IrisMessageService irisMessageService;
    private final ValidationHelper validationHelper;

    @GetMapping()
    public Page<IrisMessage> getMessages(
            @RequestParam() String folder,
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        validateField(folder, FIELD_FOLDER);
        //@todo: implement search & DTO
        validateField(search, FIELD_SEARCH);
        return this.irisMessageService.findAllByFolderId(folder, pageable);
    }

//    @GetMapping("/{messageId}")
//    public ResponseEntity<IrisMessage> getMessage(
//            @PathVariable UUID messageId
//    ) {
//        validateUUID(messageId, MESSAGE_ID);
//    }

    @GetMapping("/folders")
    public ResponseEntity<List<IrisMessageFolderMapper.IrisMessageFolderDto>> getMessageFolders() {
        List<IrisMessageFolder> irisMessageFolders = irisMessageService.getFolders();
        return ResponseEntity.ok(IrisMessageFolderMapper.map(irisMessageFolders));
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
