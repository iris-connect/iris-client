package iris.client_bff.cases;

import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.web.request_dto.IndexCaseInsertDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseUpdateDTO;
import iris.client_bff.core.log.LogHelper;
import iris.client_bff.core.IdentifierToken;
import iris.client_bff.core.service.TokenGenerator;
import iris.client_bff.core.utils.HibernateSearcher;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.proxy.IRISAnnouncementException;
import iris.client_bff.proxy.ProxyServiceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CaseDataRequestService {

	private static final String[] FIELDS = { "refId", "name" };

	private final CaseDataRequestRepository repository;
	private final HibernateSearcher searcher;
	private final ProxyServiceClient proxyClient;
	private final TokenGenerator tokenGenerator;

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
		IdentifierToken idToken;
		String announcedDomain;

		try {
			idToken = tokenGenerator.generateIdentifierToken();
			log.debug("Generated tokens for case request {}", idToken.toStringWithObfuscation());
		} catch(Exception e) {
			log.error("Failed to generate identifying tokens.");
			throw new IRISDataRequestException(e);
		}

		try {
			announcedDomain = proxyClient.announceExplicitToken(idToken.getConnectionAuthorizationToken());
			log.debug("Announced incoming connection on domain {} for readable token {}",
					LogHelper.obfuscateAtStart20(announcedDomain),
					LogHelper.obfuscateAtStart20(idToken.getReadableToken()));
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
				.identifierToken(idToken)
				.build();

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

}
