package iris.client_bff.cases;

import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.dto.DwUrlParamDto;
import iris.client_bff.cases.web.request_dto.IndexCaseInsertDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseUpdateDTO;
import iris.client_bff.config.DwConfig;
import iris.client_bff.config.HealthDepartmentConfig;
import iris.client_bff.core.log.LogHelper;
import iris.client_bff.core.utils.HibernateSearcher;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
@AllArgsConstructor
public class CaseDataRequestService {

	private static final String[] FIELDS = { "refId", "name" };

	private final CaseDataRequestRepository repository;
	private final HibernateSearcher searcher;
	private final ProxyServiceClient proxyClient;
	private final DwConfig dwConfig;
	private final HealthDepartmentConfig hdConfig;

	public Page<CaseDataRequest> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Page<CaseDataRequest> findByStatus(Status status, Pageable pageable) {
		return repository.findByStatus(status, pageable);
	}

	public Page<CaseDataRequest> search(Status status, String searchString, Pageable pageable) {

		var result = searcher.search(
				searchString,
				pageable,
				FIELDS,
				it -> status != null ? it.must(f2 -> f2.match().field("status").matching(status)) : it,
				CaseDataRequest.class);

		return new PageImpl<>(result.hits(), pageable, result.total().hitCount());
	}

	public Page<CaseDataRequest> search(String search, Pageable pageable) {
		return search(null, search, pageable);
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

			var status = Status.valueOf(update.getStatus().name());
			if (indexCase.getStatus() != status) {

				indexCase.setStatus(status);

				try {
					proxyClient.abortAnnouncement(indexCase.getAnnouncementToken());
				} catch (IRISAnnouncementException | IRISDataRequestException e) {
					log.error("Abort announcement for token {} failed", indexCase.getAnnouncementToken(), e);
				}
			}
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

		var dataRequest = CaseDataRequest.builder()
				.comment(insert.getComment())
				.refId(insert.getExternalCaseId())
				.name(insert.getName())
				.requestStart(insert.getStart())
				.requestEnd(insert.getEnd())
				.announcementToken(announcementToken)
				.build();

		dataRequest.setDwSubmissionUri(generateDwUrl(dataRequest));
		CaseDataRequest savedDataRequest = repository.save(dataRequest);
		log.info(LogHelper.CASE_DATA_REQUEST);
		return savedDataRequest;
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
					dataRequest.getAnnouncementToken(), hdConfig.getZipCode());
			String paramsAsJson = new ObjectMapper().writeValueAsString(dwUrlParamDto);
			paramsAsJsonBase64 = Base64.getEncoder().encodeToString(paramsAsJson.getBytes());
			log.debug("Generated Base64 encoded params: {}", dwUrlParamDto.toStringWithObfuscation());
		} catch (Exception e) {
			log.error("Generating DW URL failed", e);
			throw new IRISDataRequestException(e);
		}

		return dwConfig.getBaseurl() + dwConfig.getSuburlNewcase() + "?iris=" + paramsAsJsonBase64;
	}
}
