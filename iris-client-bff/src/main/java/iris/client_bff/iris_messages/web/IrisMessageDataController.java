package iris.client_bff.iris_messages.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.*;
import iris.client_bff.iris_messages.IrisMessageData.IrisMessageDataIdentifier;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/iris-messages/data")
public class IrisMessageDataController {

    private static final String FIELD_DATA_IMPORT_SELECTION = "selection";

    private IrisMessageService irisMessageService;
    private IrisMessageDataViewProvider irisMessageDataViewProvider;
    private final IrisMessageDataProcessors messageDataProcessors;

    private final ValidationHelper validationHelper;

    private final MessageSourceAccessor messages;

    @PostMapping("/{messageDataId}/import/add")
    public ResponseEntity<?> importMessageDataAndAdd(@PathVariable IrisMessageDataIdentifier messageDataId) {
        this.irisMessageService.importMessageData(messageDataId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/{messageDataId}/import/update")
    public ResponseEntity<?> importMessageDataAndUpdate(
            @PathVariable IrisMessageDataIdentifier messageDataId,
            @RequestParam UUID importTargetId,
            @RequestBody String importSelection
    ) {
        this.validateMessageDataPayload(importSelection, FIELD_DATA_IMPORT_SELECTION);
        try {
            IrisMessageDataProcessor processor = this.messageDataProcessors.getProcessor(messageDataId);
            processor.validateImportSelection(importSelection);
        } catch (IrisMessageDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        this.irisMessageService.importMessageData(messageDataId, importTargetId, importSelection);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{messageDataId}/import/select")
    public ResponseEntity<IrisMessageDataViewDataDto> getMessageDataImportSelectionViewData(
            @PathVariable IrisMessageDataIdentifier messageDataId,
            @RequestParam(required = false) UUID importTargetId
    ) {
        IrisMessageDataViewDataDto messageData = this.irisMessageDataViewProvider.getViewData(messageDataId, importTargetId);
        return ResponseEntity.ok(messageData);
    }

    @GetMapping("/{messageDataId}/view")
    public ResponseEntity<IrisMessageDataViewDataDto> getMessageDataViewData(@PathVariable IrisMessageDataIdentifier messageDataId) {
        IrisMessageDataViewDataDto messageData = this.irisMessageDataViewProvider.getViewData(messageDataId);
        return ResponseEntity.ok(messageData);
    }

    private void validateMessageDataPayload(String value, String field) {
        if (validationHelper.isPossibleAttackForMessageDataPayload(value, field, false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, messages.getMessage("iris_message.invalid_message_data") + ": "+ field);
        }
    }
}
