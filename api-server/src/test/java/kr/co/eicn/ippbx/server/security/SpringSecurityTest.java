package kr.co.eicn.ippbx.server.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.server.model.form.LoginRequest;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SpringSecurityTest {
	private ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;

//	@Test
	public void authentication_401() throws Exception {
		mockMvc.perform(get("/api/v1/admin/sounds/ring-back-tone"))
				.andDo(print())
				.andExpect(status().isUnauthorized());
	}

//	@Test
	public void authentication_200() throws Exception {
		final LoginRequest authenticationRequest = new LoginRequest();
		authenticationRequest.setCompany("primium");
		authenticationRequest.setId("master");
		authenticationRequest.setPassword("orange90");
		authenticationRequest.setExtension("");

		final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(authenticationRequest)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		final String token = getData(result, String.class);

		mockMvc.perform(get("/api/v1/admin/sounds/ring-back-tone")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
	}

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

	protected <T> T getData(MvcResult result, Class<T> convertType) {
		final JsonResult jsonResult = readValue(result);
		return mapper.convertValue(jsonResult.getData(), convertType);
	}
}
