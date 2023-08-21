package kr.co.eicn.ippbx.front.controller.web.counsel;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbDataController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.CounselApiInterface;
import kr.co.eicn.ippbx.front.service.api.WebchatConfigApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.grade.GradelistApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.WtalkTemplateApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.group.WtalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.GradeListEntity;
import kr.co.eicn.ippbx.model.enums.MultichannelChannelType;
import kr.co.eicn.ippbx.model.enums.TalkTemplate;
import kr.co.eicn.ippbx.model.form.MaindbCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.form.TalkCurrentListSearchRequest;
import kr.co.eicn.ippbx.model.search.GradeListSearchRequest;
import kr.co.eicn.ippbx.model.search.MaindbDataSearchRequest;
import kr.co.eicn.ippbx.model.search.MaindbGroupSearchRequest;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Slf4j
@LoginRequired
@Controller
@RequestMapping("counsel/wtalk")
public class CounseWTalkController extends BaseController {
    @Autowired
    private WtalkTemplateApiInterface talkTemplateApiInterface;
    @Autowired
    private CounselApiInterface counselApiInterface;
    @Autowired
    private GradelistApiInterface gradelistApiInterface;
    @Autowired
    private MaindbGroupApiInterface maindbGroupApiInterface;
    @Autowired
    private MaindbDataApiInterface maindbDataApiInterface;
    @Autowired
    private CommonTypeApiInterface commonTypeApiInterface;
    @Autowired
    private WtalkReceptionGroupApiInterface talkReceptionGroupApiInterface;
    @Autowired
    private WebchatConfigApiInterface webchatConfigApiInterface;

    @Value("${eicn.wtalk.socket.id}")
    private String wtalkSocketId;

    @GetMapping("")
    public String talkPanel(Model model) {
        model.addAttribute("talkSocketUrl", g.getSocketList().get(wtalkSocketId));
        return "counsel/wtalk/panel";
    }

    @GetMapping("upload-file")
    public String uploadFile(Model model, @RequestParam String roomId, @RequestParam String channelType, @RequestParam String senderKey, @RequestParam String userKey) throws IOException, ResultFailException {
        model.addAttribute("roomId", roomId);
        model.addAttribute("channelType", channelType);
        model.addAttribute("senderKey", senderKey);
        model.addAttribute("userKey", userKey);
        model.addAttribute("socketUrl", g.getSocketList().get(wtalkSocketId));
        return "counsel/wtalk/fileupload";
    }

    @GetMapping("modal-template")
    public String modalTemplate(Model model, @ModelAttribute("search") TemplateSearchRequest search) throws IOException, ResultFailException {
        final List<WtalkTemplateSummaryResponse> talkTemplates = talkTemplateApiInterface.list(search);
        final Pagination<WtalkTemplateSummaryResponse> pagination = talkTemplateApiInterface.getPagination(search);
        model.addAttribute("pagination", pagination);

        final Map<String, String> templateTypes = FormUtils.optionsOfCode(TalkTemplate.class);
        model.addAttribute("templateTypes", templateTypes);

        List<WtalkTemplateSummaryResponse> candidates = new ArrayList<>();

        for (WtalkTemplateSummaryResponse e : talkTemplates) {
            if (Objects.equals(e.getType(), TalkTemplate.COMPANY.getCode()))
                candidates.add(e);


            // TODO: api 수정 후, 코드 변경해야 한다. api에서 group의 code가 전달되어야 한다.


            if (Objects.equals(e.getType(), TalkTemplate.GROUP.getCode()) && e.getTypeGroup().contains(g.getUser().getGroupTreeName().substring(g.getUser().getGroupTreeName().length() - 4)))
                candidates.add(e);

            // TODO: api 수정 후, 코드 변경해야 한다. api에서 person의 id가 전달되어야 한다.
            if (Objects.equals(e.getType(), TalkTemplate.PERSON.getCode()) && Objects.equals(g.getUser().getId(), e.getTypeData()))
                candidates.add(e);
        }


        model.addAttribute("talkTemplates", candidates);

        return "counsel/wtalk/modal-template";
    }

    @GetMapping(value = "list-container", params = "mode")
    public String talkListContainer(Model model, @ModelAttribute("search") TalkCurrentListSearchRequest search, @RequestParam(required = false, defaultValue = "false") Boolean showNoti) throws IOException, ResultFailException {
        final List<WtalkCurrentListResponse> talkList = counselApiInterface.currentTalkList(search);
        model.addAttribute("talkList", talkList);
        model.addAttribute("showNoti", showNoti);

        return "counsel/wtalk/list-container";
    }

