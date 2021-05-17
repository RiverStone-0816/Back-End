package kr.co.eicn.ippbx.server.controller.api.v1.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.server.model.ResearchAnswerItemComposite;
import kr.co.eicn.ippbx.server.model.ResearchQuestionItemComposite;
import kr.co.eicn.ippbx.server.model.entity.eicn.ResearchListEntity;
import kr.co.eicn.ippbx.server.model.form.ResearchListFormRequest;
import kr.co.eicn.ippbx.server.model.form.ResearchTreeFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ResearchItemRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ResearchListRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ResearchListApiControllerTest extends BaseControllerTest {
    private final String TEST_URL = "/api/v1/admin/outbound/research";
    private final FieldDescriptor[] researchListResponse = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
            fieldWithPath("regdate").type(JsonFieldType.STRING).description("등록일"),
            fieldWithPath("researchName").type(JsonFieldType.STRING).description("설문명"),
            fieldWithPath("researchId").type(JsonFieldType.NUMBER).description("설문 아이디"),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
            fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
            fieldWithPath("haveTree").type(JsonFieldType.STRING).description("시나리오 여부"),
            fieldWithPath("ttsFieldName").type(JsonFieldType.STRING).description("TTS필드명"),
            fieldWithPath("companyTrees").type(JsonFieldType.ARRAY).description("조직정보 목록").optional()
    };
    @Autowired
    private ResearchListRepository researchListRepository;
    @Autowired
    private ResearchItemRepository researchItemRepository;
    private FieldDescriptor[] organizationSummaryResponse = new FieldDescriptor[]{
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드"),
            fieldWithPath("groupName").type(JsonFieldType.STRING).description("조직명"),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨")
    };

    private FieldDescriptor[] researchListEntity = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY"),
            fieldWithPath("regdate").type(JsonFieldType.STRING).description("등록일"),
            fieldWithPath("researchName").type(JsonFieldType.STRING).description("설문명"),
            fieldWithPath("researchId").type(JsonFieldType.NUMBER).description("설문 아이디"),
            fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional(),
            fieldWithPath("groupTreeName").type(JsonFieldType.STRING).description("윗레벨의 코드를 포함한 코드의 나열(002_003_008)").optional(),
            fieldWithPath("groupLevel").type(JsonFieldType.NUMBER).description("조직레벨").optional(),
            fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디"),
            fieldWithPath("haveTree").type(JsonFieldType.STRING).description("시나리오 여부"),
            fieldWithPath("ttsFieldName").type(JsonFieldType.STRING).description("TTS필드명"),
            fieldWithPath("scenario").type(JsonFieldType.OBJECT).description("시나리오 DATA").optional(),

    };

    private FieldDescriptor[] researchTreeEntity = new FieldDescriptor[]{
            fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY").optional(),
            fieldWithPath("code").type(JsonFieldType.STRING).description("{설문아이디}_{시나리오 레벨}_{문항 아이디}_{research_item.mapping_number}").optional(),
            fieldWithPath("path").type(JsonFieldType.STRING).description("윗레벨의 path를 포함한 path의 나열 ex>0-4_1_2_0-4_2_6_1").optional(),
            fieldWithPath("root").type(JsonFieldType.STRING).description("최상위 트리 path").optional(),
            fieldWithPath("parent").type(JsonFieldType.STRING).description("윗레벨의 path").optional(),
            fieldWithPath("level").type(JsonFieldType.NUMBER).description("해당 트리의 레벨"),
            fieldWithPath("type").type(JsonFieldType.NUMBER).description("").optional(),
            fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("항목 아이디").optional(),
            fieldWithPath("mappingNumber").type(JsonFieldType.NUMBER).description("tree index").optional(),
            fieldWithPath("researchId").type(JsonFieldType.NUMBER).description("설문 아이디").optional(),
            fieldWithPath("companyId").type(JsonFieldType.STRING).description("고객사 아이디").optional(),
            fieldWithPath("item").type(JsonFieldType.OBJECT).description("항목정보"),
            fieldWithPath("answerItems").type(JsonFieldType.ARRAY).description("답변목록").optional(),
            fieldWithPath("childTree").type(JsonFieldType.OBJECT).description("하위 트리 정보").optional()
    };

    private FieldDescriptor[] researchQuestionItem = new FieldDescriptor[]{
            fieldWithPath("level").type(JsonFieldType.NUMBER).description("해당 트리의 레벨"),
            fieldWithPath("path").type(JsonFieldType.STRING).description("윗레벨의 path를 포함한 path의 나열 ex>0-4_1_2_0-4_2_6_1").optional(),
            fieldWithPath("word").type(JsonFieldType.STRING).description("").optional(),
            fieldWithPath("parent").type(JsonFieldType.STRING).description("윗레벨의 path").optional(),

            fieldWithPath("answerItems").type(JsonFieldType.ARRAY).description("문항 답변 목록"),
            fieldWithPath("childNode").type(JsonFieldType.ARRAY).description("하위 질문 DATA").optional()
    };

    //	@Test
    public void pagination() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .param("researchName", ""))
                .andExpect(status().isOk())
                .andDo(document.document(
                        requestParameters(
                                parameterWithName("researchName").description("설문명")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.rows[]").type(JsonFieldType.ARRAY).description("설문 목록").optional())
                                .andWithPrefix("data.rows[]", researchListResponse)
                                .andWithPrefix("data.rows[].companyTrees[]", organizationSummaryResponse)
                                .andWithPrefix("data.",
                                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총갯수")
                                )
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

//    @Test
    public void get() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/{researchId}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("researchId").description("설문 아이디")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional()
                        )
                                .and(fieldWithPath("data.").type(JsonFieldType.OBJECT).description("랜덤 RID 상세정보").optional())
                                .andWithPrefix("data.", researchListResponse)
                                .andWithPrefix("data.companyTrees[]", organizationSummaryResponse)
                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();
    }

    //	@Test
    public void post() throws Exception {
        final ResearchListFormRequest form = new ResearchListFormRequest();
        form.setResearchName("답변별로같은설문 테스트");
//		form.setGroupCode("0009");

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document.document(
                        requestFields(
                                fieldWithPath("researchName").type(JsonFieldType.STRING).description("설문명"),
                                fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

    //	@Test
    public void put() throws Exception {
        final ResearchListFormRequest form = new ResearchListFormRequest();
        form.setResearchName("예약확인설문 수정");
        form.setGroupCode("0009");

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(TEST_URL + "/{researchId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(form)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("researchId").description("설문 아이디")
                        ),
                        requestFields(
                                fieldWithPath("researchName").type(JsonFieldType.STRING).description("설문명"),
                                fieldWithPath("groupCode").type(JsonFieldType.STRING).description("조직코드").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

    //	@Test
    public void delete() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(TEST_URL + "/{researchId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("researchId").description("설문 아이디")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//    @Test
    public void getScenario() throws Exception {
        final MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get(TEST_URL + "/scenario/{researchId}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document.document(
                        relaxedResponseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").description("실제 반환될 데이터").optional()
                        )
                                .andWithPrefix("data.", researchListEntity)
                                .andWithPrefix("data.scenario.", researchQuestionItem)

                                .and(fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional())
                ))
                .andReturn();

        final ResearchListEntity research = getData(result, ResearchListEntity.class);

        log.info("설문지 : {}", research.getResearchName());

        printQLog(research.getScenario());
    }

    public void printQLog(ResearchQuestionItemComposite question) {
        log.info("{}{}.질문 :{}, path: {}", prefix(question.getLevel()), question.getLevel(), kr.co.eicn.ippbx.server.util.StringUtils.subStringBytes(question.getWord(), 30), question.getPath());
        if (question.getAnswerItems() != null)
            printALog(question.getAnswerItems());
        if (!question.isLeaf())
            question.getChildNode().forEach(this::printQLog);
    }

    public void printALog(List<ResearchAnswerItemComposite> answers) {
        for (ResearchAnswerItemComposite answerItem : answers) {
            log.info("{}{}.답변 :{}, parent:{}, path: {}", prefix(answerItem.getLevel()), answerItem.getLevel(), answerItem.getWord(), answerItem.getParent(), answerItem.getPath());
            if (!answerItem.isLeaf())
                printQLog(answerItem.getChild());
        }
    }

    /**
     * 설문관리 시나리오 작성
     * - 전체답변같은설문으로분기 처리시 research_tree.mapping_number == 0 이다.
     * 질문문항의 정보만 저장된다.
     * ex) - 설문 아이디가 3, 설문 문항 아이디가 2, level = 1인 데이터를 등록한다.
     * -- code = 3_1_2_0
     * -- level = 1
     * -- mapping_number = 0
     * -- parent = 0
     * -- path = 0-3_1_2_0 (parent + code)
     * -- root = 0-3_1_2_0
     * - 설문 아이디가 3, 설문 문항 아이디가 55, level = 2인 데이터를 등록한다.
     * -- code = 3_2_55_0
     * -- level = 2
     * -- mapping_number = 0
     * -- parent = 0-3_1_2_0
     * -- path = 0-3_1_2_0-3_2_55_0 (parent + code)
     * -- root = 0-3_1_2_0
     * - 답변별로다른설문으로분기 처리시 research_tree.mapping_number == research_item.mapping_number 가 된다.
     * 답변문항의 정보만 저장된다.
     * <p>
     * code "17_3_9_0" {설문 아이디}_{시나리오_레벨}_{설문 문항 아이디}_{설문 문항 mapping_number}
     * parent  윗레벨의 path를 포함 path의 나열 ex> 0-17_1_2_0-17_2_8_0-17_3_9_0
     * 상위 path
     * path 윗레벨의 path를 포함한 path의 나열 ex> 0-17_1_2_0-17_2_8_0-17_3_9_0
     * 상위 path + code;
     * root 최상위 트리 path ex> 0-17_1_2_0
     * level 생성순서대로 1, 2, 3
     */
//    @Test
    public void scenarioRegisterRootHasChildrenMappedAnswerNumber() throws Exception {

		/*
		research_item 테이블 테스트 데이터:
		item id 1: answer 1,2
		item id 2: answer none
		item id 3: answer none
		item id 4: answer 1,2,3,4,5,6
		item id 5: answer 1,2,3
		item id 6: answer 1,2
		item id 7: answer 1,2,3
		 */

        final ResearchTreeFormRequest formItem1 = new ResearchTreeFormRequest(1, true);
        final ResearchTreeFormRequest formItem2 = new ResearchTreeFormRequest(2, false);
        final ResearchTreeFormRequest formItem3 = new ResearchTreeFormRequest(3, true);
        final ResearchTreeFormRequest formItem4 = new ResearchTreeFormRequest(4, true);
        final ResearchTreeFormRequest formItem5 = new ResearchTreeFormRequest(5, true);
        final ResearchTreeFormRequest formItem6 = new ResearchTreeFormRequest(6, true);
        final ResearchTreeFormRequest formItem7 = new ResearchTreeFormRequest(7, false);

        formItem1.getChildrenMappedByAnswerNumber().put((byte) 1, formItem6);
        formItem6.getChildrenMappedByAnswerNumber().put((byte) 1, formItem3);
        formItem6.getChildrenMappedByAnswerNumber().put((byte) 2, formItem4);
        formItem6.getChildrenMappedByAnswerNumber().put((byte) 3, formItem3); // ignoring. item 6 has only 1,2 answers
        formItem4.getChildrenMappedByAnswerNumber().put((byte) 5, formItem7);
        formItem7.setChildNotMappedByAnswerNumber(formItem2);
        formItem3.getChildrenMappedByAnswerNumber().put((byte) 1, formItem5);

        /*
           0 -> 1_1_1 -> 2_6_1 -> 3_3_0 (답변이 없는 아이템, hasableAnswersChildTree에 상관없이 0)
                                        -> 4_5_1
                                        -> 4_5_2
                                        -> 4_5_3
                      -> 2_6_2 -> 3_4_1
                               -> 3_4_2
                               -> 3_4_3
                               -> 3_4_4
                               -> 3_4_5 -> 4_7_0 -> 5_2_0 (답변이 없는 아이템, hasableAnswersChildTree에 상관없이 0)
                               -> 3_4_6
                      -X 2_6_3 -X [1] 5
                1_1_2
         */

        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(formItem1));

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/{researchId}/scenario", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(formItem1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("researchId").description("설문 아이디")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

//    @Test
    public void scenarioRegisterRootHasChildNotMappedAnswerNumber() throws Exception {

		/*
		research_item 테이블 테스트 데이터:
		item id 1: answer 1,2
		item id 2: answer none
		item id 3: answer none
		item id 4: answer 1,2,3,4,5,6
		item id 5: answer 1,2,3
		item id 6: answer 1,2
		item id 7: answer 1,2,3
		 */

        final ResearchTreeFormRequest formItem1 = new ResearchTreeFormRequest(1, false);
        final ResearchTreeFormRequest formItem2 = new ResearchTreeFormRequest(2, false);
        final ResearchTreeFormRequest formItem3 = new ResearchTreeFormRequest(3, true);
        final ResearchTreeFormRequest formItem4 = new ResearchTreeFormRequest(4, true);
        final ResearchTreeFormRequest formItem5 = new ResearchTreeFormRequest(5, true);
        final ResearchTreeFormRequest formItem6 = new ResearchTreeFormRequest(6, true);
        final ResearchTreeFormRequest formItem7 = new ResearchTreeFormRequest(7, false);

        formItem1.setChildNotMappedByAnswerNumber(formItem6);
        formItem6.getChildrenMappedByAnswerNumber().put((byte) 1, formItem3);
        formItem6.getChildrenMappedByAnswerNumber().put((byte) 2, formItem4);
        formItem6.getChildrenMappedByAnswerNumber().put((byte) 3, formItem3); // ignoring. item 6 has only 1,2 answers
        formItem4.getChildrenMappedByAnswerNumber().put((byte) 5, formItem7);
        formItem7.setChildNotMappedByAnswerNumber(formItem2);
        formItem3.getChildrenMappedByAnswerNumber().put((byte) 1, formItem5);

        /*
           0 -> 1_1_0 -> 2_6_1 -> 3_3_0 (답변이 없는 아이템, hasableAnswersChildTree에 상관없이 0)
                                        -> 4_5_1
                                        -> 4_5_2
                                        -> 4_5_3
                      -> 2_6_2 -> 3_4_1
                               -> 3_4_2
                               -> 3_4_3
                               -> 3_4_4
                               -> 3_4_5 -> 4_7_0 -> 5_2_0 (답변이 없는 아이템, hasableAnswersChildTree에 상관없이 0)
                               -> 3_4_6
                      -X 2_6_3 -X [1] 5
         */

        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(formItem1));

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(TEST_URL + "/{researchId}/scenario", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .with(new JwtRequestPostProcessor())
                .content(mapper.writeValueAsString(formItem1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document.document(
                        pathParameters(
                                parameterWithName("researchId").description("설문 아이디")
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답결과"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("실패 메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("NULL").optional(),
                                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("필드 에러 정보목록").optional()
                        )
                ))
                .andReturn();
    }

    private String prefix(Integer level) {
        StringBuilder prefix = new StringBuilder("-");
        for (int i = 0; i < level; i++) {
            prefix.append("--");
        }
        return prefix.toString();
    }
}
