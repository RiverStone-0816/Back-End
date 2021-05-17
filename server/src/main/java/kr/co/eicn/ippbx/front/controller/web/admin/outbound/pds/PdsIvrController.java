package kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsIvrApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.ScheduleGroupApiInterface;
import kr.co.eicn.ippbx.front.util.FormUtils;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PdsIvr;
import kr.co.eicn.ippbx.server.model.dto.eicn.PDSIvrDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PDSIvrResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryPDSQueueNameResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummarySoundListResponse;
import kr.co.eicn.ippbx.server.model.enums.IvrTreeType;
import kr.co.eicn.ippbx.server.model.enums.IvrTreeTypeGroup;
import kr.co.eicn.ippbx.server.model.form.PDSIvrFormRequest;
import kr.co.eicn.ippbx.server.model.form.PDSIvrFormUpdateRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/pds/ivr")
public class PdsIvrController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsIvrController.class);

    private final PdsIvrApiInterface apiInterface;
    private final ScheduleGroupApiInterface scheduleGroupApiInterface;

    @GetMapping("")
    public String page(Model model, @RequestParam(required = false) Integer seq) throws IOException, ResultFailException {
        model.addAttribute("seq", seq);

        final List<PDSIvrResponse> rootNodes = apiInterface.list();
        model.addAttribute("rootNodes", rootNodes);

        final List<PDSIvrResponse> list = seq != null ? rootNodes.stream().filter(e -> Objects.equals(e.getSeq(), seq)).collect(Collectors.toList()) : rootNodes;
        model.addAttribute("list", list);

        model.addAttribute("form", new PDSIvrFormRequest());

        final Map<Integer, String> sounds = scheduleGroupApiInterface.addSoundList().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", sounds);

        return "admin/outbound/pds/ivr/ground";
    }

    @GetMapping("{seq}/modal-key-map")
    public String modalKeyMap(Model model, @ModelAttribute("form") PDSIvrFormUpdateRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final PDSIvrDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        final Map<String, PdsIvr> buttonMap = entity.getButtonMappingList().stream().collect(Collectors.toMap(PdsIvr::getButton, e -> e));
        model.addAttribute("buttonMap", buttonMap);

        final Map<Integer, String> sounds = scheduleGroupApiInterface.addSoundList().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", sounds);

        final Map<String, String> queues = apiInterface.addPdsQueueNames().stream().collect(Collectors.toMap(SummaryPDSQueueNameResponse::getName, SummaryPDSQueueNameResponse::getHanName));
        model.addAttribute("queues", queues);

        final Map<Byte, String> types = FormUtils.optionsOfCode(IvrTreeTypeGroup.PDS_IVR.getIvrTreeTypes().toArray(new IvrTreeType[]{}));
        model.addAttribute("types", types);

        return "admin/outbound/pds/ivr/modal-key-map";
    }

}
