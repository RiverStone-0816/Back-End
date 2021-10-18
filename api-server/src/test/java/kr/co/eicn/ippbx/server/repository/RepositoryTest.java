package kr.co.eicn.ippbx.server.repository;

import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.controller.api.BaseControllerTest;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoStatus;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.CommonExecutePDSCustomInfo;
import kr.co.eicn.ippbx.model.IvrTreeComposite;
import kr.co.eicn.ippbx.model.PDSIvrComposite;
import kr.co.eicn.ippbx.model.ResearchAnswerItemComposite;
import kr.co.eicn.ippbx.model.ResearchQuestionItemComposite;
import kr.co.eicn.ippbx.model.entity.eicn.ResearchListEntity;
import kr.co.eicn.ippbx.model.entity.eicn.ScheduleGroupEntity;
import kr.co.eicn.ippbx.model.entity.eicn.ScheduleGroupListEntity;
import kr.co.eicn.ippbx.model.entity.statdb.StatInboundEntity;
import kr.co.eicn.ippbx.model.enums.ScheduleType;
import kr.co.eicn.ippbx.model.search.TalkServiceInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.StatInboundService;
import kr.co.eicn.ippbx.server.service.StatQueueWaitService;
import kr.co.eicn.ippbx.server.service.TalkStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.HolyInfo.HOLY_INFO;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.IvrTree.IVR_TREE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.Number_070.NUMBER_070;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ScheduleGroup.SCHEDULE_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ScheduleInfo.SCHEDULE_INFO;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TodoList.TODO_LIST;
import static kr.co.eicn.ippbx.meta.jooq.pds.tables.ExecutePdsCustomInfo.EXECUTE_PDS_CUSTOM_INFO;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ComponentScan(basePackages = { Constants.REPOSITORY_PACKAGE })
public class RepositoryTest extends BaseControllerTest {
	@Autowired
	@Qualifier(Constants.BEAN_DSL_EICN)
	private DSLContext eicnDsl;
	@Autowired
	@Qualifier(Constants.BEAN_DSL_CONFIGDB)
	private DSLContext configdbDsl;
	@Autowired
	@Qualifier(Constants.BEAN_DSL_CUSTOMDB)
	private DSLContext customdbDsl;
	@Autowired
	@Qualifier(Constants.BEAN_DSL_PDS)
	private DSLContext pdsDsl;
	@Autowired
	@Qualifier(Constants.BEAN_DSL_STATDB)
	private DSLContext statdbDsl;

	@Autowired
	private PhoneInfoRepository phoneInfoRepository;
	@Autowired
	private CompanyServerRepository companyServerRepository;
	@Autowired
	private RecordDownRepository recordDownRepository;
	@Autowired
	private RecordEncKeyRepository recordEncKeyRepository;
	@Autowired
	private PersonListRepository personListRepository;
	@Autowired
	private TalkMemberGroupRepository talkMemberGroupRepository;
	@Autowired
	private TalkMemberListRepository talkMemberListRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CompanyTreeRepository companyTreeRepository;
	@Autowired
	private CallbackDistRepository callbackDistRepository;
	@Autowired
	private TalkScheduleGroupRepository talkScheduleGroupRepository;
	@Autowired
	private TalkServiceInfoRepository talkServiceInfoRepository;
	@Autowired
	private TalkScheduleInfoRepository talkScheduleInfoRepository;
	@Autowired
	private TalkStatisticsService talkStatisticsService;
	@Autowired
	private OutScheduleSeedRepository outScheduleSeedRepository;
	@Autowired
	private IvrTreeRepository ivrTreeRepository;
	@Autowired
	private PDSIvrRepository pdsIvrRepository;
	@Autowired
	private PDSUploadRepository pdsUploadRepository;
	@Autowired
	private ResearchItemRepository researchItemRepository;
	@Autowired
	private ResearchTreeRepository researchTreeRepository;
	@Autowired
	private HolyInfoRepository holyInfoRepository;
	@Autowired
	private MohListRepository mohListRepository;
	@Autowired
	private NoticeRepository noticeRepository;
	@Autowired
	private NoticeFileEntityRepository noticeFileEntityRepository;
	@Autowired
	private RecordEncFileRepository recordEncFileRepository;
	@Autowired
	private HistoryPDSGroupRepository historyPDSGroupRepository;
	@Autowired
	private Number070Repository number070Repository;
	@Autowired
	private ScheduleGroupRepository scheduleGroupRepository;
	@Autowired
	private StatInboundService statInboundService;
	@Autowired
	private StatQueueWaitService statQueueWaitService;
	@Autowired
	private ArsAuthRepository arsAuthRepository;

//	@Test
	public void get_talk_serviceinfo_lists() {
		final TalkServiceInfoSearchRequest search = new TalkServiceInfoSearchRequest();
		search.setType(ScheduleType.WEEK);
	}

//	@Test
	public void getIvrTreeNodes() {
		List<IvrTreeComposite> nodes = ivrTreeRepository.getIvrTreeLists();

		ivrTreePrintTree(nodes);
	}

