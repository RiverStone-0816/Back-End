package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.model.dto.eicn.EmailMngDetailResponse;
import kr.co.eicn.ippbx.model.enums.Bool;
import kr.co.eicn.ippbx.model.enums.MailProtocolType;
import kr.co.eicn.ippbx.model.enums.SendAuthConnType;
import kr.co.eicn.ippbx.model.form.EmailMngFormRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class EmailMngApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/email/mng";
    private static Integer seq;

    private FieldDescriptor[] emailMngDetailResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.STRING).description("시퀀스").optional(),
            fieldWithPath("serviceName").type(JsonFieldType.STRING).description("서비스명").optional(),
            fieldWithPath("mailUserName").type(JsonFieldType.STRING).description("대표메일계정").optional(),
            fieldWithPath("mailUserPasswd").type(JsonFieldType.STRING).description("메일계정비밀번호"),
            fieldWithPath("mailErrorNoticeEmail").type(JsonFieldType.STRING).description("에러시알람메일계정"),
            fieldWithPath("mailViewEmail").type(JsonFieldType.STRING).description("보고자하는 메일계정"),
            fieldWithPath("mailProtocol").type(JsonFieldType.VARIES).description("메일프로토콜(SMTP, POP3, IMAP)").optional(),
            fieldWithPath("mailSslYn").type(JsonFieldType.VARIES).description("메일SSL여부(Y:사용, N:비사용)"),
            fieldWithPath("mailHost").type(JsonFieldType.STRING).description("메일호스트"),
            fieldWithPath("mailPort").type(JsonFieldType.NUMBER).description("메일포트"),
            fieldWithPath("mailAttachPath").type(JsonFieldType.STRING).description("첨부저장경로"),
            fieldWithPath("sendUserName").type(JsonFieldType.STRING).description("보내는메일계정"),
            fieldWithPath("sendUserPasswd").type(JsonFieldType.STRING).description("보내는계정 비밀번호"),
            fieldWithPath("sendEmail").type(JsonFieldType.STRING).description("보내는메일").optional(),
            fieldWithPath("sendEmailName").type(JsonFieldType.STRING).description("보내는메일명").optional(),
            fieldWithPath("sendHost").type(JsonFieldType.STRING).description("보내는메일호스트"),
            fieldWithPath("sendPort").type(JsonFieldType.NUMBER).description("보내는메일포트"),
            fieldWithPath("sendAuthConnType").type(JsonFieldType.VARIES).description("암호화된 연결방식(TLS, SSL)")
    };

    private FieldDescriptor[] emailMngSummaryResponse = new FieldDescriptor[] {
            fieldWithPath("seq").type(JsonFieldType.STRING).description("시퀀스"),
            fieldWithPath("serviceName").type(JsonFieldType.STRING).description("서비스명"),
            fieldWithPath("mailUserName").type(JsonFieldType.STRING).description("대표메일계정"),
            fieldWithPath("mailProtocol").type(JsonFieldType.VARIES).description("메일프로토콜(SMTP, POP3, IMAP)"),
            fieldWithPath("sendEmail").type(JsonFieldType.STRING).description("보내는메일"),
            fieldWithPath("sendEmailName").type(JsonFieldType.STRING).description("보내는메일명")
    };

//      @Test
    @Order(4)
    protected void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("page", "1")
                .param("limit", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환될 데이터").optional())
                        .and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("이메일서비스 목록").optional())
                        .andWithPrefix("data.rows[]", emailMngSummaryResponse)
                        .andWithPrefix("data.",
                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수"))
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

          final Pagination<EmailMngDetailResponse> pagination = paginationData(result, EmailMngDetailResponse.class);
          final List<EmailMngDetailResponse> rows = pagination.getRows();
          for (EmailMngDetailResponse row : rows) {
              log.info(row.toString());
          }
    }

//      @Test
    @Order(3)
    protected void get() throws Exception {
        final MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{seq}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("시퀀스")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                        .and(fieldWithPath("data").type(JsonFieldType.OBJECT).description("이메일 설정관리 상세"))
                        .andWithPrefix("data.", emailMngDetailResponse)
                        .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
          final EmailMngDetailResponse data = getData(result, EmailMngDetailResponse.class);

          log.info("data {}", data);
    }

