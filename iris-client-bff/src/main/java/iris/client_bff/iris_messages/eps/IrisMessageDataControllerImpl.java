package iris.client_bff.iris_messages.eps;

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

    @Override
    public void createIrisMessage(IrisMessageTransfer messageTransfer) throws ResponseStatusException {
        if (messageTransfer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.IRIS_MESSAGE_SUBMISSION);
        }
        try {
            this.irisMessageService.receiveMessage(this.messageTransferDefuse.defuse(messageTransfer));
        } catch (IrisMessageException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getErrorMessage());
        }
    }
}