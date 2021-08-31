package iris.client_bff.events.eps;

import iris.client_bff.events.EventDataRequest;
import iris.client_bff.events.exceptions.IRISDataRequestException;

public interface DataProviderClient {
	void requestGuestListData(EventDataRequest request) throws IRISDataRequestException;

	void abortGuestListDataRequest(EventDataRequest request) throws IRISDataRequestException;
}
