package kr.co.eicn.ippbx.front.controller.web.counsel;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbDataController;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbResultController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.MaindbDataSearch;
import kr.co.eicn.ippbx.front.model.search.MaindbResultSearch;
import kr.co.eicn.ippbx.front.model.search.RecordCallSearchForm;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.api.application.sms.SmsCategoryApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.sms.SmsMessageTemplateApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.tel.ServiceApiInterface;
import kr.co.eicn.ippbx.model.form.SendMessageFormRequest;
import kr.co.eicn.ippbx.model.form.SendMessageTemplateFormRequest;
import kr.co.eicn.ippbx.model.form.SendSmsCategoryFormRequest;
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
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMaindbCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo;
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
import kr.co.eicn.ippbx.model.entity.eicn.CommonCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.enums.*;
import kr.co.eicn.ippbx.model.search.*;
import kr.co.eicn.ippbx.model.search.search.SearchServiceRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jooq.tools.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("counsel")
public class CounselController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CounselController.class);

    private final MonitApiInterface monitApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final QueueApiInterface queueApiInterface;
    private final PersonLinkApiInterface personLinkApiInterface;
    private final CounselApiInterface counselApiInterface;
    private final SearchApiInterface searchApiInterface;
    private final RecordingHistoryApiInterface recordingHistoryApiInterface;
    private final MaindbGroupApiInterface maindbGroupApiInterface;
    private final MaindbDataApiInterface maindbDataApiInterface;
    private final MaindbResultApiInterface maindbResultApiInterface;
    private final CommonTypeApiInterface commonTypeApiInterface;
    private final EnumerationValueApiInterface enumerationValueApiInterface;
    private final OrganizationService organizationService;
    private final OutboundDayScheduleApiInterface outboundDayScheduleApiInterface;
    private final GradelistApiInterface gradelistApiInterface;
    private final IvrApiInterface ivrApiInterface;
    private final CallbackHistoryApiInterface callbackHistoryApiInterface;
    private final ArsApiInterface arsApiInterface;
    private final SmsCategoryApiInterface smsCategoryApiInterface;
    private final ServiceApiInterface serviceApiInterface;
    private final SmsMessageTemplateApiInterface smsMessageTemplateApiInterface;

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
        Calendar createdStartDate = Calendar.getInstance();
        createdStartDate.add(Calendar.MONTH, -3);
        search.setCreatedStartDate(new java.sql.Date(createdStartDate.getTime().getTime()));
        search.setLimit(1000);
        if (StringUtils.isNotEmpty(customId))
            search.getDbTypeFields().put("CUSTOM_ID", new MaindbDataSearchRequest.FieldCondition(customId, null, null, null));
        final List<ResultCustomInfoEntity> list = maindbResultApiInterface.getPagination(search).getRows();
        model.addAttribute("list", list);
        return "counsel/counsel-list";
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

        final Map<String, Map<String, String>> codeMap = new HashMap<>();
        customDbType.getFields().stream()
                .filter(e -> e.getCodes() != null && e.getCodes().size() > 0)
                .forEach(e -> {
                    final Map<String, String> codes = e.getCodes().stream().collect(Collectors.toMap(CommonCodeEntity::getCodeId, CommonCodeEntity::getCodeName));
                    codeMap.put(e.getFieldId(), codes);
                });
        model.addAttribute("codeMap", new JSONObject(codeMap));

        model.addAttribute("customIdToFieldNameToValueMap", MaindbDataController.createCustomIdToFieldNameToValueMap((List<CommonMaindbCustomInfo>) (List<?>) pagination.getRows(), customDbType));

        return "counsel/modal-search-maindb-custom-body";
    }

    @GetMapping("modal-search-counseling-history")
    public String modalSearchCounselingHistory() {
        return "counsel/modal-search-counseling-history";
    }

    @SuppressWarnings("unchecked")
    @GetMapping("modal-search-counseling-history-body")
    public String modalSearchCounselingHistoryBody(Model model, @ModelAttribute("search") MaindbResultSearch search) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
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

        final Map<String, Map<String, String>> codeMap = new HashMap<>();
        customDbType.getFields().stream()
                .filter(e -> e.getCodes() != null && e.getCodes().size() > 0)
                .forEach(e -> {
                    final Map<String, String> codes = e.getCodes().stream().collect(Collectors.toMap(CommonCodeEntity::getCodeId, CommonCodeEntity::getCodeName));
                    codeMap.put(e.getFieldId(), codes);
                });
        resultType.getFields().stream()
                .filter(e -> e.getCodes() != null && e.getCodes().size() > 0)
                .forEach(e -> {
                    final Map<String, String> codes = e.getCodes().stream().collect(Collectors.toMap(CommonCodeEntity::getCodeId, CommonCodeEntity::getCodeName));
                    codeMap.put(e.getFieldId(), codes);
                });
        model.addAttribute("codeMap", new JSONObject(codeMap));

        model.addAttribute("seqToFieldNameToValueMap", MaindbResultController.createSeqToFieldNameToValueMap((List<CommonResultCustomInfo>) (List<?>) pagination.getRows(), resultType));
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
        model.addAttribute("extensions", outboundDayScheduleApiInterface.addExtensions().stream().filter(e -> e.getExtension() != null && e.getInUseIdName() != null)
                .sorted(Comparator.comparing(SummaryPhoneInfoResponse::getInUseIdName)).collect(Collectors.toList()));
        // model.addAttribute("numbers", pdsGroupApiInterface.addNumberLists().stream().collect(Collectors.toMap(SummaryNumber070Response::getNumber, SummaryNumber070Response::getNumber)));
        model.addAttribute("queues", new MapToLinkedHashMap().toLinkedHashMapByValue(gradelistApiInterface.queues().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName))));
        model.addAttribute("callStatuses", FormUtils.optionsOfCode(CallStatus.normal_clear, CallStatus.no_answer, CallStatus.user_busy, CallStatus.fail, CallStatus.local_forward));
        model.addAttribute("etcStatuses", FormUtils.optionsOfCode(AdditionalState.HANGUP_BEFORE_CONNECT, AdditionalState.CANCEL_CONNECT, AdditionalState.PICKUPEE, AdditionalState.PICKUPER,
                AdditionalState.TRANSFEREE, AdditionalState.TRANSFERER, AdditionalState.FORWARD_TRANSFEREE, AdditionalState.FORWARD_TRANSFERER, AdditionalState.SCD_TRANSFEREE,
                AdditionalState.SCD_TRANSFERER, AdditionalState.LOCAL_TRANSFEREE, AdditionalState.LOCAL_TRANSFERER));
        final List<IvrTree> ivrTrees = ivrApiInterface.addIvrTreeList();
        model.addAttribute("ivrCodes", ivrTrees.stream().filter(e -> e.getTreeName().split("[_]").length == 1).collect(Collectors.toMap(IvrTree::getCode, IvrTree::getName)));
        model.addAttribute("ivrKeys", ivrTrees.stream().filter(e -> StringUtils.isNotEmpty(e.getButton())).collect(Collectors.groupingBy(IvrTree::getCode)));
        model.addAttribute("customerGrades", FormUtils.optionsOfCode(RecordCallSearch.SearchCustomRating.class));
        model.addAttribute("sortTypes", FormUtils.options(false, RecordCallSearch.Sort.class));
        model.addAttribute("callTimeTypes", FormUtils.options(false, RecordCallSearch.CallTime.class));
        model.addAttribute("callTypes", FormUtils.optionsOfCode(RecordCallSearch.SearchCallType.class));

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