    @GetMapping("room/{roomId}")
    public String room(Model model, @PathVariable String roomId) throws IOException, ResultFailException {
        final WtalkCurrentMsgResponse talk = counselApiInterface.currentTalkMsg(roomId);
        model.addAttribute("talk", talk);

        return "counsel/wtalk/room";
    }

    @GetMapping("room/empty")
    public String emptyRoom(Model model) {
        final WtalkCurrentMsgResponse talk = new WtalkCurrentMsgResponse();
        model.addAttribute("talk", talk);

        return "counsel/wtalk/room";
    }

    @GetMapping("custom-input")
    public String customInput(Model model, @ModelAttribute("form") MaindbCustomInfoFormRequest form,
                              @RequestParam(required = false) Integer maindbGroupSeq,
                              @RequestParam(required = false) String customId,
                              @RequestParam(required = false) String roomId,
                              @RequestParam(required = false) String senderKey,
                              @RequestParam(required = false) String userKey,
                              @RequestParam(required = false) String channel) throws IOException, ResultFailException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final List<MaindbGroupSummaryResponse> groups = maindbGroupApiInterface.list(new MaindbGroupSearchRequest());
        model.addAttribute("maindbGroups", groups);
        if (groups.isEmpty())
            return "counsel/wtalk/custom-input";

        final Map<String, String> chatServiceMap = webchatConfigApiInterface.list().stream().collect(Collectors.toMap(WebchatServiceSummaryInfoResponse::getSenderKey, WebchatServiceSummaryInfoResponse::getChannelName));
        final Map<String, String> talkServices = talkReceptionGroupApiInterface.talkServices().stream().collect(Collectors.toMap(SummaryWtalkServiceResponse::getSenderKey, SummaryWtalkServiceResponse::getKakaoServiceName));
        talkServices.putAll(chatServiceMap);
        model.addAttribute("talkServices", new MapToLinkedHashMap().toLinkedHashMapByValue(talkServices));
        if (talkServices.isEmpty())
            return "counsel/wtalk/custom-input";

        if (StringUtils.isNotEmpty(roomId)) {
            final TalkCurrentListSearchRequest talkCurrentListSearchRequest = new TalkCurrentListSearchRequest();
            talkCurrentListSearchRequest.setRoomId(roomId);
            final List<WtalkCurrentListResponse> talkCurrentListResponses = counselApiInterface.currentTalkList(talkCurrentListSearchRequest);
            if (talkCurrentListResponses.size() > 0) {
                final WtalkCurrentListResponse talk = talkCurrentListResponses.get(0);

                senderKey = talk.getSenderKey();
                userKey = talk.getUserKey();

                if (StringUtils.isNotEmpty(talk.getMaindbCustomId())) {
                    maindbGroupSeq = talk.getMaindbGroupId();
                    //customId = talk.getMaindbCustomId();
                }
            }
        }

        if (maindbGroupSeq == null)
            maindbGroupSeq = groups.get(0).getSeq();

        model.addAttribute("roomId", roomId);
        model.addAttribute("senderKey", senderKey);
        model.addAttribute("userKey", userKey);

        final GradeListSearchRequest gradeListSearchRequest = new GradeListSearchRequest();

        form.setGroupSeq(maindbGroupSeq);
        form.setRoomId(roomId);

        final MaindbGroupDetailResponse group = maindbGroupApiInterface.get(maindbGroupSeq);
        model.addAttribute("group", group);
        final CommonTypeEntity customDbType = commonTypeApiInterface.get(group.getMaindbType());
        model.addAttribute("customDbType", customDbType);
        final CommonTypeEntity resultType = commonTypeApiInterface.get(group.getResultType());
        model.addAttribute("resultType", resultType);
        final Map<String, String> channelTypes = FormUtils.options(MultichannelChannelType.class);
        model.addAttribute("channelTypes", channelTypes);

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

