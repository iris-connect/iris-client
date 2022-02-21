package iris.client_bff.hd_search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthDepartment {

    private String name;
    private String rkiCode;
    private String epsName;
    private String department;
    private Address address;
    private ContactData contactData;
    private ContactData covid19ContactData;
    private ContactData entryContactData;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class Address {
        private String street;
        private String zipCode;
        private String city;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class ContactData {
        private String phone;
        private String fax;
        @JsonProperty("eMail")
        private String eMail;
    }
}