package kr.co.eicn.ippbx.front.controller.web.admin.outbound.preview;

import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.preview.PreviewGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.callback.CallbackHistoryApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.enums.PrvMemberKind;
import kr.co.eicn.ippbx.model.form.PrvGroupFormRequest;
import kr.co.eicn.ippbx.model.search.PrvGroupSearchRequest;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/preview/group")
public class PreviewGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PreviewGroupController.class);

    private final PreviewGroupApiInterface    apiInterface;
    private final PdsGroupApiInterface        pdsGroupApiInterface;
    private final OrganizationService         organizationService;
    private final CallbackHistoryApiInterface callbackHistoryApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PrvGroupSearchRequest search) throws IOException, ResultFailException {
        final Pagination<PrvGroupSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        model.addAttribute("prvTypes", new MapToLinkedHashMap().toLinkedHashMapByValue(apiInterface.prvType().stream().collect(Collectors.toMap(CommonTypeResponse::getSeq, CommonTypeResponse::getName))));

        return "admin/outbound/preview/group/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") PrvGroupFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("prvTypes", new MapToLinkedHashMap().toLinkedHashMapByValue(apiInterface.prvType().stream().collect(Collectors.toMap(CommonTypeResponse::getSeq, CommonTypeResponse::getName))));
        model.addAttribute("resultTypes", new MapToLinkedHashMap().toLinkedHashMapByValue(apiInterface.resultType().stream().collect(Collectors.toMap(CommonTypeResponse::getSeq, CommonTypeResponse::getName))));
        model.addAttribute("rids", pdsGroupApiInterface.addRidNumberLists());
        model.addAttribute("numbers", new MapToLinkedHashMap().toLinkedHashMapByValue(pdsGroupApiInterface.addNumberLists().stream().collect(Collectors.toMap(SummaryNumber070Response::getNumber, SummaryNumber070Response::getNumber))));
        model.addAttribute("prvGroups", new MapToLinkedHashMap().toLinkedHashMapByValue(apiInterface.prvGroup().stream().collect(Collectors.toMap(PrvGroupResponse::getSeq, PrvGroupResponse::getName))));
        model.addAttribute("memberKinds", new MapToLinkedHashMap().toLinkedHashMapByValue(FormUtils.optionsOfCode(PrvMemberKind.class)));

        if (!model.containsAttribute("persons"))
            model.addAttribute("persons", new MapToLinkedHashMap().toLinkedHashMapByValue(callbackHistoryApiInterface.addPersons().stream().collect(Collectors.toMap(SummaryCallbackDistPersonResponse::getId, SummaryPersonResponse::getIdName))));

        return "admin/outbound/preview/group/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @ModelAttribute("form") PrvGroupFormRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final PrvGroupDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(entity.getGroupCode()));

        final Map<String, String> persons = callbackHistoryApiInterface.addPersons().stream().collect(Collectors.toMap(SummaryCallbackDistPersonResponse::getId, SummaryPersonResponse::getIdName));
        model.addAttribute("persons", new MapToLinkedHashMap().toLinkedHashMapByValue(persons));

        final Map<String, String> members = entity.getMemberDataList().stream().collect(Collectors.toMap(CommonMemberResponse::getId, CommonMemberResponse::getIdName));
        model.addAttribute("members", new MapToLinkedHashMap().toLinkedHashMapByValue(members));

        for (String id : members.keySet())
            persons.remove(id);

        return modal(model, form);
    }

    @GetMapping("{seq}/modal-upload")
    public String modalUpload(Model model, @PathVariable Integer seq, @ModelAttribute("form") FileForm form) throws IOException, ResultFailException {
        final PrvGroupDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);

        return "admin/outbound/preview/group/modal-upload";
    }
}
