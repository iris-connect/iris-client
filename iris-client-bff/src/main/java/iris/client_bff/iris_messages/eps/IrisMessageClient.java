package iris.client_bff.iris_messages.eps;

import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageHdContact;

import java.util.List;

public interface IrisMessageClient {
    IrisMessageHdContact getOwnIrisMessageHdContact() throws IrisMessageException;
    IrisMessageHdContact findIrisMessageHdContactById(String contactId) throws IrisMessageException;
    List<IrisMessageHdContact> getIrisMessageHdContacts() throws IrisMessageException;
}
