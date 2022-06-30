package iris.client_bff.vaccination_info.eps;

/**
 * @author Jens Kutzsche
 */
public interface VaccinationInfoSubmissionTestData {

	String JSON_RPC_REQUEST = """
			{
			    "id":"1",
			    "jsonrpc":"2.0",
			    "method":"submitVaccinationInfoList",
			    "params":{
			    		"_client":{"name":"hd-1"}
			    		%s
			    }
			}
			""";

	String VALID_SUBMISSION = String.format(JSON_RPC_REQUEST, """
			,"dataAuthorizationToken": "%s",
			"facility": {
			  "name": "Pflegeheim Müller",
			  "address": {
			    "street": "Beispielstraße",
			    "houseNumber": "11",
			    "zipCode": "08150",
			    "city": "Beispielhausen"
			  },
			  "contactPerson": {
			    "firstName": "Max",
			    "lastName": "Mustermann",
			    "eMail": "max@ph-mueller.de",
			    "phone": "+4915108154711"
			  }
			},
			"employees": [
			  {
			    "firstName": "Erika",
			    "lastName": "Musterfrau",
			    "dateOfBirth": "1980-12-12",
			    "sex": "FEMALE",
			    "address": {
			      "street": "Teststrasse",
			      "houseNumber": "5",
			      "zipCode": "08153",
			      "city": "Beispielhausen"
			    },
			    "vaccination": "COVID_19",
			    "vaccinationStatus": "NOT_VACCINATED"
			  },
			  {
			    "firstName": "Heiner",
			    "lastName": "Hampelmann",
					"dateOfBirth": "1960-06-21",
					"sex": "OTHER",
			    "address": {
			      "street": "Spaßallee",
			      "houseNumber": "47",
			      "zipCode": "08151",
			      "city": "Clownstadt"
			    },
					"phone": "+49 3521 123456",
					"eMail": "ich@email.de",
			    "vaccination": "Covid-19",
			    "vaccinationStatus": "suspiciousProof"
			  }
			]
			""");

	String SUBMISSION_WITHOUT_PARAMS = String.format(JSON_RPC_REQUEST, "");

	String SUBMISSION_WITHOUT_VALUES = String.format(JSON_RPC_REQUEST, """
			,"dataAuthorizationToken": "",
			"facility": {},
			"employees": []
			""");

	String SUBMISSION_INVALID_MAIL_PHONE = String.format(JSON_RPC_REQUEST, """
			,"dataAuthorizationToken": "9519ac6f-59d7-4a8b-80f7-0a7d0c5ecb14",
			"facility": {
			  "name": "Pflegeheim Müller",
			  "address": {
			    "street": "Beispielstraße",
			    "houseNumber": "11",
			    "zipCode": "08150",
			    "city": "Beispielhausen"
			  },
			  "contactPerson": {
			    "firstName": "Max",
			    "lastName": "Mustermann",
			    "eMail": "max",
			    "phone": "abc"
			  }
			},
			"employees": []
			""");

	String SUBMISSION_INVALID_EMPLOYEES = String.format(JSON_RPC_REQUEST, """
			,"dataAuthorizationToken": "9519ac6f-59d7-4a8b-80f7-0a7d0c5ecb14",
			"facility": {
			  "name": "Pflegeheim Müller",
			  "address": {
			    "street": "Beispielstraße",
			    "houseNumber": "11",
			    "zipCode": "08150",
			    "city": "Beispielhausen"
			  },
			  "contactPerson": {
			    "firstName": "Max",
			    "lastName": "Mustermann",
			    "eMail": "max@max.de",
			    "phone": "+4915108154711"
			  }
			},
			"employees": [{}]
			""");

	String SUBMISSION_WITH_FORBIDDEN_SYMBOLS = String.format(JSON_RPC_REQUEST, """
			,"dataAuthorizationToken": "9519ac6f-59d7-4a8b-80f7-0a7d0c5ecb14",
			"facility": {
			  "name": "=Pflegeheim Müller",
			  "address": {
			    "street": "<Beispielstraße",
			    "houseNumber": ">11",
			    "zipCode": "!08150",
			    "city": "Beispiel DELETE hausen FROM"
			  },
			  "contactPerson": {
			    "firstName": "/Max",
			    "lastName": "%Mustermann",
			    "eMail": "max@ DROP ph-mueller VIEW .de",
			    "phone": "…+4915108154711"
			  }
			},
			"employees": [
			  {
			    "firstName": "_Heiner",
			    "lastName": "(Hampelmann",
					"dateOfBirth": "1960-06-21",
					"sex": "OTHER",
			    "address": {
			      "street": "!Spaßallee",
			      "houseNumber": "\\"47",
			      "zipCode": "^08151",
			      "city": "ÆClownstadt"
			    },
					"phone": "+49 3521 JAVASCRIPT: 123456",
					"eMail": "+ich@email.de",
			    "vaccination": "Covid-19",
			    "vaccinationStatus": "suspiciousProof"
			  }
			]
			""");
}
