package kr.co.eicn.ippbx.front.controller.web.counsel;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbDataController;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbResultController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.MaindbDataSearch;
import kr.co.eicn.ippbx.front.model.search.MaindbResultSearch;
import kr.co.eicn.ippbx.front.model.search.RecordCallSearchForm;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.api.application.kms.KmsGraphQLInterface;
import kr.co.eicn.ippbx.front.service.api.application.kms.KmsMemoInterface;
import kr.co.eicn.ippbx.front.service.api.application.sms.SmsCategoryApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.sms.SmsMessageTemplateApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.stt.SttTextApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.tel.ServiceApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.Memo;
import kr.co.eicn.ippbx.model.form.MemoFormRequest;
import kr.co.eicn.ippbx.model.form.SendMessageFormRequest;
import kr.co.eicn.ippbx.model.form.SendMessageTemplateFormRequest;
import kr.co.eicn.ippbx.model.form.SendSmsCategoryFormRequest;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.*;
import kr.co.eicn.ippbx.front.service.api.acd.QueueApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.grade.GradelistApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.code.EnumerationValueApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbResultApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.callback.CallbackHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.history.RecordingHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.api.service.etc.MonitApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.IvrApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.OutboundDayScheduleApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.sounds.ArsApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoKind;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoStatus;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.RecordFile;
import kr.co.eicn.ippbx.model.dto.customdb.CommonEicnCdrResponse;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchMaindbGroupResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.customdb.ResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.enums.*;
import kr.co.eicn.ippbx.model.search.*;
import kr.co.eicn.ippbx.model.search.search.SearchServiceRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * @author tinywind
 */
