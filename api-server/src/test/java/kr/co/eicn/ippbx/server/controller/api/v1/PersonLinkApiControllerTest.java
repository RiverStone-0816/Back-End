package kr.co.eicn.ippbx.server.controller.api.v1;

import kr.co.eicn.ippbx.model.form.PersonLinkFormRequest;
import kr.co.eicn.ippbx.model.form.PersonLinkListFormRequest;
import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class PersonLinkApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/person-link";

//    @Test
    protected void insert_person_link() throws Exception {
        final PersonLinkListFormRequest form = new PersonLinkListFormRequest();
        final PersonLinkFormRequest form1 = new PersonLinkFormRequest();
        final PersonLinkFormRequest form2 = new PersonLinkFormRequest();
        final PersonLinkFormRequest form3 = new PersonLinkFormRequest();
        form1.setName("네이버");
        form1.setReference("http://www.naver.com");
        form2.setName("다음");
        form2.setReference("http://www.daum.com");
        form3.setName("구글");
        form3.setReference("http://www.google.com");
        ArrayList<PersonLinkFormRequest> list = new ArrayList<>();
        list.add(form1);
        list.add(form2);
        list.add(form3);
        form.setPersonLinkForms(list);

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL+"/list")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                /*문서작성필요.*/
                .andReturn();

        log.info(String.valueOf(result));
    }

    @Test
    protected void select_person_link() throws Exception {

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                /*문서작성필요.*/
                .andReturn();

        log.info(String.valueOf(result));
    }
}
