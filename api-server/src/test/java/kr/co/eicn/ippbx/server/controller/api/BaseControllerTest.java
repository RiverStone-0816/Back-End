package kr.co.eicn.ippbx.server.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.form.LoginRequest;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Getter
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@EnableConfigurationProperties
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public abstract class BaseControllerTest {
	public static final ObjectMapper mapper = new ObjectMapper();

	@Value("${server.schema}")
	private String schema;
	@Value("${server.host}")
	private String host;
	@Value("${env.rest-doc.port}")
	private String port;

	protected MockMvc mockMvc;
	protected RestDocumentationResultHandler document;

	protected List<FieldDescriptor> baseFieldDescriptors = new ArrayList<>();

	@BeforeEach
	protected void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		document = document(
				"{class-name}/{method-name}",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint())
		);

		this.mockMvc = securityBuilder(webApplicationContext, restDocumentation);
	}

	/**
	 * default set
	 */
	protected MockMvc securityBuilder(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		return MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.apply(springSecurity())
				.apply(documentationConfiguration(restDocumentation).uris()
					.withScheme(schema)
					.withHost(host)
					.withPort(Integer.parseInt(port)))
				.alwaysDo(document)
				.build();
	}

	protected void pagination() throws Exception {};
	protected void get() throws Exception {};
	protected void post() throws Exception {};
	protected void put() throws Exception {};
	protected void delete() throws Exception {};

	protected JsonResult readValue(MvcResult result) {
		return readValue(result, true);
	}

	protected JsonResult readValue(MvcResult result, boolean isLogging) {
		try {
			final JsonResult jsonResult = mapper.readValue(result.getResponse().getContentAsString(), JsonResult.class);
			if (isLogging)
				log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonResult));
			return jsonResult;
		} catch (JsonProcessingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected <T> Pagination<T> paginationData(MvcResult result, Class<T> convertType) {
		final List<T> convertList = new ArrayList<>();
		final JsonResult jsonResult = readValue(result);
		final Pagination pagination = mapper.convertValue(jsonResult.getData(), Pagination.class);
		final ArrayList rows = mapper.convertValue(pagination.getRows(), ArrayList.class);

		for (Object row : rows) {
			final T t = mapper.convertValue(row, convertType);
			convertList.add(t);
		}

		pagination.setRows(convertList);

		return pagination;
	}

	protected <T> List<T> listData(MvcResult result, Class<T> convertType) {
		final List<T> convertList = new ArrayList<>();
		final JsonResult jsonResult = readValue(result);
		final List rows = mapper.convertValue(jsonResult.getData(), List.class);

		for (Object row : rows) {
			final T t = mapper.convertValue(row, convertType);
			convertList.add(t);
		}

		return convertList;
	}

	protected <T> T getData(MvcResult result, Class<T> convertType) {
		final JsonResult jsonResult = readValue(result);
		return mapper.convertValue(jsonResult.getData(), convertType);
	}

	@SneakyThrows
	protected String getAuthToken() {
		final LoginRequest authenticationRequest = new LoginRequest();
//		authenticationRequest.setCompany("primium");
//		authenticationRequest.setId("user0678");
//		authenticationRequest.setPassword("orange90");
//		authenticationRequest.setExtension("0677");

		authenticationRequest.setCompany("skdev");
		authenticationRequest.setId("user1");
		authenticationRequest.setPassword("user12!@!");
		authenticationRequest.setActionType(WebSecureActionType.LOGIN.getCode());
		authenticationRequest.setExtension("1000");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(authenticationRequest)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		return getData(result, String.class);
	}

	public class JwtRequestPostProcessor implements RequestPostProcessor {
		@Override
		public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
			request.addHeader("Authorization", "Bearer " + getAuthToken());
			return request;
		}
	}
}