//@AllArgsConstructor
@RequiredArgsConstructor
@LoginRequired
@Controller
@RequestMapping("counsel")
public class CounselController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CounselController.class);

    private final MonitApiInterface               monitApiInterface;
    private final CompanyApiInterface             companyApiInterface;
    private final QueueApiInterface               queueApiInterface;
    private final PersonLinkApiInterface          personLinkApiInterface;
    private final CounselApiInterface             counselApiInterface;
    private final SearchApiInterface              searchApiInterface;
    private final RecordingHistoryApiInterface    recordingHistoryApiInterface;
    private final MaindbGroupApiInterface         maindbGroupApiInterface;
    private final MaindbDataApiInterface          maindbDataApiInterface;
    private final MaindbResultApiInterface        maindbResultApiInterface;
    private final CommonTypeApiInterface          commonTypeApiInterface;
    private final EnumerationValueApiInterface    enumerationValueApiInterface;
    private final OrganizationService             organizationService;
    private final OutboundDayScheduleApiInterface outboundDayScheduleApiInterface;
    private final GradelistApiInterface           gradelistApiInterface;
    private final IvrApiInterface                 ivrApiInterface;
    private final CallbackHistoryApiInterface     callbackHistoryApiInterface;
    private final ArsApiInterface                 arsApiInterface;
    private final SmsCategoryApiInterface         smsCategoryApiInterface;
    private final ServiceApiInterface             serviceApiInterface;
    private final SmsMessageTemplateApiInterface  smsMessageTemplateApiInterface;
    private final KmsGraphQLInterface             kmsGraphQLInterface;
    private final KmsMemoInterface                kmsMemoInterface;
    private final SttTextApiInterface             sttTextApiInterface;
    private final UserApiInterface                userApiInterface;

    @Value("${assist.stt.request.url}")
    private String sttRequestUrl;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        model.addAttribute("services", new MapToLinkedHashMap().toLinkedHashMapByValue(searchApiInterface.services(new SearchServiceRequest()).stream().collect(Collectors.toMap(ServiceList::getSvcNumber, ServiceList::getSvcName))));
        model.addAttribute("memberStatuses", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));
        return "counsel/main";
    }

    @GetMapping("coworker-navigation")
    public String coworkerNavigation(Model model) throws IOException, ResultFailException {
        final List<MonitControlResponse> list = monitApiInterface.listDashboard(new MonitControlSearchRequest());
        model.addAttribute("list", list);
        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));
        model.addAttribute("memberStatuses", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));

        for (MonitControlResponse monitGroup : list) {
            for (int i = 0; i < monitGroup.getPerson().size(); i++) {
                final PersonListSummary person = monitGroup.getPerson().get(i);
                if (Objects.equals(person.getId(), g.getUser().getId())) {
                    monitGroup.getPerson().remove(person);
                    i--;
                }
            }
        }

        return "counsel/coworker-navigation";
    }

    @GetMapping("call-transfer")
    public String callTransfer(Model model) throws IOException, ResultFailException {
        model.addAttribute("services", queueApiInterface.services());
        model.addAttribute("queues", queueApiInterface.addQueueNames());
        return "counsel/call-transfer";
    }

    @GetMapping("outer-link")
    public String outerLink(Model model) throws IOException, ResultFailException {
        final List<PersonLink> list = personLinkApiInterface.list();
        model.addAttribute("list", list);
        return "counsel/outer-link";
    }

    @GetMapping("current-status")
    public String currentStatus(Model model) throws IOException, ResultFailException {
        final List<TodoDataResponse> list = counselApiInterface.getCurrentStatus();
        model.addAttribute("list", list);
        model.addAttribute("todoKinds", Arrays.asList(TodoListTodoKind.values()));
        return "counsel/current-status";
    }

    @GetMapping("todo-list")
    public String todoList(Model model) throws IOException, ResultFailException {
        final TodoListSearchRequest search = new TodoListSearchRequest();
        search.setUserId(g.getUser().getId());
        search.setStatuses(Arrays.asList(TodoListTodoStatus.ING, TodoListTodoStatus.HOLD));

        final List<TodoList> list = counselApiInterface.getTodoList(search);
        model.addAttribute("list", list);
        model.addAttribute("todoStatuses", FormUtils.options(TodoListTodoStatus.class));
        return "counsel/todo-list";
    }

    @GetMapping("counsel-list")
    public String counselList(Model model, @RequestParam(required = false) String customId) throws IOException, ResultFailException {
        final ResultCustomInfoSearchRequest search = new ResultCustomInfoSearchRequest();
        final Calendar createdStartDate = Calendar.getInstance();
        createdStartDate.add(Calendar.MONTH, -3);
        search.setCreatedStartDate(new java.sql.Date(createdStartDate.getTime().getTime()));
        search.setLimit(1000);

        if (StringUtils.isNotEmpty(customId))
            search.getDbTypeFields().put("CUSTOM_ID", new MaindbDataSearchRequest.FieldCondition(customId, null, null, null));

        final List<ResultCustomInfoEntity> list = maindbResultApiInterface.getPagination(search).getRows();
        model.addAttribute("list", list);

        return "counsel/counsel-list";
    }

    /**
     * KMS 관련 컨트롤러 시작
     */
    @GetMapping("assist-sso")
    public ResponseEntity<HashMap<String, String>> kmsCustomSidebarList(HttpSession session) throws JsonProcessingException {
        final String sessionId = session.getId();
        final HashMap<String, String> result = kmsGraphQLInterface.getSSOToken(sessionId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("kms-custom-sidebar")
    public String kmsCustomSidebarList(Model model, @RequestParam(required = false) Integer id, @RequestParam(required = false) String keyword, @RequestParam(required = false) String sort) throws IOException {
        // KMS 로그인이 불가능한 상태라면, 어시스트 관련 사이드바 노출 하지 않음.
        if (g.getUsingServices().contains("ASTIN") && g.getUser().getIsAstIn().equals("Y")) {
            final Boolean availabilityCheck = kmsGraphQLInterface.loginAvailabilityCheck();
            if (!availabilityCheck) {
                return "counsel/kms-custom-sidebar-empty";
            }
        }

        if (g.getUsingServices().contains("ASTIN") && g.getUser().getIsAstIn().equals("Y")) {
            final List<KmsGraphQLInterface.GetKnowledgeCategory> category = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories();
            model.addAttribute("kmsCategoryMenu", category.stream().filter(e -> e.getLevel() == 0).collect(Collectors.toList()));
            model.addAttribute("kmsCategoryList", category.stream().collect(Collectors.toMap(KmsGraphQLInterface.GetKnowledgeCategory::getId, e -> e)));
            model.addAttribute("kmsList", kmsGraphQLInterface.getSearchKnowledge(keyword, id, sort, "all").getData().getSearchKnowledgeBySolr().getRows());
        } else {
            model.addAttribute("kmsCategoryMenu", new ArrayList<>());
            model.addAttribute("kmsCategoryList", new HashMap<>());
            model.addAttribute("kmsList", new ArrayList<>());
        }
        return "counsel/kms-custom-sidebar";
    }

    // kms 리스트 가져오기 (json)
    @GetMapping("kms/list")
    public ResponseEntity<JsonResult<Map<Object, List<?>>>> getKmsLists(Model model, @RequestParam(required = false) Integer id, @RequestParam(required = false) String keyword, @RequestParam(required = false) String sort, @RequestParam(required = false, name = "search_type_flag") String searchTypeFlag) throws IOException {
        // searchType이 tag면 '태그' 검색, all이면 '태그/제목/내용' 검색
        final List<KmsGraphQLInterface.GetKnowledgeCategory> categoryList = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories();
        final List<KmsGraphQLInterface.Knowledge> kmsList = kmsGraphQLInterface.getSearchKnowledge(keyword, id, sort, searchTypeFlag).getData().getSearchKnowledgeBySolr().getRows();
        final Map<Object, List<?>> result = Map.of("categoryList", categoryList, "kmsList", kmsList);
        return ResponseEntity.ok(data(result));
    }

    // kms 지식정보 상세 조회
    @GetMapping("kms/{id}")
    public ResponseEntity<JsonResult<Map<Object, Object>>> getKmsView(Model model, @PathVariable Integer id) throws IOException {
        final KmsGraphQLInterface.Knowledge getKnowledge = kmsGraphQLInterface.getSearchIdKnowledge(id).getData().getGetKnowledge();
        final List<KmsGraphQLInterface.GetKnowledgeCategory> category = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories();
        model.addAttribute("categoryList", category);

        final Map<Object, Object> result = Map.of("categoryList", category, "kmsList", getKnowledge);
        return ResponseEntity.ok(data(result));
    }

    // kms 지식정보 상세 조회 (modal)
    @GetMapping("kms/{id}/modal")
    public String getKmsViewModal(Model model, @PathVariable Integer id) throws IOException {
        model.addAttribute("kms", kmsGraphQLInterface.getSearchIdKnowledge(id).getData().getGetKnowledge());
        return "counsel/modal-kms-view";
    }

    // kms like
    @GetMapping("kms/{id}/like")
    public ResponseEntity<JsonResult<KmsGraphQLInterface.Knowledge>> kmsLike(Model model, @PathVariable Integer id, @RequestParam boolean like) throws IOException {
        kmsGraphQLInterface.likeKnowledge(id, like);
        final KmsGraphQLInterface.Knowledge getKnowledge = kmsGraphQLInterface.getSearchIdKnowledge(id).getData().getGetKnowledge();
        return ResponseEntity.ok(data(getKnowledge));
    }

    // kms 북마크 하기
    @GetMapping("kms/{id}/bookmark")
    public ResponseEntity<JsonResult<KmsGraphQLInterface.Knowledge>> kmsBookmark(Model model, @PathVariable Integer id, @RequestParam boolean bookmark) throws IOException {
        kmsGraphQLInterface.bookmarkKnowledge(id, bookmark);
        final KmsGraphQLInterface.Knowledge getKnowledge = kmsGraphQLInterface.getSearchIdKnowledge(id).getData().getGetKnowledge();
        return ResponseEntity.ok(data(getKnowledge));
    }

    // kms 북마크 리스트 가져오기
    @GetMapping("kms/bookmark")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> kmsBookmarkList() throws IOException, ResultFailException {
        final List<KmsGraphQLInterface.KnowledgeItem> bookmarkList = kmsGraphQLInterface.getMyBookmarkedKnowledge().getData().getGetMyBookmarkedKnowledge().getRows();
        final List<KmsGraphQLInterface.GetKnowledgeCategory> category = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories();

        final HashMap<String, Object> result = new HashMap<>();
        result.put("bookmarkList", bookmarkList);
        result.put("categoryList", category);

        return ResponseEntity.ok(data(result));
    }

    // component 모음
    @GetMapping(value = "kms/component", params = "type=bookmark")
    public String getKmsComponentBookmark(Model model) throws IOException, ResultFailException {
        final List<KmsGraphQLInterface.KnowledgeItem> bookmarkList = kmsGraphQLInterface.getMyBookmarkedKnowledge().getData().getGetMyBookmarkedKnowledge().getRows();
        final List<KmsGraphQLInterface.GetKnowledgeCategory> category = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories();
        model.addAttribute("categoryList", category);
        model.addAttribute("bookmarkList", bookmarkList);

        return "counsel/component-bookmark";
    }

    @GetMapping(value = "kms/component", params = "type=memo")
    public String getKmsComponentNote(Model model) throws IOException, ResultFailException {
        final List<Memo> memoList = kmsMemoInterface.getMemoList("");
        model.addAttribute("memoList", memoList);

        return "counsel/component-memo";
    }

    @GetMapping(value = "kms/component", params = "type=recent")
    public String getKmsComponentRecentRead(Model model) throws IOException {
        final List<KmsGraphQLInterface.Knowledge> recentList = kmsGraphQLInterface.getRecentChangedKnowledge().getData().getGetRecentChangedKnowledge().getRows();
        final List<KmsGraphQLInterface.GetKnowledgeCategory> category = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories();
        model.addAttribute("recentList", recentList);
        model.addAttribute("categoryList", category);

        return "counsel/component-recent-read";
    }

    @GetMapping(value = "kms/component", params = "type=hits-rank")
    public String getKmsComponentHitsRank(Model model) throws IOException {
        // 현재월의 첫번째 일자
        final LocalDate firstDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        final String defaultStartDate = firstDate.format(DateTimeFormatter.ISO_DATE);

        // 현재월의 마지막 일자
        final LocalDate lastDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        final String defaultEndDate = lastDate.format(DateTimeFormatter.ISO_DATE);

        final List<KmsGraphQLInterface.GetTopHitKnowledge> hitsRankList = kmsGraphQLInterface.getTopHitKnowledge(defaultStartDate, defaultEndDate).getData().getGetTopHitKnowledge();
        final List<KmsGraphQLInterface.GetKnowledgeCategory> category = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories();
        model.addAttribute("hitsRankList", hitsRankList);
        model.addAttribute("categoryList", category);

        return "counsel/component-hits-rank";
    }

    @GetMapping(value = "kms/component", params = "type=hits-tag-rank")
    public String getKmsComponentHitsTagRank(Model model) throws IOException {
        // 현재월의 첫번째 일자
        final LocalDate firstDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        final String defaultStartDate = firstDate.format(DateTimeFormatter.ISO_DATE);

        // 현재월의 마지막 일자
        final LocalDate lastDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        final String defaultEndDate = lastDate.format(DateTimeFormatter.ISO_DATE);

        final List<KmsGraphQLInterface.KnowledgeTags> hitsTagRankList = kmsGraphQLInterface.getTopHitKnowledgeTags(defaultStartDate, defaultEndDate).getData().getGetTopHitKnowledgeTags();
        model.addAttribute("hitsTagRankList", hitsTagRankList);

        return "counsel/component-hits-tag-rank";
    }

    @GetMapping(value = "kms/component", params = "type=recommend-rank")
    public String getKmsComponentRecommendRank(Model model) throws IOException {
        final List<KmsGraphQLInterface.GetKnowledgeCategory> categoryList = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories();
        final List<KmsGraphQLInterface.Knowledge> recommendList = kmsGraphQLInterface.getSearchKnowledge("", null, "LIKES", "all").getData().getSearchKnowledgeBySolr().getRows();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("recommendList", recommendList);

        return "counsel/component-recommend-rank";
    }

    /**
     * 처음 JSP로 화면을 만들때 사용
     *
     * @param model
     * @param commentFilter
     * @return
     * @throws IOException
     */
    @GetMapping(value = "kms/component", params = "type=comment")
    public String getKmsComponentComment(Model model, @RequestParam(value = "commentFilter", required = false, defaultValue = "") String commentFilter) throws IOException {
        final PersonDetailResponse user = g.getUser();
        final String userName = user.getIdName();
        final String idType = user.getIdType();

        final List<KmsGraphQLInterface.GetKnowledgeCategory> categoryList = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories();
        model.addAttribute("categoryList", categoryList);

        List<kr.co.eicn.ippbx.front.service.api.application.kms.KmsGraphQLInterface.SearchKnowledgeUpdate> commentList = null;
        if (idType.equals("A")) {
            commentList = kmsGraphQLInterface.searchKnowledgeUpdateRequestForAdmin().getData().getSearchKnowledgeUpdateRequest().getRows();
        } else {
            // 상담사 화면을 위한 호출 이므로 userName과 requesterName으로 필터
            commentList = kmsGraphQLInterface.searchKnowledgeUpdateRequestForCounsel(commentFilter).getData().getSearchKnowledgeUpdateRequest().getRows();

            final List<KmsGraphQLInterface.SearchKnowledgeUpdate> filterCommentList = commentList.stream().filter(comment -> {
                if (StringUtils.isNotEmpty(comment.getRequesterName()) && StringUtils.equals(comment.getRequesterName(), userName))
                    return true;
                return false;
            }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(filterCommentList)) {
                commentList = filterCommentList;
            }
        }

        if (CollectionUtils.isNotEmpty(commentList)) {
            commentList.forEach(e -> {
                // createAt 날짜 포맷 변경하기
                final Instant instant = Instant.ofEpochMilli(e.getCreatedAt());
                final LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                e.setFormatCreatedAt(dateTime.format(formatter));

                // KMS 글의 카테고리명 넣어주기
                categoryList.forEach(category -> {
                    if (StringUtils.equals(category.getId().toString(), e.getKnowledge().getCategory().toString())) {
                        e.setCategoryName(category.getName());
                    }
                });
            });
        }

        model.addAttribute("commentList", commentList);

        return "counsel/component-comment";
    }

    /**
     * JSP로 화면을 보는중, 새로고침이나 전체/답변완료/미답변 등 화면이 다시 렌더링 되어야 하는 경우 restSelf로 json을 가져와 수정하게 만들었음.
     *
     * @param commentFilterType
     * @return
     * @throws IOException
     */
    @GetMapping(value = "kms/comment/json")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> getKmsCommentListJson(@RequestParam(value = "commentFilterType", required = false) String commentFilterType) throws IOException {
        final PersonDetailResponse user = g.getUser();
        final String userName = user.getIdName();
        final String idType = user.getIdType();

        final List<KmsGraphQLInterface.GetKnowledgeCategory> categoryList = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories();
        List<kr.co.eicn.ippbx.front.service.api.application.kms.KmsGraphQLInterface.SearchKnowledgeUpdate> commentList = null;
        if (idType.equals("A")) {
            commentList = kmsGraphQLInterface.searchKnowledgeUpdateRequestForAdmin().getData().getSearchKnowledgeUpdateRequest().getRows();
        } else {
            // 상담사 화면을 위한 호출 이므로 userName과 requesterName으로 필터
            commentList = kmsGraphQLInterface.searchKnowledgeUpdateRequestForCounsel(commentFilterType).getData().getSearchKnowledgeUpdateRequest().getRows();

            final List<KmsGraphQLInterface.SearchKnowledgeUpdate> filterCommentList = commentList.stream().filter(comment -> {
                if (StringUtils.isNotEmpty(comment.getRequesterName()) && StringUtils.equals(comment.getRequesterName(), userName))
                    return true;
                return false;
            }).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(filterCommentList)) {
                commentList = filterCommentList;
            }
        }

        if (CollectionUtils.isNotEmpty(commentList)) {
            commentList.forEach(e -> {
                // createAt 날짜 포맷 변경하기
                final Instant instant = Instant.ofEpochMilli(e.getCreatedAt());
                final LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                e.setFormatCreatedAt(dateTime.format(formatter));

                // KMS 글의 카테고리명 넣어주기
                categoryList.forEach(category -> {
                    if (StringUtils.equals(category.getId().toString(), e.getKnowledge().getCategory().toString())) {
                        e.setCategoryName(category.getName());
                    }
                });
            });
        }

        final HashMap<String, Object> result = new HashMap<>();
        result.put("commentList", commentList);
        result.put("categoryList", categoryList);

        return ResponseEntity.ok(data(result));
    }

    // 메모 추가를 위한 모달
    @GetMapping(value = "kms/memo/new/modal")
    public String getKmsNewModal() {
        return "counsel/kms-memo-modal";
    }

    // 메모 추가
    @PostMapping(value = "kms/memo/new")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> createKmsMemo(@Valid MemoFormRequest formRequest) throws IOException, ResultFailException {
        kmsMemoInterface.createMemo(formRequest);

        final HashMap<String, Object> result = new HashMap<>();
        result.put("result", "success");
        return ResponseEntity.ok(data(result));
    }

    // 메모 조회
    @GetMapping(value = "kms/memo")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> getKmsMemo(@RequestParam(required = false) String keyword) throws IOException, ResultFailException {
        final String searchKeyword = keyword != null ? keyword : "";
        final List<Memo> memoList = kmsMemoInterface.getMemoList(searchKeyword);

        final HashMap<String, Object> result = new HashMap<>();
        result.put("kmsMemoList", memoList);

        return ResponseEntity.ok(data(result));
    }

    // 메모 수정
    @PutMapping(value = "kms/memo/{id}")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> updateKmsMemo(@PathVariable Long id, @Valid @RequestBody MemoFormRequest formRequest) throws IOException, ResultFailException {
        kmsMemoInterface.updateMemo(id, formRequest);

        final HashMap<String, Object> result = new HashMap<>();
        result.put("result", "success");

        return ResponseEntity.ok(data(result));
    }

    // 메모 삭제
    @DeleteMapping(value = "kms/memo/{id}")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> deleteKmsMemo(@PathVariable Long id) throws IOException, ResultFailException {
        kmsMemoInterface.deleteMemo(id);

        final HashMap<String, Object> result = new HashMap<>();
        result.put("result", "success");

        return ResponseEntity.ok(data(result));
    }

    // 메모 북마크
    @PutMapping(value = "kms/memo/{id}/bookmarked/{bookmarked}")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> bookmarkKmsMemo(@PathVariable Long id, @PathVariable boolean bookmarked) throws IOException, ResultFailException {
        kmsMemoInterface.bookmarkMemo(id, bookmarked);

        final HashMap<String, Object> result = new HashMap<>();
        result.put("result", "success");

        return ResponseEntity.ok(data(result));
    }

    // 최근열람지식
    @GetMapping(value = "kms/recent-read")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> getKmsRecentRead() throws IOException, ResultFailException {
        final List<KmsGraphQLInterface.Knowledge> recentList = kmsGraphQLInterface.getRecentChangedKnowledge().getData().getGetRecentChangedKnowledge().getRows();
        final List<KmsGraphQLInterface.GetKnowledgeCategory> categoryList = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories().stream().collect(Collectors.toList());

        final HashMap<String, Object> result = new HashMap<>();
        result.put("recentList", recentList);
        result.put("categoryList", categoryList);

        return ResponseEntity.ok(data(result));
    }

    // 조회랭킹
    @GetMapping(value = "kms/hits-rank")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> getKmsHitsRank(@RequestParam String startDate, @RequestParam String endDate) throws IOException, ResultFailException {
        final List<KmsGraphQLInterface.GetTopHitKnowledge> hitsRankList = kmsGraphQLInterface.getTopHitKnowledge(startDate, endDate).getData().getGetTopHitKnowledge();
        final List<KmsGraphQLInterface.GetKnowledgeCategory> categoryList = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories().stream().collect(Collectors.toList());

        final HashMap<String, Object> result = new HashMap<>();
        result.put("hitsRankList", hitsRankList);
        result.put("categoryList", categoryList);

        return ResponseEntity.ok(data(result));
    }

    // 키워드랭킹
    @GetMapping(value = "kms/hits-tag-rank")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> getKmsHitsTagRank(@RequestParam String startDate, @RequestParam String endDate) throws IOException, ResultFailException {
        final List<KmsGraphQLInterface.KnowledgeTags> hitsTagRankList = kmsGraphQLInterface.getTopHitKnowledgeTags(startDate, endDate).getData().getGetTopHitKnowledgeTags();

        final HashMap<String, Object> result = new HashMap<>();
        result.put("hitsTagRankList", hitsTagRankList);

        return ResponseEntity.ok(data(result));
    }

    // 추천랭킹
    @GetMapping(value = "kms/recommend-rank")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> getKmsRecommendRank(Model model) throws IOException, ResultFailException {
        final List<KmsGraphQLInterface.GetKnowledgeCategory> categoryList = kmsGraphQLInterface.getCategory().getData().getGetKnowledgeCategories().stream().collect(Collectors.toList());
        final List<KmsGraphQLInterface.Knowledge> recommendList = kmsGraphQLInterface.getSearchKnowledge("", null, "LIKES", "all").getData().getSearchKnowledgeBySolr().getRows();

        final HashMap<String, Object> result = new HashMap<>();
        result.put("categoryList", categoryList);
        result.put("recommendList", recommendList);

        return ResponseEntity.ok(data(result));
    }

    @GetMapping(value = "kms/comment")
    public ResponseEntity<JsonResult<HashMap<String, Object>>> createKmsComment(@RequestParam Integer id, @RequestParam String content) throws IOException, ResultFailException {
        final KmsGraphQLInterface.CreateKnowledgeUpdateRequestResult comment = kmsGraphQLInterface.createComment(id, content);

        final HashMap<String, Object> result = new HashMap<>();
        result.put("createCommentId", comment.getData().getCreateKnowledgeUpdateRequest().getId());
        result.put("result", "success");

        return ResponseEntity.ok(data(result));
    }

    @GetMapping("modal-bookmark")
    public String modalBookmark(Model model) throws IOException, ResultFailException {
        final List<PersonLink> list = personLinkApiInterface.list();
        model.addAttribute("list", list);

        return "counsel/modal-bookmark";
    }

    @GetMapping("modal-config")
    public String modalConfig(Model model) throws IOException, ResultFailException {
        model.addAttribute("todoKinds", FormUtils.options(TodoListTodoKind.TALK, TodoListTodoKind.CALLBACK, TodoListTodoKind.RESERVE, TodoListTodoKind.TRANSFER));

        return "counsel/modal-config";
    }

    @GetMapping("modal-counseling-info")
    public String modalCounselInfo(Model model, @RequestParam Integer seq) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ResultCustomInfoEntity entity = maindbResultApiInterface.get(seq);
        model.addAttribute("entity", entity);
        final CommonTypeEntity maindbType = commonTypeApiInterface.get(entity.getGroupType());
        model.addAttribute("maindbType", maindbType);
        final CommonTypeEntity resultType = commonTypeApiInterface.get(entity.getResultType());
        model.addAttribute("resultType", resultType);
        final Map<String, Object> customFieldNameToValueMap = MaindbDataController.createFieldNameToValueMap(entity.getCustomInfo(), maindbType);
        model.addAttribute("customFieldNameToValueMap", customFieldNameToValueMap);
        final Map<String, Object> resultFieldNameToValueMap = MaindbResultController.createFieldNameToValueMap(entity, resultType);
        model.addAttribute("resultFieldNameToValueMap", resultFieldNameToValueMap);

        if (StringUtils.isNotEmpty(entity.getUniqueid())) {
            final List<RecordFile> files = maindbResultApiInterface.getFiles(entity.getUniqueid());
            model.addAttribute("files", files);
        }

        return "counsel/modal-counseling-info";
    }

    @GetMapping("modal-stt-info")
    public String modalSttInfo(Model model, @RequestParam Integer seq) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ResultCustomInfoEntity entity = maindbResultApiInterface.get(seq);
        model.addAttribute("uniqueId", entity.getUniqueid());
        model.addAttribute("phoneNumber", entity.getMultichannelList().stream().filter(e -> e.getChannelType().equals(MultichannelChannelType.PHONE.getCode())).collect(Collectors.toList()).get(0).getChannelData());
        model.addAttribute("sttRequestUrl", sttRequestUrl);
        logger.info("sttRequestUrl = {}", sttRequestUrl);

        if (StringUtils.isNotEmpty(entity.getUniqueid())) {
            final List<RecordFile> files = maindbResultApiInterface.getFiles(entity.getUniqueid());
            model.addAttribute("files", files);
        }

        return "counsel/modal-stt-info";
    }

    @GetMapping("modal-stt-monit/{userId}")
    public String modalSttMonit(Model model, @PathVariable String userId) throws IOException, ResultFailException {
        final String callUniqueId = sttTextApiInterface.adminMonit(userId);
        model.addAttribute("callUniqueId", callUniqueId);
        model.addAttribute("sttCdr", sttTextApiInterface.getSttCdr(callUniqueId));
        model.addAttribute("userId", userId);
        model.addAttribute("personListMap", userApiInterface.list(new PersonSearchRequest()).stream().collect(Collectors.toMap(PersonSummaryResponse::getId, PersonSummaryResponse::getIdName)));

        return "counsel/modal-stt-monit";
    }

    @GetMapping("modal-stt-unique-info")
    public String modalSttUniqueInfo(
            Model model, @RequestParam String uniqueId, @RequestParam String phoneNumber, @RequestParam String dialupDate, @RequestParam String hangupDate
    ) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        model.addAttribute("uniqueId", uniqueId);
        model.addAttribute("phoneNumber", phoneNumber);
        model.addAttribute("sttRequestUrl", sttRequestUrl);
        model.addAttribute("dialupDate", dialupDate);
        model.addAttribute("hangupDate", hangupDate);

        final List<RecordFile> files = maindbResultApiInterface.getFiles(uniqueId);
        model.addAttribute("files", files);

        return "counsel/modal-stt-info";
    }

    @GetMapping("modal-search-maindb-custom")
    public String modalSearchMaindbCustom() {
        return "counsel/modal-search-maindb-custom";
    }

    @SuppressWarnings("unchecked")
    @GetMapping("modal-search-maindb-custom-body")
    public String modalSearchMaindbCustomBody(Model model, @ModelAttribute("search") MaindbDataSearch search) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        search.setLimit(100);

        final List<SearchMaindbGroupResponse> customdbGroups = maindbDataApiInterface.customdbGroup();
        if (customdbGroups.size() == 0)
            throw new IllegalStateException("고객DB그룹이 존재하지 않습니다.");

        final Map<Integer, String> groups = customdbGroups.stream().collect(Collectors.toMap(SearchMaindbGroupResponse::getSeq, SearchMaindbGroupResponse::getName));
        model.addAttribute("customdbGroups", new MapToLinkedHashMap().toLinkedHashMapByValue(groups));

        if (search.getGroupSeq() == null)
            search.setGroupSeq(customdbGroups.get(0).getSeq());

        final Pagination<MaindbCustomInfoEntity> pagination = maindbDataApiInterface.getPaginationCounsel(search.getGroupSeq(), search.convertToRequest(""));
        model.addAttribute("pagination", pagination);

        final MaindbGroupDetailResponse customGroup = maindbGroupApiInterface.get(search.getGroupSeq());
        final CommonTypeEntity customDbType = commonTypeApiInterface.get(customGroup.getMaindbType());
        model.addAttribute("customDbType", customDbType);
        model.addAttribute("customIdToFieldNameToValueMap", MaindbDataController.createCustomIdToFieldNameToValueMap(pagination.getRows(), customDbType));

        return "counsel/modal-search-maindb-custom-body";
    }

    @GetMapping("modal-search-counseling-history")
    public String modalSearchCounselingHistory() {
        return "counsel/modal-search-counseling-history";
    }

    @SuppressWarnings("unchecked")
    @GetMapping("modal-search-counseling-history-body")
    public String modalSearchCounselingHistoryBody(Model model, @ModelAttribute("search") MaindbResultSearch search, @RequestParam(required = false) String mode) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (StringUtils.isNotEmpty(mode))
            model.addAttribute("mode", mode);

        search.setLimit(100);

        final List<SearchMaindbGroupResponse> customdbGroups = maindbResultApiInterface.customdbGroup();
        if (customdbGroups.size() == 0)
            throw new IllegalStateException("고객DB그룹이 존재하지 않습니다.");

        final Map<Integer, String> groups = customdbGroups.stream().collect(Collectors.toMap(SearchMaindbGroupResponse::getSeq, SearchMaindbGroupResponse::getName));
        model.addAttribute("customdbGroups", new MapToLinkedHashMap().toLinkedHashMapByValue(groups));

        if (search.getGroupSeq() == null)
            search.setGroupSeq(customdbGroups.get(0).getSeq());

