package iris.client_bff.iris_messages.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.*;
import iris.client_bff.iris_messages.data.*;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/iris-messages/data")
public class IrisMessageDataController {

    private static final String MESSAGE_DATA_ID = "messageDataId";
    private static final String IMPORT_TARGET_ID = "importTargetId";

    private static final String FIELD_DATA_IMPORT_SELECTION = "selection";

    private IrisMessageService irisMessageService;
    private final ValidationHelper validationHelper;

    @PostMapping("/{messageDataId}/import/add")
    public ResponseEntity<?> importMessageDataAndAdd(@PathVariable UUID messageDataId) {
        this.validateUUID(messageDataId, MESSAGE_DATA_ID, ErrorMessages.INVALID_IRIS_MESSAGE_DATA_ID);
        this.irisMessageService.importMessageData(messageDataId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/{messageDataId}/import/update")
    public ResponseEntity<?> importMessageDataAndUpdate(
            @PathVariable UUID messageDataId,
            @RequestParam UUID importTargetId,
            @RequestBody String importSelection
    ) {
        this.validateUUID(messageDataId, MESSAGE_DATA_ID, ErrorMessages.INVALID_IRIS_MESSAGE_DATA_ID);
        this.validateUUID(importTargetId, IMPORT_TARGET_ID, ErrorMessages.INVALID_IRIS_MESSAGE_DATA_IMPORT_TARGET);
        this.validateMessageDataPayload(importSelection, FIELD_DATA_IMPORT_SELECTION);
        try {
            IrisMessageDataProcessor processor = this.irisMessageService.getMessageDataProcessor(messageDataId);
            processor.validateImportSelection(importSelection);
        } catch (IrisMessageDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        this.irisMessageService.importMessageData(messageDataId, importTargetId, importSelection);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{messageDataId}/import/select")
    public ResponseEntity<IrisMessageDataViewData> getMessageDataImportSelectionViewData(
            @PathVariable UUID messageDataId,
            @RequestParam(required = false) UUID importTargetId
    ) {
        this.validateUUID(messageDataId, MESSAGE_DATA_ID, ErrorMessages.INVALID_IRIS_MESSAGE_DATA_ID);
        if (importTargetId != null) {
            this.validateUUID(importTargetId, IMPORT_TARGET_ID, ErrorMessages.INVALID_IRIS_MESSAGE_DATA_IMPORT_TARGET);
        }
        IrisMessageDataViewData messageData = this.irisMessageService.getMessageDataImportSelectionViewData(messageDataId, importTargetId);
        return ResponseEntity.ok(messageData);
    }

    @GetMapping("/{messageDataId}/view")
    public ResponseEntity<IrisMessageDataViewData> getMessageDataViewData(@PathVariable UUID messageDataId) {
        this.validateUUID(messageDataId, MESSAGE_DATA_ID, ErrorMessages.INVALID_IRIS_MESSAGE_DATA_ID);
        IrisMessageDataViewData messageData = this.irisMessageService.getMessageDataViewData(messageDataId);
        return ResponseEntity.ok(messageData);
    }

    private void validateUUID(UUID value, String field, String errorMessage) {
        if (value == null || !ValidationHelper.isUUIDInputValid(value.toString(), field)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage + ": "+ field);
        }
    }

    private void validateMessageDataPayload(String value, String field) {
        if (validationHelper.isPossibleAttackForMessageDataPayload(value, field, false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_IRIS_MESSAGE_DATA + ": "+ field);
        }
    }

}