        if (StringUtils.isNotEmpty(customId)) {
            final MaindbCustomInfoEntity entity = maindbDataApiInterface.get(customId);
            model.addAttribute("entity", entity);

            if (entity.getMultichannelList() != null)
                entity.getMultichannelList().stream().filter(e -> Objects.equals(e.getChannelType(), "PHONE")).forEach(e -> gradeListSearchRequest.getGradeNumbers().add(e.getChannelData()));

            final Map<String, Object> fieldNameToValueMap = MaindbDataController.createFieldNameToValueMap(entity, customDbType);
            model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);
        } else if (StringUtils.isNotEmpty(senderKey) && StringUtils.isNotEmpty(userKey)) {
            final MaindbDataSearchRequest search = new MaindbDataSearchRequest();
            search.setChannelType(MultichannelChannelType.TALK);
            search.setChannelData(senderKey + "_" + userKey); // TODO: channelType 어쩔?
            final List<MaindbCustomInfoEntity> rows = maindbDataApiInterface.getPagination(search).getRows();

            if (rows.size() > 0) {
                final MaindbCustomInfoEntity entity = rows.get(0);
                model.addAttribute("entity", entity);

                if (entity.getMultichannelList() != null)
                    entity.getMultichannelList().stream().filter(e -> Objects.equals(e.getChannelType(), "PHONE")).forEach(e -> gradeListSearchRequest.getGradeNumbers().add(e.getChannelData()));

                final Map<String, Object> fieldNameToValueMap = MaindbDataController.createFieldNameToValueMap(entity, customDbType);
                model.addAttribute("fieldNameToValueMap", fieldNameToValueMap);
            }
        }

        if (!gradeListSearchRequest.getGradeNumbers().isEmpty()) {
            final List<GradeListEntity> gradeListEntities = gradelistApiInterface.paginationCounsel(gradeListSearchRequest).getRows();
            if (gradeListEntities.stream().anyMatch(e -> e.getGrade() != null && e.getGrade().startsWith("VIP")))
                model.addAttribute("vip", Boolean.TRUE);
            if (gradeListEntities.stream().anyMatch(e -> e.getGrade() != null && e.getGrade().startsWith("BLACK")))
                model.addAttribute("blacklist", Boolean.TRUE);
        }

        model.addAttribute("channel", channel);

        return "counsel/wtalk/custom-input";
    }

    @GetMapping("modal-webrtc")
    public String modalWebrtc() { return "counsel/wtalk/modal-webrtc"; }

    @GetMapping("counseling-input")
    public String counselingInput(Model model, @ModelAttribute("form") ResultCustomInfoFormRequest form,
                                  @RequestParam(required = false) Integer maindbGroupSeq,
                                  @RequestParam(required = false) String customId,
                                  @RequestParam(required = false) String roomId,
                                  @RequestParam(required = false) String senderKey,
                                  @RequestParam(required = false) String userKey) throws IOException, ResultFailException {
        if (maindbGroupSeq == null) {
            final List<MaindbGroupSummaryResponse> groups = maindbGroupApiInterface.list(new MaindbGroupSearchRequest());
            if (groups.isEmpty())
                return "counsel/wtalk/counseling-input";

            maindbGroupSeq = groups.get(0).getSeq();
        }

        model.addAttribute("roomId", roomId);
        model.addAttribute("senderKey", senderKey);
        model.addAttribute("userKey", userKey);

        final MaindbGroupDetailResponse group = maindbGroupApiInterface.get(maindbGroupSeq);
        model.addAttribute("group", group);
        final CommonTypeEntity resultType = commonTypeApiInterface.get(group.getResultType());
        model.addAttribute("resultType", resultType);

        form.setMaindbType(group.getMaindbType());
        form.setResultType(group.getResultType());
        form.setGroupId(maindbGroupSeq);
        form.setCustomId(customId);
        form.setGroupKind(MultichannelChannelType.TALK.getCode());
        form.setClickKey(senderKey); // TODO: channelType 어쩔?
        form.setCustomNumber(userKey);
        form.setHangupMsg(roomId);

        if (StringUtils.isNotEmpty(roomId)) {
            final TalkCurrentListSearchRequest talkCurrentListSearchRequest = new TalkCurrentListSearchRequest();
            talkCurrentListSearchRequest.setRoomId(roomId);
            final List<WtalkCurrentListResponse> talkCurrentListResponses = counselApiInterface.currentTalkList(talkCurrentListSearchRequest);

            if (talkCurrentListResponses.size() > 0) {
                final WtalkCurrentListResponse talk = talkCurrentListResponses.get(0);
                model.addAttribute("talk", talk);
                form.setRoomName(talk.getRoomName());
            }
        }

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

        return "counsel/wtalk/counseling-input";
    }
}
