package iris.client_bff.cases;

import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.dto.DwUrlParamDto;
import iris.client_bff.cases.web.request_dto.IndexCaseInsertDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseUpdateDTO;
import iris.client_bff.config.DwConfig;
import iris.client_bff.config.HealthDepartmentConfig;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.proxy.IRISAnnouncementException;
import iris.client_bff.proxy.ProxyServiceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
@AllArgsConstructor
public class CaseDataRequestService {

	private final CaseDataRequestRepository repository;
	private final ProxyServiceClient proxyClient;
	private final DwConfig dwConfig;
	private final HealthDepartmentConfig hdConfig;

	public Page<CaseDataRequest> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Page<CaseDataRequest> findByStatus(Status status, Pageable pageable) {
		return repository.findByStatus(status, pageable);
	}

	public Page<CaseDataRequest> findByStatusAndSearchByRefIdOrName(Status status, String search, Pageable pageable) {
		return repository.findByStatusAndSearchByRefIdOrName(status, search, pageable);
	}

	public Page<CaseDataRequest> searchByRefIdOrName(String search, Pageable pageable) {
		return repository.findByRefIdContainsOrNameContainsAllIgnoreCase(search, search, pageable);
	}

	public Optional<CaseDataRequest> findDetailed(UUID uuid) {
		var id = DataRequestIdentifier.of(uuid);
		return repository.findById(id);
	}

	public CaseDataRequest update(CaseDataRequest indexCase, IndexCaseUpdateDTO update) {
		if (StringUtils.isNotEmpty(update.getComment())) {
			indexCase.setComment(update.getComment());
		}
		if (StringUtils.isNotEmpty(update.getName())) {
			indexCase.setName(update.getName());
		}
		if (StringUtils.isNotEmpty(update.getExternalCaseId())) {
			indexCase.setRefId(update.getExternalCaseId());
		}
		if (update.getStatus() != null) {
			indexCase.setStatus(Status.valueOf(update.getStatus().name()));
		}
		return repository.save(indexCase);
	}

	public CaseDataRequest create(IndexCaseInsertDTO insert) throws IRISDataRequestException {
		String announcementToken;
		try {
			announcementToken = proxyClient.announce();
		} catch (IRISAnnouncementException e) {
			log.error("Announcement failed: ", e);
			throw new IRISDataRequestException(e);
		}

		var dataRequest = CaseDataRequest
				.builder()
				.comment(insert.getComment())
				.refId(insert.getExternalCaseId())
				.name(insert.getName())
				.requestStart(insert.getStart())
				.requestEnd(insert.getEnd())
				.announcementToken(announcementToken)
				.build();

		dataRequest.setDwSubmissionUri(generateDwUrl(dataRequest));

		return repository.save(dataRequest);
	}

	public int getCountSinceDate(Instant date) {
		return repository.getCountSinceDate(date);
	}

	public int getCountWithStatus(Status status) {
		return repository.getCountWithStatus(status);
	}

	private String generateDwUrl(CaseDataRequest dataRequest) throws IRISDataRequestException {
		String paramsAsJsonBase64;

		try {
			DwUrlParamDto dwUrlParamDto = new DwUrlParamDto(dataRequest.getId().toString(),
					dataRequest.getAnnouncementToken(),	hdConfig.getZipCode());
			String paramsAsJson = new ObjectMapper().writeValueAsString(dwUrlParamDto);
			paramsAsJsonBase64 = Base64.getEncoder().encodeToString(paramsAsJson.getBytes());
			log.info("Generated Base64 encoded params: {}", dwUrlParamDto);
		} catch(Exception e) {
			log.error("Generating DW URL failed", e);
			throw new IRISDataRequestException(e);
		}

		return dwConfig.getBaseurl() + dwConfig.getSuburlNewcase() + "?iris=" + paramsAsJsonBase64;
	}
}
