package iris.client_bff.users.web;

/**
 * @author Jens Kutzsche
 */
public interface UsersTestData {

	String VALID_USER = """
			{
				"firstName":"first name",
				"lastName":"last name",
				"userName":"%s",
				"password":"passwort1234",
				"role":"USER"
			}
			""";

	String VALID_UPDATE_ADMIN = """
			{
				"firstName":"admin Test",
				"lastName":"ABC",
				"userName":"admin_abc",
				"password":"passwort1234",
				"oldPassword":"admin",
				"role":"ADMIN"
			}
			""";

	String WITHOUT_VALUES = """
			{}
			""";

	String WITH_BLANK_VALUES = """
			{
			"firstName": "",
			"lastName": "  ",
			"userName":"",
			"password":"  "
			}
			""";

	String WITH_TOO_LONG_VALUES = """
			{
				"firstName": "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijx",
				"lastName": "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijx",
				"userName":"abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijx",
				"password":"abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijx",
				"role":"USER"
			}
			""";

	String WITH_FORBIDDEN_SYMBOLS = """
			{
			  "firstName": "Heiner JAVASCRIPT: Maus",
			  "lastName": "(Hampelmann",
				"userName":"_user",
				"password":"Beispiel DELETE hausen FROM",
				"role":"_aaa_"
			}
			""";

	String INVALID_PASSWORD = """
			{
				"firstName":"first name",
				"lastName":"last name",
				"userName":"user",
				"password":"password",
				"role":"USER"
			}
			""";
}
