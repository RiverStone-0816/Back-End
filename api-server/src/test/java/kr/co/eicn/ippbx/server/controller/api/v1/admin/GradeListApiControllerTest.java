package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.form.GradeListFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class GradeListApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/acd/grade/gradelist";

    //@Test
    public void pagination() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL+ "/{grade}/list", "VIP")
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "2"))
                .andDo(print())
                .andExpect(status().isOk())

                .andReturn();
    }

    //@Test
    @Order(1)
    public void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())

                .andReturn();
    }

    //@Test
    public void post() throws Exception {
        final GradeListFormRequest form = new GradeListFormRequest();

        form.setGradeNumber("11111111222");
        form.setType("A");
        form.setQueueNumber("00000000001");

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL+ "/{grade}","VIP")
                .contentType(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isCreated())

                .andReturn();
    }

    //@Test
    protected void put() throws Exception {
        final GradeListFormRequest form = new GradeListFormRequest();

        form.setGradeNumber("07075490108");
        form.setType("B");
        form.setQueueNumber("01000001111");

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 25)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())

                .andReturn();
    }

    //@Test
    public void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 26)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))

                .andReturn();
    }

}
