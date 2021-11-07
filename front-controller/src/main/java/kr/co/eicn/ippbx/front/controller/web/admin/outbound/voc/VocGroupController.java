package kr.co.eicn.ippbx.front.controller.web.admin.outbound.voc;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.voc.VocGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.callback.CallbackHistoryApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CidInfo;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryCallbackDistPersonResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPersonResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryResearchListResponse;
import kr.co.eicn.ippbx.model.dto.eicn.VOCGroupResponse;
import kr.co.eicn.ippbx.model.enums.*;
import kr.co.eicn.ippbx.model.form.VOCGroupFormRequest;
import kr.co.eicn.ippbx.model.search.VOCGroupSearchRequest;
import kr.co.eicn.ippbx.model.search.search.SearchCidRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/voc/group")
public class VocGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(VocGroupController.class);

    private final VocGroupApiInterface apiInterface;
    private final PdsGroupApiInterface pdsGroupApiInterface;
    private final SearchApiInterface searchApiInterface;
    private final CallbackHistoryApiInterface callbackHistoryApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") VOCGroupSearchRequest search) throws IOException, ResultFailException {
        final Pagination<VOCGroupResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);
        return "admin/outbound/voc/group/ground";
    }

    @GetMapping("new/modal")
    public String modalInfo(Model model, @ModelAttribute("form") VOCGroupFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("researches", pdsGroupApiInterface.addResearchLists().stream().collect(Collectors.toMap(SummaryResearchListResponse::getResearchId, SummaryResearchListResponse::getResearchName)));
        model.addAttribute("processKinds", FormUtils.optionsOfCode(ProcessKind.class));
        model.addAttribute("isArsSmsOptions", FormUtils.optionsOfCode(IsArsSms.class));
        model.addAttribute("vocGroupSenderOptions", FormUtils.optionsOfCode(VocGroupSender.class));
        model.addAttribute("outboundTargetOptions", FormUtils.optionsOfCode(InOutTarget.MEMBER, InOutTarget.CIDNUM, InOutTarget.ALL, InOutTarget.NO));
        model.addAttribute("inboundTargetOptions", FormUtils.optionsOfCode(InOutTarget.MEMBER, InOutTarget.SVCNUM, InOutTarget.ALL, InOutTarget.NO));
        model.addAttribute("callKinds", FormUtils.optionsOfCode(InOutCallKind.class));
        model.addAttribute("cids", searchApiInterface.cids(new SearchCidRequest()).stream().collect(Collectors.toMap(CidInfo::getCidNumber, CidInfo::getCidNumber)));

        final Map<String, String> persons = callbackHistoryApiInterface.addPersons().stream().collect(Collectors.toMap(SummaryCallbackDistPersonResponse::getId, SummaryPersonResponse::getIdName));
        model.addAttribute("persons", persons);
        model.addAttribute("outboundPersons", new HashMap<>(persons));
        model.addAttribute("inboundPersons", new HashMap<>(persons));

        return "admin/outbound/voc/group/modal";
    }

    @GetMapping("{seq}/modal")
    public String modalInfo(Model model, @PathVariable Integer seq, @ModelAttribute("form") VOCGroupFormRequest form) throws IOException, ResultFailException {
        final VOCGroupResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        form.setVocGroupName(entity.getVocGroupName());
        form.setArsResearchId(entity.getArsResearchId());
        form.setOutboundTargetCidnum(entity.getOutboundTargetCidnum());
        form.setInboundTargetSvcnum(entity.getInboundTargetSvcnum());

        model.addAttribute("researches", pdsGroupApiInterface.addResearchLists().stream().collect(Collectors.toMap(SummaryResearchListResponse::getResearchId, SummaryResearchListResponse::getResearchName)));
        model.addAttribute("processKinds", FormUtils.optionsOfCode(ProcessKind.class));
        model.addAttribute("isArsSmsOptions", FormUtils.optionsOfCode(IsArsSms.class));
        model.addAttribute("vocGroupSenderOptions", FormUtils.optionsOfCode(VocGroupSender.class));
        model.addAttribute("outboundTargetOptions", FormUtils.optionsOfCode(InOutTarget.MEMBER, InOutTarget.CIDNUM, InOutTarget.ALL, InOutTarget.NO));
        model.addAttribute("inboundTargetOptions", FormUtils.optionsOfCode(InOutTarget.MEMBER, InOutTarget.SVCNUM, InOutTarget.ALL, InOutTarget.NO));
        model.addAttribute("callKinds", FormUtils.optionsOfCode(InOutCallKind.class));
        model.addAttribute("cids", searchApiInterface.cids(new SearchCidRequest()).stream().collect(Collectors.toMap(CidInfo::getCidNumber, CidInfo::getCidNumber)));

        final Map<String, String> persons = callbackHistoryApiInterface.addPersons().stream().collect(Collectors.toMap(SummaryCallbackDistPersonResponse::getId, SummaryPersonResponse::getIdName));
        model.addAttribute("persons", persons);

        final HashMap<String, String> outboundPersons = new HashMap<>(persons);
        entity.getOutboundMemberList().forEach(outboundPersons::remove);
        final HashMap<String, String> inboundPersons = new HashMap<>(persons);
        entity.getOutboundMemberList().forEach(inboundPersons::remove);

        model.addAttribute("outboundPersons", outboundPersons);
        model.addAttribute("inboundPersons", inboundPersons);

        return "admin/outbound/voc/group/modal";
    }
}
