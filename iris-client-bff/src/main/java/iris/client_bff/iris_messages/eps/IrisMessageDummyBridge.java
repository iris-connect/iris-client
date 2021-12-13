package iris.client_bff.iris_messages.eps;

import iris.client_bff.iris_messages.IrisMessageTransfer;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class IrisMessageDummyBridge {

    private final IrisMessageDataController irisMessageDataController;

    public IrisMessageDummyBridge(@Lazy IrisMessageDataController irisMessageDataController) {
        this.irisMessageDataController = irisMessageDataController;
    }

    public void createMessage(IrisMessageTransfer messageTransfer) throws ResponseStatusException {
        this.irisMessageDataController.createIrisMessage(messageTransfer);
    }
}