//        if (g.getUser().getIdType().equals(IdType.USER.getCode()))
//            search.setUserId(g.getUser().getId());

        final Pagination<ResultCustomInfoEntity> pagination = maindbResultApiInterface.getPaginationCounsel(search.getGroupSeq(), search.convertToRequest(""));
        model.addAttribute("pagination", pagination);

        final MaindbGroupDetailResponse customGroup = maindbGroupApiInterface.get(search.getGroupSeq());
        final CommonTypeEntity customDbType = commonTypeApiInterface.get(customGroup.getMaindbType());
        model.addAttribute("customDbType", customDbType);
        final CommonTypeEntity resultType = commonTypeApiInterface.get(customGroup.getResultType());
        model.addAttribute("resultType", resultType);

        model.addAttribute("seqToFieldNameToValueMap", MaindbResultController.createSeqToFieldNameToValueMap(pagination.getRows(), resultType));
        model.addAttribute("customIdToFieldNameToValueMap", MaindbDataController.createCustomIdToFieldNameToValueMap(pagination.getRows().stream().map(ResultCustomInfoEntity::getCustomInfo).collect(Collectors.toList()), customDbType));

        model.addAttribute("users", searchApiInterface.persons());

        return "counsel/modal-search-counseling-history-body";
    }

    @GetMapping("modal-search-call-history")
    public String modalSearchCallHistory() {
        return "counsel/modal-search-call-history";
    }

    @GetMapping("modal-search-call-history-body")
    public String modalSearchCallHistoryBody(Model model, @ModelAttribute("search") RecordCallSearchForm search) throws IOException, ResultFailException {
        search.setLimit(100);

        final Pagination<CommonEicnCdrResponse> pagination = recordingHistoryApiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(search.getGroupCode()));
        model.addAttribute("persons", searchApiInterface.persons());

        model.addAttribute("extensions", outboundDayScheduleApiInterface.addExtensions().stream()
                .filter(e -> e.getExtension() != null && e.getInUseIdName() != null).sorted(Comparator.comparing(SummaryPhoneInfoResponse::getInUseIdName)).collect(Collectors.toList()));
        model.addAttribute("sortTypes", FormUtils.options(false, RecordCallSearch.Sort.class));

        model.addAttribute("callTypes", FormUtils.optionsOfCode(RecordCallSearch.SearchCallType.class));
        model.addAttribute("callStatuses", FormUtils.optionsOfCode(CallStatus.normal_clear, CallStatus.no_answer, CallStatus.user_busy, CallStatus.fail, CallStatus.local_forward));
        model.addAttribute("etcStatuses", FormUtils.optionsOfCode(
                AdditionalState.HANGUP_BEFORE_CONNECT, AdditionalState.CANCEL_CONNECT,
                AdditionalState.PICKUPEE, AdditionalState.PICKUPER,
                AdditionalState.TRANSFERER, AdditionalState.TRANSFEREE,
                AdditionalState.REDIRECTOUT_TRANSFERER, AdditionalState.REDIRECTOUT_TRANSFEREE,
                AdditionalState.SCD_TRANSFERER, AdditionalState.SCD_TRANSFEREE,
                AdditionalState.LOCAL_TRANSFERER, AdditionalState.LOCAL_TRANSFEREE,
                AdditionalState.FORWARD_TRANSFERER, AdditionalState.FORWARD_TRANSFEREE));

        final List<IvrTree> ivrTrees = ivrApiInterface.addIvrTreeList();
        model.addAttribute("ivrCodes", new MapToLinkedHashMap().toLinkedHashMapByValue(ivrTrees.stream().filter(e -> e.getTreeName().split("_").length == 1).collect(Collectors.toMap(IvrTree::getCode, IvrTree::getName))));
        model.addAttribute("ivrKeys", ivrTrees.stream().filter(e -> StringUtils.isNotEmpty(e.getButton())).collect(Collectors.groupingBy(IvrTree::getCode)));
        model.addAttribute("services", serviceApiInterface.addServices());

        model.addAttribute("callTimeTypes", FormUtils.options(false, RecordCallSearch.CallTime.class));
        model.addAttribute("queues", new MapToLinkedHashMap().toLinkedHashMapByValue(gradelistApiInterface.queues().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName))));

        return "counsel/modal-search-call-history-body";
    }

    @GetMapping("modal-search-callback-history")
    public String modalSearchCallbackHistory() {
        return "counsel/modal-search-callback-history";
    }

    @GetMapping("modal-search-callback-history-body")
    public String modalSearchCallbackHistoryBody(Model model, @ModelAttribute("search") CallbackHistorySearchRequest search) throws IOException, ResultFailException {
        search.setLimit(100);

        final Pagination<CallbackHistoryResponse> pagination = callbackHistoryApiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final LinkedHashMap<String, String> callbackStatusOptions = FormUtils.options(true, CallbackStatus.class);
        model.addAttribute("callbackStatusOptions", callbackStatusOptions);

        final Map<String, String> serviceOptions = new HashMap<>();
        serviceOptions.put("", "");
        searchApiInterface.services(new SearchServiceRequest()).forEach(e -> serviceOptions.put(e.getSvcNumber(), e.getSvcName()));
        model.addAttribute("serviceOptions", new MapToLinkedHashMap().toLinkedHashMapByValue(serviceOptions));

        return "counsel/modal-search-callback-history-body";
    }

    @GetMapping("modal-field-info")
    public String modalFieldInfo(Model model, @RequestParam Integer type, @RequestParam String fieldId, @RequestParam List<String> selectItem) throws IOException, ResultFailException {
        final CommonFieldResponse field = enumerationValueApiInterface.getField(type, fieldId);
        List<CommonCodeDetailResponse> detail = field.getCommonCodes().stream().filter(e -> selectItem.contains(e.getCodeId())).collect(Collectors.toList());
        field.setCommonCodes(detail);
        model.addAttribute("field", field);

        return "counsel/modal-field-info";
    }

    @GetMapping("modal-calling")
    public String modalCalling(Model model) {
        return "counsel/modal-calling";
    }

    @GetMapping("modal-cms")
    public String modalCms(Model model) {
        return "counsel/modal-cms";
    }

    @GetMapping("modal-send-message")
    public String modalSendMessage(Model model) {
        return "counsel/modal-send-message";
    }

    @GetMapping("modal-ars")
    public String modalArs(Model model) throws IOException, ResultFailException {
        final SoundListSearchRequest search = new SoundListSearchRequest();
        search.setArs(true);
        final Map<Integer, String> sounds = arsApiInterface.list(search).stream().collect(Collectors.toMap(SoundListSummaryResponse::getSeq, SoundListSummaryResponse::getSoundName));
        model.addAttribute("sounds", new MapToLinkedHashMap().toLinkedHashMapByValue(sounds));
        model.addAttribute("ProtectArs", FormUtils.optionsOfCode(ProtectArs.class));

        return "counsel/modal-ars";
    }

    @GetMapping("modal-sms-send")
    public String modalSmsSend(Model model, @RequestParam String phoneNumber, @ModelAttribute("form") SendMessageFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("phoneNumber", phoneNumber);
        model.addAttribute("smsCategories", smsCategoryApiInterface.list());
        model.addAttribute("serviceLists", serviceApiInterface.listCounsel(new ServiceListSearchRequest()));
        model.addAttribute("smsTemplates", smsMessageTemplateApiInterface.list().stream().collect(Collectors.groupingBy(SendMessageTemplateResponse::getCategoryCode)));
        return "modal-sms-send";
    }

    @GetMapping("modal-route-application")
    public String modalRouteApplication(Model model) {
        return "counsel/modal-route-application";
    }

    @GetMapping("modal-sms-category")
    public String modalSmsCategory(Model model, @ModelAttribute("form") SendSmsCategoryFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("categories", smsCategoryApiInterface.list());

        return "counsel/modal-sms-category";
    }

    @GetMapping("modal-sms-template")
    public String modalSmsTemplate(Model model, @ModelAttribute("form") SendMessageTemplateFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("categories", smsCategoryApiInterface.list());
        model.addAttribute("templates", smsMessageTemplateApiInterface.list());

        return "counsel/modal-sms-template";
    }

    enum MaindbCustomSearchType {
        TALK, CALL
    }
}
