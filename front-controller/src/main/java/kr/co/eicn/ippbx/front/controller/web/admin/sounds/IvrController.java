package kr.co.eicn.ippbx.front.controller.web.admin.sounds;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.acd.QueueApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.grade.GradelistApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.IvrApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.OutboundWeekScheduleApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.ScheduleGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.tel.NumberApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.enums.IsWebVoiceYn;
import kr.co.eicn.ippbx.model.enums.IvrMenuType;
import kr.co.eicn.ippbx.model.enums.NumberType;
import kr.co.eicn.ippbx.model.form.IvrFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsDtmfFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsInputFormRequest;
import kr.co.eicn.ippbx.model.search.NumberSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/sounds/ivr")
public class IvrController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(IvrController.class);

    private final IvrApiInterface apiInterface;
    private final ScheduleGroupApiInterface scheduleGroupApiInterface;
    private final GradelistApiInterface gradelistApiInterface;
    private final QueueApiInterface queueApiInterface;
    private final OutboundWeekScheduleApiInterface outboundWeekScheduleApiInterface;
    private final NumberApiInterface numberApiInterface;

    @GetMapping("")
    public String page(Model model, @RequestParam(required = false) Integer seq) throws IOException, ResultFailException {
        final List<IvrResponse> roots = apiInterface.rootIvrTrees();
        final Map<Integer, String> rootMap = roots.stream().collect(Collectors.toMap(IvrTree::getSeq, IvrTree::getName));
        List<IvrMenuType> menuTypes = Arrays.asList(IvrMenuType.values());

        if (model.asMap().get("serviceKind").equals("CC")) {
            menuTypes = menuTypes.stream().filter(e -> ! (e.getCode().equals(IvrMenuType.CONNECT_MENU_AFTER_DONE_EXCEPTION.getCode()) ||
                    e.getCode().equals(IvrMenuType.CONNECT_REPRESENTABLE_NUMBER_AFTER_DONE_EXCEPTION.getCode()) ||
                    e.getCode().equals(IvrMenuType.CONNECT_HUNT_NUMBER_AFTER_DONE_EXCEPTION.getCode()))).collect(Collectors.toList());
        }

        model.addAttribute("roots", rootMap);
        model.addAttribute("menuTypes", menuTypes);
        model.addAttribute("level", apiInterface.list().stream().collect(Collectors.toMap(IvrTree::getSeq, IvrTree::getLevel)));
        model.addAttribute("webVoices", apiInterface.list().stream().collect(Collectors.toMap(IvrTree::getSeq, IvrTree::getIsWebVoice)));

        if (seq == null && roots.size() > 0)
            seq = roots.get(0).getSeq();

        if (seq != null && !rootMap.containsKey(seq))
            seq = roots.size() > 0 ? roots.get(0).getSeq() : null;

        model.addAttribute("seq", seq);

        return "admin/sounds/ivr/ground";
    }

    @GetMapping(value = "new/modal", params = {"type", "posX", "posY"})
    public String modal(Model model, @ModelAttribute("form") IvrFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("typeName", FormUtils.optionsOfCode(IvrMenuType.class).get(form.getType()));

        final Map<Integer, String> sounds = scheduleGroupApiInterface.addSoundList().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", sounds);
        final Map<String, String> serviceNumbers = numberApiInterface.list(new NumberSearchRequest(NumberType.SERVICE.getCode())).stream().collect(Collectors.toMap(NumberSummaryResponse::getNumber, NumberSummaryResponse::getNumber));
        model.addAttribute("serviceNumbers", serviceNumbers);
        final Map<String, String> queues = gradelistApiInterface.queues().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName));
        model.addAttribute("queues", queues);
        final Map<String, String> contexts = queueApiInterface.context().stream().collect(Collectors.toMap(SummaryContextInfoResponse::getContext, SummaryContextInfoResponse::getName));
        model.addAttribute("contexts", contexts);
        final Map<String, String> extensions = outboundWeekScheduleApiInterface.addExtensions().stream().collect(Collectors.toMap(SummaryPhoneInfoResponse::getExtension, e -> defaultString(e.getInUseIdName())));
        model.addAttribute("extensions", extensions);
        model.addAttribute("webVoiceYn", FormUtils.optionsOfCode(IsWebVoiceYn.class));

        return "admin/sounds/ivr/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") IvrFormRequest form) throws IOException, ResultFailException {
        final Byte updatingType = form.getType();

        final IvrResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        model.addAttribute("webVoiceYn", FormUtils.optionsOfCode(IsWebVoiceYn.class));
        ReflectionUtils.copy(form, entity);

        if (form.getType() == null || Objects.equals(form.getType(), (byte) 0))
            form.setType(updatingType);

        return modal(model, form);
    }

    @GetMapping("{ivrCode}/modal-visual-ars")
    public String modalVisualArs(Model model, @PathVariable Integer ivrCode, @ModelAttribute("form") WebVoiceItemsFormRequest form) throws IOException, ResultFailException {
        final Map<Integer, IvrTree> ivrTreeMap = apiInterface.getIvrList().stream().collect(Collectors.toMap(IvrTree::getCode, e -> e));
        model.addAttribute("entity", ivrTreeMap.get(ivrCode));
        form.setIvrCode(ivrCode);

        try {
            final WebVoiceResponse voice = apiInterface.getWebVoice(ivrCode);
            ReflectionUtils.copy(form, voice);
            voice.getItems().sort(Comparator.comparingInt(WebVoiceItemsResponse::getSequence));

            voice.getItems().stream().filter(e -> Objects.equals(e.getItemType(), "HEADER")).findFirst()
                    .ifPresent(webVoiceItemsResponse -> form.setHeaderStr(webVoiceItemsResponse.getItemName()));
            voice.getItems().stream().filter(e -> Objects.equals(e.getItemType(), "TEXT")).findFirst()
                    .ifPresent(webVoiceItemsResponse -> form.setTextStr(webVoiceItemsResponse.getItemName()));

            form.setTitles(
                    voice.getItems().stream().filter(e -> Objects.equals(e.getItemType(), "INPUTDIGIT")).map(e -> {
                        final WebVoiceItemsInputFormRequest inputForm = new WebVoiceItemsInputFormRequest();
                        inputForm.setInputTitle(e.getItemName());
                        inputForm.setMaxLen(e.getInputMaxLen());
                        inputForm.setIsView(e.getIsView());
                        return inputForm;
                    }).collect(Collectors.toList())
            );

            form.setDtmfs(
                    voice.getItems().stream().filter(e -> Objects.equals(e.getItemType(), "DTMF")).map(e -> {
                        final WebVoiceItemsDtmfFormRequest inputForm = new WebVoiceItemsDtmfFormRequest();
                        inputForm.setDtmfTitle(e.getItemName());
                        inputForm.setDtmfValue(e.getItemValue());
                        inputForm.setIsView(e.getIsView());
                        return inputForm;
                    }).collect(Collectors.toList())
            );
        } catch (Exception ignored) {
        }

        return "admin/sounds/ivr/modal-visual-ars";
    }
}
