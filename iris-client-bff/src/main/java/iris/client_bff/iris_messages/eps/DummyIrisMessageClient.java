package iris.client_bff.iris_messages.eps;

import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DummyIrisMessageClient implements IrisMessageClient {

    private static final List<IrisMessageHdContact> IRIS_MESSAGE_HD_CONTACTS = List.of(
            IrisMessageHdContact.builder()
                    .name("dummy_contact_name_1")
                    .id("dummy_contact_id_1")
                    .build(),
            IrisMessageHdContact.builder()
                    .name("dummy_contact_name_2")
                    .id("dummy_contact_id_2")
                    .build(),
            IrisMessageHdContact.builder()
                    .name("dummy_contact_name_3")
                    .id("dummy_contact_id_3")
                    .build()
    );

    @Override
    public IrisMessageHdContact getOwnIrisMessageHdContact() throws IrisMessageException {
        return this.findIrisMessageHdContactById("dummy_contact_id_1");
    }

    @Override
    public IrisMessageHdContact findIrisMessageHdContactById(String contactId) throws IrisMessageException {
        return IRIS_MESSAGE_HD_CONTACTS
                .stream()
                .filter(c -> c.getId().equals(contactId))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<IrisMessageHdContact> getIrisMessageHdContacts() throws IrisMessageException {
        return IRIS_MESSAGE_HD_CONTACTS;
    }
}
