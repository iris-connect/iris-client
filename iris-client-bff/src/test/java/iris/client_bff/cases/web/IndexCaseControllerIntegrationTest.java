package iris.client_bff.cases.web;

import static iris.client_bff.cases.web.IndexCaseMapper.map;
import static iris.client_bff.cases.web.IndexCaseMapper.mapDetailed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.RestResponsePage;
import iris.client_bff.cases.CaseDataRequest;
import iris.client_bff.cases.CaseDataRequest.Status;
import iris.client_bff.cases.IndexCaseService;
import iris.client_bff.cases.web.request_dto.IndexCaseDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseDetailsDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseInsertDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseStatusDTO;
import iris.client_bff.cases.web.request_dto.IndexCaseUpdateDTO;

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
class IndexCaseControllerIntegrationTest {

	private final String baseUrl = "/data-requests-client/cases";

	TypeReference<RestResponsePage<IndexCaseDTO>> PAGE_TYPE = new TypeReference<>() {
	};

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper om;

	@MockBean
	IndexCaseService service;

	// mock responses
	private final CaseDataRequest MOCK_CASE = getCase();
	private final UUID MOCK_CASE_ID = UUID.fromString(MOCK_CASE.getId().toString());
	private final IndexCaseDTO MOCK_CASE_DTO = map(MOCK_CASE);
	private final IndexCaseDetailsDTO MOCK_CASE_DETAILED_DTO = mapDetailed(MOCK_CASE);
	private final Page<CaseDataRequest> casesPage = new RestResponsePage<>(List.of(MOCK_CASE));

	@BeforeEach
	void setUp() {
		when(service.findAll(any(Pageable.class))).thenReturn(casesPage);

		when(service.findByStatus(any(Status.class), any(Pageable.class))).thenReturn(casesPage);

		when(service.searchByRefIdOrName(any(String.class), any(Pageable.class))).thenReturn(casesPage);

		when(service.findByStatusAndSearchByRefIdOrName(any(Status.class), any(String.class), any(Pageable.class))).thenReturn(casesPage);

		when(service.findDetailed(MOCK_CASE_ID)).thenReturn(Optional.of(MOCK_CASE));

		when(service.update(any(CaseDataRequest.class), any(IndexCaseUpdateDTO.class))).then(invocation -> {
			CaseDataRequest dataRequest = invocation.getArgument(0);
			IndexCaseUpdateDTO updateDTO = invocation.getArgument(1);
			dataRequest.setName(updateDTO.getName());
			return dataRequest;
		});

		when(service.create(any())).thenReturn(MOCK_CASE);
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

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "?status=DATA_REQUESTED")).andExpect(status().isOk()).andReturn();

		verify(service).findByStatus(eq(Status.DATA_REQUESTED), any(Pageable.class));
		var allCases = om.readValue(res.getResponse().getContentAsString(), PAGE_TYPE);
		assertEquals(List.of(MOCK_CASE_DTO), allCases.getContent());
	}

	@Test
	@WithMockUser()
	@Order(4)
	void getAllFiltered() throws Exception {

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "?search=test")).andExpect(status().isOk()).andReturn();

		verify(service).searchByRefIdOrName(eq("test"), any(Pageable.class));
		var allCases = om.readValue(res.getResponse().getContentAsString(), PAGE_TYPE);
		assertEquals(List.of(MOCK_CASE_DTO), allCases.getContent());
	}

	@Test
	@WithMockUser()
	@Order(5)
	void getAllWithStatusAndFiltered() throws Exception {

		var res = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "?search=test&status=DATA_REQUESTED")).andExpect(status().isOk()).andReturn();

		verify(service).findByStatusAndSearchByRefIdOrName(eq(Status.DATA_REQUESTED), eq("test"), any(Pageable.class));
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
		var res = mockMvc.perform(MockMvcRequestBuilders.patch(url).content(payload).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		var updated = om.readValue(res.getResponse().getContentAsString(), IndexCaseDetailsDTO.class);

		var expected = mapDetailed(MOCK_CASE);
		expected.setName(updatedName);

		assertEquals(expected, updated);
	}

	@Test
	@WithMockUser()
	void update_invalidId() throws Exception {

		var url_404 = baseUrl + "/" + UUID.randomUUID().toString();

		mockMvc.perform(MockMvcRequestBuilders.patch(url_404).content("{}").contentType(MediaType.APPLICATION_JSON))
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
