package iris.client_bff.data_submission.supplier_connection;

import iris.client_bff.data_submission.DataSubmissionDto;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.util.Streamable;

@RequiredArgsConstructor(staticName = "of")
public class FetchedDataSubmissions implements Streamable<DataSubmissionDto> {

	private final List<DataSubmissionDto> dataSubmissions;

	@Override
	public Iterator<DataSubmissionDto> iterator() {
		return dataSubmissions.iterator();
	}
}