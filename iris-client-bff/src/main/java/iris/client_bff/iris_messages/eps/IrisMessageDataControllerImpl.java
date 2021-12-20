package iris.client_bff.iris_messages.eps;

import iris.client_bff.config.JsonRpcDataValidator;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageTransfer;
import iris.client_bff.iris_messages.IrisMessageService;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class IrisMessageDataControllerImpl implements IrisMessageDataController {

    private final IrisMessageTransferDefuse messageTransferDefuse;
    private final IrisMessageService irisMessageService;

    private final JsonRpcDataValidator jsonRpcDataValidator;

    @Override
    public IrisMessageTransfer createIrisMessage(IrisMessageTransfer messageTransfer) throws ResponseStatusException {
        if (messageTransfer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.IRIS_MESSAGE_SUBMISSION);
        }
        jsonRpcDataValidator.validateData(messageTransfer);
        try {
            IrisMessage message = this.irisMessageService.receiveMessage(this.messageTransferDefuse.defuse(messageTransfer));
            return IrisMessageTransfer.fromEntity(message);
        } catch (IrisMessageException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getErrorMessage());
        }
    }
}