package iris.client_bff.cases.web;

import static iris.client_bff.cases.web.IndexCaseMapper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.RestResponsePage;
import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequest.DataRequestIdentifier;
import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.CaseDataRequestService;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseInsertDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseStatusDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseUpdateDTO;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.users.UserDetailsServiceImpl;
import iris.client_bff.users.entities.UserAccount;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@IrisWebIntegrationTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IndexCaseControllerTest {

	private final String baseUrl = "/data-requests-client/cases";

	TypeReference<RestResponsePage<IndexCaseDTO>> PAGE_TYPE = new TypeReference<>() {};

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper om;

	@MockBean
	CaseDataRequestService service;
	@MockBean
	UserDetailsServiceImpl userService;

	// mock responses
	private final CaseDataRequest MOCK_CASE = getCase();
	private final DataRequestIdentifier MOCK_CASE_ID = MOCK_CASE.getId();
	private final IndexCaseDTO MOCK_CASE_DTO = map(MOCK_CASE);
	private final Page<CaseDataRequest> casesPage = new RestResponsePage<>(List.of(MOCK_CASE));

	private IndexCaseDetailsDTO MOCK_CASE_DETAILED_DTO;

	@BeforeEach
	void setUp() throws IRISDataRequestException {

		when(service.findAll(any(Pageable.class))).thenReturn(casesPage);

		when(service.findByStatus(any(Status.class), any(Pageable.class))).thenReturn(casesPage);

		when(service.search(any(String.class), any(Pageable.class))).thenReturn(casesPage);

		when(service.search(any(Status.class), any(String.class), any(Pageable.class))).thenReturn(casesPage);

		when(service.findDetailed(MOCK_CASE_ID)).thenReturn(Optional.of(MOCK_CASE));

		when(service.update(any(CaseDataRequest.class), any(IndexCaseUpdateDTO.class))).then(invocation -> {
			CaseDataRequest dataRequest = invocation.getArgument(0);
			IndexCaseUpdateDTO updateDTO = invocation.getArgument(1);
			dataRequest.setName(updateDTO.getName());
			return dataRequest;
		});

		when(service.create(any())).thenReturn(MOCK_CASE);

		when(userService.findByUuid(any()))
				.thenReturn(Optional.of(new UserAccount().setFirstName("Max").setLastName("Muster")));

		MOCK_CASE_DETAILED_DTO = mapDetailed(MOCK_CASE, userService);
	}

	@Test
	@Order(1)
	void endpointShouldBeProtected() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl)).andExpect(status().isForbidden()).andReturn();
	}

	@Test
	@WithMockUser()
	@Order(2)
	void getAll() throws Exception {

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl)).andExpect(status().isOk()).andReturn();

		verify(service).findAll(any(Pageable.class));
		var allCases = om.readValue(res.getResponse().getContentAsString(), PAGE_TYPE);
		assertEquals(List.of(MOCK_CASE_DTO), allCases.getContent());
	}

	@Test
	@WithMockUser()
	@Order(3)
	void getAllWithStatus() throws Exception {

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "?status=DATA_REQUESTED")).andExpect(status().isOk())
				.andReturn();

		verify(service).findByStatus(eq(Status.DATA_REQUESTED), any(Pageable.class));
		var allCases = om.readValue(res.getResponse().getContentAsString(), PAGE_TYPE);
		assertEquals(List.of(MOCK_CASE_DTO), allCases.getContent());
	}

	@Test
	@WithMockUser()
	@Order(4)
	void getAllFiltered() throws Exception {

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "?search=test")).andExpect(status().isOk())
				.andReturn();

		verify(service).search(eq("test"), any(Pageable.class));
		var allCases = om.readValue(res.getResponse().getContentAsString(), PAGE_TYPE);
		assertEquals(List.of(MOCK_CASE_DTO), allCases.getContent());
	}

	@Test
	@WithMockUser()
	@Order(5)
	void getAllWithStatusAndFiltered() throws Exception {

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "?search=test&status=DATA_REQUESTED"))
				.andExpect(status().isOk()).andReturn();

		verify(service).search(eq(Status.DATA_REQUESTED), eq("test"), any(Pageable.class));
		var allCases = om.readValue(res.getResponse().getContentAsString(), PAGE_TYPE);
		assertEquals(List.of(MOCK_CASE_DTO), allCases.getContent());
	}

	@Test
	@WithMockUser()
	void create() throws Exception {

		var insert = om.writeValueAsString(IndexCaseInsertDTO.builder().start(Instant.now()).build());

		mockMvc.perform(MockMvcRequestBuilders.post(baseUrl).content(insert).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	@WithMockUser()
	void create_invalidStartDate() throws Exception {

		var insert = om.writeValueAsString(IndexCaseInsertDTO.builder().start(null).build());

		mockMvc.perform(MockMvcRequestBuilders.post(baseUrl).content(insert).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();
	}

	@Test
	@WithMockUser()
	void getDetails() throws Exception {

		var url = baseUrl + "/" + MOCK_CASE_ID.toString();

		var res = mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andReturn();

		var detailed = om.readValue(res.getResponse().getContentAsString(), IndexCaseDetailsDTO.class);

		assertEquals(MOCK_CASE_DETAILED_DTO, detailed);
	}

	@Test
	@WithMockUser()
	void getDetails_notFound() throws Exception {

		var url_404 = baseUrl + "/" + UUID.randomUUID().toString();

		mockMvc.perform(MockMvcRequestBuilders.get(url_404)).andExpect(status().isNotFound()).andReturn();
	}

	@Test
	@WithMockUser()
	void update() throws Exception {
		String updatedComment = "This is an updated comment";
		String updatedName = "CASE_UPDATED";
		String updatedExternalCaseId = "CASE_EXTERNALID";
		IndexCaseStatusDTO updatedStatus = IndexCaseStatusDTO.DATA_RECEIVED;

		var payload = om.writeValueAsString(
				IndexCaseUpdateDTO.builder()
						.name(updatedName)
						.status(updatedStatus)
						.comment(updatedComment)
						.externalCaseId(updatedExternalCaseId)
						.build());

		var url = baseUrl + "/" + MOCK_CASE_ID.toString();
		var res = mockMvc
				.perform(MockMvcRequestBuilders.patch(url).content(payload).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		var updated = om.readValue(res.getResponse().getContentAsString(), IndexCaseDetailsDTO.class);

		var expected = mapDetailed(MOCK_CASE, userService);
		expected.setName(updatedName);

		assertEquals(expected, updated);
	}

	@Test
	@WithMockUser()
	void update_invalidId() throws Exception {

		var url_404 = baseUrl + "/" + UUID.randomUUID().toString();

		String updatedComment = "This is an updated comment";
		String updatedName = "CASE_UPDATED";
		String updatedExternalCaseId = "CASE_EXTERNALID";
		IndexCaseStatusDTO updatedStatus = IndexCaseStatusDTO.DATA_RECEIVED;

		var payload = om.writeValueAsString(
				IndexCaseUpdateDTO.builder()
						.name(updatedName)
						.status(updatedStatus)
						.comment(updatedComment)
						.externalCaseId(updatedExternalCaseId)
						.build());

		mockMvc.perform(MockMvcRequestBuilders.patch(url_404).content(payload).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();
	}

	@Test
	@WithMockUser()
	void update_noBody() throws Exception {

		var url = baseUrl + "/" + MOCK_CASE_ID.toString();

		mockMvc.perform(MockMvcRequestBuilders.patch(url)).andExpect(status().isBadRequest()).andReturn();
	}

	private CaseDataRequest getCase() {
		var request = new CaseDataRequest();
		request.setName("TEST_CASE_DATA_REQUEST");
		return request;
	}
}