//      @Test
    @Order(1)
    protected void post() throws Exception {
        final EmailMngFormRequest form = new EmailMngFormRequest();
        form.setServiceName("서비스");         //서비스명
        form.setMailUserName("eicnamail11@gamil.com");    //대표메일계정
        form.setMailUserPasswd("user!234");   //메일계정비밀번호
        form.setMailErrorNoticeEmail("eicn@eicn.co.kr");  //에러시알람메일계정
        form.setMailViewEmail("eicnmail11@gamil.com");     //보고자하는 메일계정
        form.setMailProtocol(MailProtocolType.IMAP);         //메일프로토콜(SMTP, POP3, IMAP)
        form.setMailSslYn(Bool.Y);      //메일SSL여부(Y:사용, N:비사용)
        form.setMailHost("192.168.1.14");        //메일호스트
        form.setMailPort(345);     //메일포트
        form.setMailAttachPath("multichannel_mail");      //첨부저장경로
        form.setSendUserName("eicnmail11@gmail.com");     //보내는메일계정
        form.setSendUserPasswd("user!234");   //보내는계정 비밀번호
        form.setSendEmail("eicnmail11@gmail.com");        //보내는메일
        form.setSendEmailName("EICN");        //보내는메일명
        form.setSendHost("192.168.1.14");        //보내는메일호스트
        form.setSendPort(543);     //보내는메일포트
        form.setSendAuthConnType(SendAuthConnType.SSL);      //암호화된 연결방식(TLS, SSL)

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("serviceName").type(JsonFieldType.STRING).description("서비스명"),
                                fieldWithPath("mailUserName").type(JsonFieldType.STRING).description("대표메일계정"),
                                fieldWithPath("mailUserPasswd").type(JsonFieldType.STRING).description("메일계정비밀번호"),
                                fieldWithPath("mailErrorNoticeEmail").type(JsonFieldType.STRING).description("에러시알람메일계정"),
                                fieldWithPath("mailViewEmail").type(JsonFieldType.STRING).description("보고자하는 메일계정"),
                                fieldWithPath("mailProtocol").type(JsonFieldType.VARIES).description("메일프로토콜(SMTP, POP3, IMAP)"),
                                fieldWithPath("mailSslYn").type(JsonFieldType.VARIES).description("메일SSL여부(Y:사용, N:비사용)"),
                                fieldWithPath("mailHost").type(JsonFieldType.STRING).description("메일호스트"),
                                fieldWithPath("mailPort").type(JsonFieldType.NUMBER).description("메일포트"),
                                fieldWithPath("mailAttachPath").type(JsonFieldType.STRING).description("첨부저장경로"),
                                fieldWithPath("sendUserName").type(JsonFieldType.STRING).description("보내는메일계정"),
                                fieldWithPath("sendUserPasswd").type(JsonFieldType.STRING).description("보내는계정 비밀번호"),
                                fieldWithPath("sendEmail").type(JsonFieldType.STRING).description("보내는메일"),
                                fieldWithPath("sendEmailName").type(JsonFieldType.STRING).description("보내는메일명"),
                                fieldWithPath("sendHost").type(JsonFieldType.STRING).description("보내는메일호스트"),
                                fieldWithPath("sendPort").type(JsonFieldType.NUMBER).description("보내는메일포트"),
                                fieldWithPath("sendAuthConnType").type(JsonFieldType.VARIES).description("암호화된 연결방식(TLS, SSL)")
                        )
                        , responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();

        seq = getData(result, Integer.class);
        log.info("SEQUENCE KEY {}", seq);
    }

//       @Test
    @Order(2)
    protected void put() throws Exception {
        final EmailMngFormRequest form = new EmailMngFormRequest();
        form.setServiceName("서비스수정2");      //서비스명
        form.setMailUserName("update11@gamil.com");     //대표메일계정
        form.setMailUserPasswd("user!234");   //메일계정비밀번호
        form.setMailErrorNoticeEmail("eicn@eicn.co.kr");   //에러시알람메일계정
        form.setMailViewEmail("update11@gamil.com");        //보고자하는 메일계정
        form.setMailProtocol(MailProtocolType.POP3);         //메일프로토콜(SMTP, POP3, IMAP)
        form.setMailSslYn(Bool.Y);      //메일SSL여부(Y:사용, N:비사용)
        form.setMailHost("1.1.1.1");        //메일호스트
        form.setMailPort(543);     //메일포트
        form.setMailAttachPath("update_mail");            //첨부저장경로
        form.setSendUserName("update11@gmail.com");       //보내는메일계정
        form.setSendUserPasswd("user!234");   //보내는계정 비밀번호
        form.setSendEmail("update11@gmail.com");          //보내는메일
        form.setSendEmailName("EICN");        //보내는메일명
        form.setSendHost("1.1.1.1");        //보내는메일호스트
        form.setSendPort(345);     //보내는메일포트
        form.setSendAuthConnType(SendAuthConnType.TLS);         //암호화된 연결방식(TLS, SSL)

        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{seq}", 1)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(form))
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("시퀀스")
                        ),
                        requestFields(
                                fieldWithPath("serviceName").type(JsonFieldType.STRING).description("서비스명"),
                                fieldWithPath("mailUserName").type(JsonFieldType.STRING).description("대표메일계정"),
                                fieldWithPath("mailUserPasswd").type(JsonFieldType.STRING).description("메일계정비밀번호"),
                                fieldWithPath("mailErrorNoticeEmail").type(JsonFieldType.STRING).description("에러시알람메일계정"),
                                fieldWithPath("mailViewEmail").type(JsonFieldType.STRING).description("보고자하는 메일계정"),
                                fieldWithPath("mailProtocol").type(JsonFieldType.VARIES).description("메일프로토콜(SMTP, POP3, IMAP)"),
                                fieldWithPath("mailSslYn").type(JsonFieldType.VARIES).description("메일SSL여부(Y:사용, N:비사용)"),
                                fieldWithPath("mailHost").type(JsonFieldType.STRING).description("메일호스트"),
                                fieldWithPath("mailPort").type(JsonFieldType.NUMBER).description("메일포트"),
                                fieldWithPath("mailAttachPath").type(JsonFieldType.STRING).description("첨부저장경로"),
                                fieldWithPath("sendUserName").type(JsonFieldType.STRING).description("보내는메일계정"),
                                fieldWithPath("sendUserPasswd").type(JsonFieldType.STRING).description("보내는계정 비밀번호"),
                                fieldWithPath("sendEmail").type(JsonFieldType.STRING).description("보내는메일"),
                                fieldWithPath("sendEmailName").type(JsonFieldType.STRING).description("보내는메일명"),
                                fieldWithPath("sendHost").type(JsonFieldType.STRING).description("보내는메일호스트"),
                                fieldWithPath("sendPort").type(JsonFieldType.NUMBER).description("보내는메일포트"),
                                fieldWithPath("sendAuthConnType").type(JsonFieldType.VARIES).description("암호화된 연결방식(TLS, SSL)")
                        )
                        ,
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//      @Test
    @Order(5)
    protected void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{seq}", 3)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("success"))
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("seq").description("시퀀스")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("실제 반환될 데이터").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }
}
