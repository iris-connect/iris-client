package iris.client_bff.utils;

import iris.client_bff.cases.eps.dto.ContactPerson;
import iris.client_bff.cases.eps.dto.Event;
import iris.client_bff.core.web.dto.Address;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DtoSupplier {
    private final ArrayList<Address> addressList = new ArrayList<>();
    private final ArrayList<ContactPerson> contactPersonList = new ArrayList<>();
    private final ArrayList<Event> eventList = new ArrayList<>();

    @PostConstruct
    public void init() {
        addressList.add(Address.builder().street("Rathausplatz").houseNumber("25").city("Neustadt").zipCode("12345").build());
        addressList.add(Address.builder().street("Bahnhofstrasse").houseNumber("9").city("'Altheim'").zipCode("34567").build());
        addressList.add(Address.builder().street("Hauptstrasse").houseNumber("163").city("Musterhausen").zipCode("23456").build());

        contactPersonList.add(ContactPerson.builder().firstName("Heinz").lastName("Huber").build());
        contactPersonList.add(ContactPerson.builder().firstName("Harald").lastName("Meier").build());
        contactPersonList.add(ContactPerson.builder().firstName("Vera").lastName("Kleinschmidt").build());
        contactPersonList.add(ContactPerson.builder().firstName("Katja").lastName("Zimmermann").build());

        eventList.add(Event.builder().name("Restaurantbesuch Hirsch").address(addressList.get(0)).phone("12345678").additionalInformation("Draußen").build());
        eventList.add(Event.builder().name("Museumsbesuch Staatsgalerie").address(addressList.get(1)).build());
    }

    public Address getAddress(int index) {
        return addressList.get(index);
    }

    public List<ContactPerson> getContactPersonList(int size) {
        int toIndex = size > contactPersonList.size() ? contactPersonList.size()-1 : size-1;
        return contactPersonList.subList(0, toIndex);
    }

    public List<Event> getEventList(int size) {
        int toIndex = size > eventList.size() ? eventList.size()-1 : size-1;
        return eventList.subList(0, toIndex);
    }
}
