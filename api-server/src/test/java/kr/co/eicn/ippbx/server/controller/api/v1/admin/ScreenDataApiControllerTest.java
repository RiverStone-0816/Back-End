package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.controller.api.v1.admin.monitor.screen.ScreenDataApiController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ScreenDataApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/monitor/screen-data";

//    @Test
    public void byPaused() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("expressionType", "INTEGRATION"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final ScreenDataApiController.IntegrationData integrationData = getData(result, ScreenDataApiController.IntegrationData.class);

        log.info("data {}", integrationData.toString());
    }

//    @Test
    public void byServices() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
		        .param("expressionType", "BY_SERVICE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final ScreenDataApiController.ByServiceData byServiceData = getData(result, ScreenDataApiController.ByServiceData.class);

        if (byServiceData.getServices() != null) {
            for (ScreenDataApiController.IntegrationData service : byServiceData.getServices()) {
                log.info("data {}", service.toString());
            }
        }
    }

//    @Test
    public void byQueues() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("expressionType", "BY_HUNT"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final ScreenDataApiController.ByHuntData byHuntData = getData(result, ScreenDataApiController.ByHuntData.class);

        if (byHuntData.getHunts() != null) {
            for (ScreenDataApiController.ByHuntData.HuntData hunt : byHuntData.getHunts()) {
                log.info("data {}", hunt.toString());
            }
        }
    }
}
