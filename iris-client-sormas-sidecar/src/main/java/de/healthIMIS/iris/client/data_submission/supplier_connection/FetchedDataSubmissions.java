package de.healthIMIS.iris.client.data_submission.supplier_connection;

import de.healthIMIS.iris.client.data_submission.DataSubmissionDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
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
