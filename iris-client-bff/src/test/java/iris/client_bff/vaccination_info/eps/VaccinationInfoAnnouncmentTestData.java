package iris.client_bff.vaccination_info.eps;

/**
 * @author Jens Kutzsche
 */
public interface VaccinationInfoAnnouncmentTestData {

	static final String JSON_RPC_REQUEST = """
			{
			    "id":"1",
			    "jsonrpc":"2.0",
			    "method":"announceVaccinationInfoList",
			    "params":{
			    		"_client":{"name":"hd-1"},
			    		%s
			    }
			}
			""";

	static final String VALID_ANNOUNCEMENT = String.format(JSON_RPC_REQUEST, """
			"announcementData":{
				"externalId":"company-0815",
				"submitterPublicKey":"%s"
			}
			""");

	static final String ANNOUNCEMENT_WITHOUT_VALUES = String.format(JSON_RPC_REQUEST, """
			"announcementData":{
			}
			""");

	static final String ANNOUNCEMENT_WITH_EMPTY_VALUES = String.format(JSON_RPC_REQUEST, """
			"announcementData":{
				"externalId":"",
				"submitterPublicKey":"   "
			}
			""");

	static final String ANNOUNCEMENT_WITH_FORBIDDEN_VALUES = String.format(JSON_RPC_REQUEST, """
			"announcementData":{
				"externalId":"@auieiuae",
				"submitterPublicKey":"uaietrn<SCRIPT >auieuiae"
			}
			""");
}