	@Test
	public void getPdsTreeNodes() {
		List<PDSIvrComposite> nodes = pdsIvrRepository.getIvrLists();

		printTree(nodes);
	}

	private void ivrTreePrintTree(List<IvrTreeComposite> nodes) {
		for (IvrTreeComposite node : nodes) {
			log.info(prefix(StringUtils.countMatches(node.getTreeName(), '_')) + "({}){}, code: {}, root: {}, tree_name: {}"
					, StringUtils.countMatches(node.getTreeName(), '_')
					, node.getName()
					, node.getCode()
					, node.getRoot()
					, node.getTreeName());
			if (!node.isLeaf())
				ivrTreePrintTree(node.getNodes());
		}
	}

	private void printTree(List<PDSIvrComposite> nodes) {
		for (PDSIvrComposite node : nodes) {
			log.info(prefix(StringUtils.countMatches(node.getTreeName(), '_')) + "({}){}, code: {}, root: {}, tree_name: {}"
					, StringUtils.countMatches(node.getTreeName(), '_')
					, node.getName()
					, node.getCode()
					, node.getRoot()
					, node.getTreeName());
			if (!node.isLeaf())
				printTree(node.getNodes());
		}
	}

//	@Test
	public void researchTree() {
//		final List<ResearchTreeEntity> researchScenarioLists = researchTreeRepository.getResearchScenarioLists(1);
//		for (ResearchTreeEntity tree : researchScenarioLists) {
//			printResearchTree(tree);
//		}
	}

	private String prefix(Integer level) {
		StringBuilder prefix = new StringBuilder("-");
		for (int i = 0; i < level ; i++) {
			prefix.append("--");
		}
		return prefix.toString();
	}

//	@Test
	public void dslTest(){
		final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HolyInfo> holyInfos = eicnDsl.select().from(HOLY_INFO).fetchInto(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HolyInfo.class);
		for (kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HolyInfo holyInfo : holyInfos) {
			log.info("holy -> {}", holyInfo.toString());
		}
	}

//	@Test
	public void get_research_scenario() {
		final ResearchListEntity researchScenarioLists = researchTreeRepository.getResearchScenarioLists(23);

		ResearchQuestionItemComposite root = researchScenarioLists.getScenario();

		printQLog(root);
	}

