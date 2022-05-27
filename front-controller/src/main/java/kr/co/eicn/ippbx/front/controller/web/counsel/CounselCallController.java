package kr.co.eicn.ippbx.front.controller.web.counsel;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbDataController;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbResultController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.RecordCallSearchForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.CounselApiInterface;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.grade.GradelistApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbResultApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.display.ScreenDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.voc.VocGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.history.RecordingHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.api.stat.ConsultantStatApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.group.WtalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoKind;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoStatus;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CmpMemberStatusCode;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup;
import kr.co.eicn.ippbx.model.dto.customdb.CommonEicnCdrResponse;
import kr.co.eicn.ippbx.model.dto.customdb.CustomMultichannelInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbGroupSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkServiceResponse;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbMultichannelInfoEntity;
import kr.co.eicn.ippbx.model.entity.customdb.ResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.GradeListEntity;
import kr.co.eicn.ippbx.model.enums.MultichannelChannelType;
import kr.co.eicn.ippbx.model.form.MaindbCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.*;
import kr.co.eicn.ippbx.model.search.search.SearchServiceRequest;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("counsel/call")
public class CounselCallController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CounselCallController.class);

    private final CounselApiInterface counselApiInterface;
    private final SearchApiInterface searchApiInterface;
    private final UserApiInterface userApiInterface;
    private final RecordingHistoryApiInterface recordingHistoryApiInterface;
    private final MaindbGroupApiInterface maindbGroupApiInterface;
    private final MaindbDataApiInterface maindbDataApiInterface;
    private final CommonTypeApiInterface commonTypeApiInterface;
    private final GradelistApiInterface gradelistApiInterface;
    private final VocGroupApiInterface vocGroupApiInterface;
    private final WtalkReceptionGroupApiInterface talkReceptionGroupApiInterface;
    private final MaindbResultApiInterface maindbResultApiInterface;
    private final ScreenDataApiInterface screenDataApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final ConsultantStatApiInterface consultantStatApiInterface;

    @GetMapping("")
    public String callPanel(Model model) throws IOException, ResultFailException {
        model.addAttribute("services", searchApiInterface.services(new SearchServiceRequest()).stream().collect(Collectors.toMap(ServiceList::getSvcCid, ServiceList::getSvcName)));
        return "counsel/call/panel";
    }

    @SneakyThrows
    @GetMapping("consultant-status")
    public String consultantStatus(Model model) {
        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));
        model.addAttribute("integrationData", screenDataApiInterface.integration());
        model.addAttribute("huntData", screenDataApiInterface.byHunt());
        return "counsel/call/consultant-status";
    }

    @SneakyThrows
    @GetMapping("stat")
    public String stat(Model model) {
        model.addAttribute("data", consultantStatApiInterface.myCallByTimeStat());
        return "counsel/call/stat";
    }

    @GetMapping("custom-input")
    public String customInput(Model model, @ModelAttribute("form") MaindbCustomInfoFormRequest form,
                              @RequestParam(required = false) Integer maindbGroupSeq,
                              @RequestParam(required = false) String customId,
                              @RequestParam(required = false) String phoneNumber) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final List<MaindbGroupSummaryResponse> groups = maindbGroupApiInterface.list(new MaindbGroupSearchRequest());
        model.addAttribute("maindbGroups", groups);

        if (groups.isEmpty())
            return "counsel/call/custom-input";

        final Map<String, String> talkServices = talkReceptionGroupApiInterface.talkServices().stream().collect(Collectors.toMap(SummaryWtalkServiceResponse::getSenderKey, SummaryWtalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", talkServices);

        final GradeListSearchRequest gradeListSearchRequest = new GradeListSearchRequest();
        if (StringUtils.isNotEmpty(phoneNumber))
            gradeListSearchRequest.getGradeNumbers().add(phoneNumber);

        model.addAttribute("phoneNumber", phoneNumber);
        form.setGroupSeq(maindbGroupSeq);

        final Map<String, String> channelTypes = FormUtils.options(MultichannelChannelType.class);
        model.addAttribute("channelTypes", channelTypes);

        MaindbCustomInfoEntity entity = null;

        if (StringUtils.isNotEmpty(customId)) {
            entity = maindbDataApiInterface.get(customId);
            model.addAttribute("entity", entity);
            form.setGroupSeq(entity.getMaindbSysGroupId());

            if (entity.getMultichannelList() != null)
                entity.getMultichannelList().stream().filter(e -> Objects.equals(e.getChannelType(), MultichannelChannelType.PHONE.getCode())).forEach(e -> gradeListSearchRequest.getGradeNumbers().add(e.getChannelData()));

        } else if (StringUtils.isNotEmpty(phoneNumber)) {
            final MaindbDataSearchRequest search = new MaindbDataSearchRequest();
            if (maindbGroupSeq != null) search.setGroupSeq(maindbGroupSeq);
            search.setChannelType(MultichannelChannelType.PHONE);
            search.setChannelData(phoneNumber);
            final List<MaindbCustomInfoEntity> rows = maindbDataApiInterface.getPagination(search).getRows();

            if (rows.size() > 0) {
                entity = rows.get(0);
                model.addAttribute("entity", entity);
                form.setGroupSeq(entity.getMaindbSysGroupId());

                if (entity.getMultichannelList() != null)
                    entity.getMultichannelList().stream().filter(e -> Objects.equals(e.getChannelType(), MultichannelChannelType.PHONE.getCode())).forEach(e -> gradeListSearchRequest.getGradeNumbers().add(e.getChannelData()));

            } else {
                if (maindbGroupSeq == null)
                    maindbGroupSeq = groups.get(0).getSeq();
                form.setGroupSeq(maindbGroupSeq);
            }
        } else {
            if (maindbGroupSeq == null)
                maindbGroupSeq = groups.get(0).getSeq();
            form.setGroupSeq(maindbGroupSeq);
        }

        final MaindbGroupDetailResponse group = maindbGroupApiInterface.get(form.getGroupSeq());
        model.addAttribute("group", group);
        final CommonTypeEntity customDbType = commonTypeApiInterface.get(group.getMaindbType());
        customDbType.setFields(customDbType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplay())).collect(Collectors.toList()));
        model.addAttribute("customDbType", customDbType);

        final Map<String, String> fieldToRelatedField = new HashMap<>();
        for (CommonFieldEntity field : customDbType.getFields()) {
            if (field.getCodes() != null) {
                for (CommonCodeEntity code : field.getCodes()) {
                    if (StringUtils.isNotEmpty(code.getRelatedFieldId())) {
                        fieldToRelatedField.put(field.getFieldId(), code.getRelatedFieldId());
                    }
                }
            }
        }
        model.addAttribute("fieldToRelatedField", fieldToRelatedField);

        if(StringUtils.isNotEmpty(customId) && Objects.nonNull(entity)){
            final Map<String, Object> fieldNameToValueMap = MaindbDataController.createFieldNameToValueMap(entity, customDbType);
            model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);
            String phoneNumberData = "";

            for (MaindbMultichannelInfoEntity channelInfo : entity.getMultichannelList()) {
                if (MultichannelChannelType.PHONE.getCode().equals(channelInfo.getChannelType())) {
                    phoneNumberData = channelInfo.getChannelData();
                    break;
                }
            }

            model.addAttribute("phoneNumberData", phoneNumberData);
        } else if (StringUtils.isNotEmpty(phoneNumber) && Objects.nonNull(entity)) {
            final Map<String, Object> fieldNameToValueMap = MaindbDataController.createFieldNameToValueMap(entity, customDbType);
            model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);
            String phoneNumberData = "";

            for (int i = 0; i < entity.getMultichannelList().size(); i++){
                if(entity.getMultichannelList().get(i).getChannelType().equals(MultichannelChannelType.PHONE.getCode())){
                    phoneNumberData = entity.getMultichannelList().get(i).getChannelData();
                    break;
                }
            }

            model.addAttribute("phoneNumberData", phoneNumberData);
        } else {
            model.addAttribute("phoneNumberData", phoneNumber);
        }

        if (!gradeListSearchRequest.getGradeNumbers().isEmpty()) {
            final List<GradeListEntity> gradeListEntities = gradelistApiInterface.pagination(gradeListSearchRequest).getRows();
            if (gradeListEntities.stream().anyMatch(e -> e.getGrade() != null && e.getGrade().startsWith("VIP")))
                model.addAttribute("vip", Boolean.TRUE);
            if (gradeListEntities.stream().anyMatch(e -> e.getGrade() != null && e.getGrade().startsWith("BLACK")))
                model.addAttribute("blacklist", Boolean.TRUE);
        }

        return "counsel/call/custom-input";
    }

    @GetMapping("counseling-input")
    public String counselingInput(Model model, @ModelAttribute("form") ResultCustomInfoFormRequest form,
                                  @RequestParam(required = false) Integer maindbGroupSeq,
                                  @RequestParam(required = false) String customId,
                                  @RequestParam(required = false) String phoneNumber,
                                  @RequestParam(required = false) Integer maindbResultSeq) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (maindbGroupSeq == null) {
            final List<MaindbGroupSummaryResponse> groups = maindbGroupApiInterface.list(new MaindbGroupSearchRequest());
            if (groups.isEmpty())
                return "counsel/call/counseling-input";

            maindbGroupSeq = groups.get(0).getSeq();
        }

        final MaindbGroupDetailResponse group = maindbGroupApiInterface.get(maindbGroupSeq);
        model.addAttribute("group", group);
        final CommonTypeEntity resultType = commonTypeApiInterface.get(group.getResultType());
        resultType.setFields(resultType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplay())).collect(Collectors.toList()));
        model.addAttribute("resultType", resultType);
        model.addAttribute("maindbResultSeq", maindbResultSeq);

        final Map<String, String> fieldToRelatedField = new HashMap<>();
        for (CommonFieldEntity field : resultType.getFields()) {
            if (field.getCodes() != null) {
                for (CommonCodeEntity code : field.getCodes()) {
                    if (StringUtils.isNotEmpty(code.getRelatedFieldId())) {
                        fieldToRelatedField.put(field.getFieldId(), code.getRelatedFieldId());
                    }
                }
            }
        }
        model.addAttribute("fieldToRelatedField", fieldToRelatedField);

        form.setMaindbType(group.getMaindbType());
        form.setResultType(group.getResultType());
        form.setGroupId(maindbGroupSeq);
        form.setGroupKind(MultichannelChannelType.PHONE.getCode());

        final List<String> phoneNumbers = new ArrayList<>();
        if (StringUtils.isNotEmpty(phoneNumber))
            phoneNumbers.add(phoneNumber);

        if (StringUtils.isNotEmpty(customId)) {
            try {
                final MaindbCustomInfoEntity custom = maindbDataApiInterface.get(customId);
                if (custom != null && custom.getMultichannelList() != null)
                    custom.getMultichannelList().stream().filter(e -> Objects.equals(e.getChannelType(), MultichannelChannelType.PHONE.getCode())).forEach(e -> phoneNumbers.add(e.getChannelData()));
            } catch (Exception ignored) {
            }
        }

        final TodoListSearchRequest search = new TodoListSearchRequest();
        search.setStartDate(null);
        search.setEndDate(null);
        search.setPhoneNumbers(phoneNumbers);
        search.setUserId(g.getUser().getId());
        search.setStatuses(Arrays.asList(TodoListTodoStatus.ING, TodoListTodoStatus.DELETE, TodoListTodoStatus.HOLD));

        List<TodoList> todoChk = new ArrayList<>();
        if (!phoneNumbers.isEmpty()) {
            todoChk = counselApiInterface.getTodoList(search);
            model.addAttribute("todoList", todoChk);
        }
        if (todoChk != null) {
            for (TodoList todoList : todoChk) {
                if (todoList.getTodoKind().equals(TodoListTodoKind.TRANSFER) && todoList.getTodoStatus().equals(TodoListTodoStatus.ING)) {
                    final List<ResultCustomInfoEntity> entity = maindbResultApiInterface.getTodo(todoList.getUserid(), todoList.getTodoInfo());
                    if (entity.size() > 0) {
                        model.addAttribute("entity", entity.get(0));
                        ReflectionUtils.copy(form, entity.get(0));
                        final LinkedHashMap<String, Object> fieldNameToValueMap = MaindbResultController.createFieldNameToValueMap(entity.get(0), resultType);
                        model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);
                    }
                    break;
                }
            }
        }
        model.addAttribute("todoStatuses", FormUtils.options(TodoListTodoStatus.class));

        final List<VocGroup> vocGroups = vocGroupApiInterface.getArsSmsList("ARS");
        model.addAttribute("vocGroups", vocGroups);

        final List<VocGroup> smsVocGroups = vocGroupApiInterface.getArsSmsList("SMS");
        model.addAttribute("smsVocGroups", smsVocGroups);

        if (maindbResultSeq != null) {
            final ResultCustomInfoEntity entity = maindbResultApiInterface.get(maindbResultSeq);
            model.addAttribute("entity", entity);
            // entity에 있는 필드를 form으로 복사합니다. (이름과 타입이 같을때)..
            // 만약 ReflectionUtils.copy로 복사가 다 되지 않았다면 하나씩 form에 넣어줘야함.
            ReflectionUtils.copy(form, entity);

            final Map<String, Object> fieldNameToValueMap = MaindbResultController.createFieldNameToValueMap(entity, resultType);
            model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);

        }

        return "counsel/call/counseling-input";
    }

    @GetMapping("modal-counseling-reservation")
    public String modalCounselingReservation(Model model) {
        return "counsel/call/modal-counseling-reservation";
    }

    @GetMapping("modal-counseling-transfer")
    public String modalCounselingTransfer(Model model) throws IOException, ResultFailException {
        model.addAttribute("users", userApiInterface.list(new PersonSearchRequest()).stream().collect(Collectors.toMap(PersonSummaryResponse::getId, PersonSummaryResponse::getIdName)));
        return "counsel/call/modal-counseling-transfer";
    }

    @GetMapping("user-call-history")
    public String userCallHistory(Model model) throws IOException, ResultFailException {
        final RecordCallSearchForm search = new RecordCallSearchForm();
        search.setLimit(1000);
        search.setUserId(g.getUser().getId());
        final List<CommonEicnCdrResponse> list = recordingHistoryApiInterface.pagination(search).getRows();
        model.addAttribute("list", list);

        return "counsel/call/user-call-history";
    }

    @GetMapping("user-custom-info")
    public String userCustomInfo(Model model, @RequestParam(required = false) String channelData) throws IOException, ResultFailException {

        final List<CustomMultichannelInfoResponse> list = counselApiInterface.customInfoList(channelData);
        model.addAttribute("list", list);

        return "counsel/call/user-custom-info";
    }
}