	public void printQLog(ResearchQuestionItemComposite question) {
		log.info("{}{}.질문 :{}, path: {}", prefix(question.getLevel()), question.getLevel(), kr.co.eicn.ippbx.util.StringUtils.subStringBytes(question.getWord(), 30), question.getPath());
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

//	@Test
	public void dynamic_companyId() {

		Table<Record> table = DSL.table(DSL.name("view_result_cst_by_result_ssss"));

		log.info("table {}", table);

		log.info("table null ? {}", table);
		//		List<ResultCustomInfoEntity> resultCustomInfoEntities = customdbDsl.select()
//				.from(table)
//				.fetchInto(ResultCustomInfoEntity.class);
//
//		for (ResultCustomInfoEntity resultCustomInfoEntity : resultCustomInfoEntities) {
//			log.info("data {}", resultCustomInfoEntity.toString());
//		}
	}

	/**
	 *	fieldWithPath("seq").type(JsonFieldType.NUMBER).description("SEQUENCE KEY");
	 */
//	@Test
	public void 테이블정보를_읽어와서_API명세필드를_만들어줌() {
		Stream<Field<?>> fieldStream = eicnDsl.selectFrom(IVR_TREE).fieldStream();

		fieldStream.forEach(e -> {
			System.out.println(String.format("fieldWithPath(\"%s\").type(JsonFieldType.%s).description(\"%s\")%s,", convert2CamelCase(e.getName()), type(e.getType()), e.getComment(), nullable(e.getDataType())));
		});
	}

	private String type(Class<?> type) {
		if (Objects.equals(type, String.class) || Objects.equals(type, Timestamp.class))
			return "STRING";
		else if (Objects.equals(type, Integer.class) || Objects.equals(type, Byte.class))
			return "NUMBER";

		return "타입없음(" + type.getName() + ")";
	}

	private String nullable(DataType<?> dataType) {
		return (dataType.nullable()) ? ".optional()" : "";
	}

	private String convert2CamelCase(String underScore) {
		if (underScore.indexOf('_') < 0
				&& Character.isLowerCase(underScore.charAt(0))) {
			return underScore;
		}

		StringBuilder result = new StringBuilder();
		boolean nextUpper = false;
		int len = underScore.length();

		for (int i = 0; i < len; i++) {
			char currentChar = underScore.charAt(i);
			if (currentChar == '_') {
				nextUpper = true;
			} else {
				if (nextUpper) {
					result.append(Character.toUpperCase(currentChar));
					nextUpper = false;
				} else {
					result.append(Character.toLowerCase(currentChar));
				}
			}
		}
		return result.toString();
	}

//	@Test
	public void todo_list_query() {
//		select todo_kind, count(todo_kind) as total, count(case when todo_status='DONE' then 1 end) as success
//		from todo_list where userid='user0677' and regdate>='2020-04-24 00:00:00' and regdate<='2020-04-24 23:59:59' group by todo_kind;
		eicnDsl.select(TODO_LIST.TODO_KIND)
				.select(DSL.count(TODO_LIST.TODO_KIND).as("total"))
				.select(DSL.count(DSL.when(TODO_LIST.TODO_STATUS.eq(TodoListTodoStatus.DONE), 1)).as("success"))
				.from(TODO_LIST)
				.where(TODO_LIST.USERID.eq("user0677"))
				.and(TODO_LIST.REGDATE.ge(Timestamp.valueOf("2020-04-24 00:00:00")))
				.and(TODO_LIST.REGDATE.le(Timestamp.valueOf("2020-04-24 23:59:59")))
				.groupBy(TODO_LIST.TODO_KIND)
		.fetch(); // 이 부분은 수정해야됩니다.
	}

//	@Test
//	public void 설문시나리오등록테스트() {
//		// researchId = 15
//		// 질문 선생님께서는 어느 곳에 살고 계십니까?
//		final ResearchTreeFormRequest root = new ResearchTreeFormRequest(41, (byte) 0, false);
//
//		// 질문2단계 선생님의 성별은
//		final ResearchTreeFormRequest depth1 = new ResearchTreeFormRequest(42, (byte) 0, false);
////		root.addNodes(depth1);
//
//		// 질문3단계 선생님의 연령은  어덯게 되십니까
//		final ResearchTreeFormRequest depth2 = new ResearchTreeFormRequest(43, (byte) 0, false);
////		depth1.addNodes(depth2);
//
//		// [1] 질문4 선생님께서는 오는 4월
//		final ResearchTreeFormRequest depth3 = new ResearchTreeFormRequest(44, (byte) 0, false);
////		depth2.addNodes(depth3);
//
////		researchTreeRepository.insert(root, 15);
//
//		// researchId = 21
//		// [1]질문 안녕하십니까.
//		final ResearchTreeFormRequest root1 = new ResearchTreeFormRequest(1, (byte) 0, true);
//
//		//  [1]답변 10대
//		final ResearchTreeFormRequest rootChild1 = new ResearchTreeFormRequest(1, (byte) 1, true);
////		root1.addNodes(rootChild1);
//
//		//      [2] 답변 111
//		final ResearchTreeFormRequest rootChild2 = new ResearchTreeFormRequest(3, (byte) 1, true);
//		//      [2] 답변 2222
//		final ResearchTreeFormRequest rootChild3 = new ResearchTreeFormRequest(3, (byte) 2, true);
//		//      [2] 답변 333
//		final ResearchTreeFormRequest rootChild4 = new ResearchTreeFormRequest(3, (byte) 3, true);
//
////		rootChild1.addNodes(rootChild2);
////		rootChild1.addNodes(rootChild3);
////		rootChild1.addNodes(rootChild4);
//
//		researchTreeRepository.insert(rootChild1, 21);
//
//		/**
//		 * 0-21_1_1_1
//		 * 0-21_1_1_1-21_2_3_1
//		 *
//		 * 0-21_1_1_1-21_2_3_1
//		 * 0-21_1_1_1-21_2_3_1
//		 *
//		 * 0-21_1_1_1-21_2_3_2
//		 * 0-21_1_1_1-21_2_3_2
//		 *
//		 */
//
//		/**
//		 * {
//		 // level : 1
//		 // code : 2_1_1_0
//		 // path : 0-2_1_1_0
//		 // root : 0-2_1_1_0
//		 * }
//			 * {
//			 // level : 2
//			 // code : 2_2_1_1
//			 // path : 0-2_1_1_0-2_2_1_1
//			 // root : 0-2_1_1_0
//			 * }
//		 */
//
//		/**
//		 *  ResearchTreeFormRequest root // 질문
//		 *      ResearchTreeFormRequest child1 // 답변
//		 *      ResearchTreeFormRequest child1 // 답변
//		 *      ResearchTreeFormRequest child1 // 답변
//		 *      ResearchTreeFormRequest child1 // 답변
//		 *      ResearchTreeFormRequest child1 // 답변
//		 *
//		 *          ResearchTreeFormRequest child2 // 질문
//		 *              ResearchTreeFormRequest child3 // 답변
//		 *              ResearchTreeFormRequest child3 // 답변
//		 *              ResearchTreeFormRequest child3 // 답변
//		 *              ResearchTreeFormRequest child3 // 답변
//		 *              ResearchTreeFormRequest child3 // 답변
//		 *
//		 *
//		 *
//		 */
//	}

//	@Test
	public void 일정목록조회() {
		final List<Integer> groupIds = eicnDsl.selectDistinct(SCHEDULE_INFO.GROUP_ID)
				.from(NUMBER_070)
				.leftOuterJoin(SCHEDULE_INFO)
				.on(NUMBER_070.NUMBER.eq(SCHEDULE_INFO.NUMBER).and(SCHEDULE_INFO.COMPANY_ID.eq("primium")))
				.innerJoin(SCHEDULE_GROUP)
				.on(SCHEDULE_INFO.GROUP_ID.eq(SCHEDULE_GROUP.PARENT)
						.and(SCHEDULE_GROUP.COMPANY_ID.eq("primium")))
				.where(NUMBER_070.COMPANY_ID.eq("primium"))
				.fetchInto(Integer.class);

		final List<ScheduleGroupEntity> scheduleGroupLists = scheduleGroupRepository.getScheduleGroupLists();

		List<ScheduleGroupListEntity> r = scheduleGroupLists.stream()
				.filter(e -> groupIds.stream().anyMatch(groupId -> groupId.equals(e.getParent())))
				.flatMap(e -> e.getScheduleGroupLists().stream())
				.collect(Collectors.toList());

		r.stream().forEach(e -> {
			log.info("{}", e.toString());
		});
	}

//	@Test
	public void byServices() {
		List<StatInboundEntity> statInboundEntities = statInboundService.getRepository().groupingByServices();

		for (StatInboundEntity statInboundEntity : statInboundEntities) {
			log.info("data -> {}", statInboundEntity.toString());
		}
	}

//	@Test
	public void query_test() {
		statQueueWaitService.getRepository().findAll();
	}

//	@Test
	public void createTable() {
		final CommonExecutePDSCustomInfo test = new CommonExecutePDSCustomInfo("test888");
		pdsDsl.createTableIfNotExists(test).columns(EXECUTE_PDS_CUSTOM_INFO.fields())
				.constraint( DSL.constraint(test.SEQ.getName()).primaryKey(test.SEQ.getName()))
				.storage("ENGINE=MyISAM")
					.execute();

		final List<Index> indexes = EXECUTE_PDS_CUSTOM_INFO.getIndexes();
		indexes.stream().filter(index -> !"PRIMARY".equals(index.getName())).forEach(index -> pdsDsl.createIndex(index.getName()).on(test.getName(), index.getName()).execute());
	}
}

