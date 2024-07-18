package kr.co.eicn.ippbx.front.controller.web.admin.stt.transcribe;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.stt.transcribe.TranscribeGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeGroupResponse;
import kr.co.eicn.ippbx.model.form.TranscribeGroupFormRequest;
import kr.co.eicn.ippbx.model.search.TranscribeGroupSearchRequest;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/stt/transcribe/group")
public class TranscribeGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TranscribeGroupController.class);

    private final TranscribeGroupApiInterface apiInterface;
    private final OrganizationService organizationService;
    private final SearchApiInterface searchApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TranscribeGroupSearchRequest search) throws IOException, ResultFailException {
        final Pagination<TranscribeGroupResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<Integer, String> transGroups = apiInterface.transcribeGroup().stream().collect(Collectors.toMap(TranscribeGroupResponse::getSeq, TranscribeGroupResponse::getGroupName));
        model.addAttribute("transGroups", transGroups);
        model.addAttribute("persons", searchApiInterface.persons());

        return "admin/stt/transcribe/group/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") TranscribeGroupFormRequest form) throws IOException, ResultFailException {
        final Map<Integer, String> transcribeGroups = apiInterface.transcribeGroup().stream().collect(Collectors.toMap(TranscribeGroupResponse::getSeq, TranscribeGroupResponse::getGroupName));
        model.addAttribute("transcribeGroups", transcribeGroups);

        final List<TranscribeGroupResponse> transcribeGroupList = apiInterface.transcribeGroup();
        /*
        final Map<Integer, String> transceribeGroups = transcribeGroupList.stream().collect(Collectors.toMap(TranscribeGroupSummaryResponse::getSeq, TranscribeGroupSummaryResponse::getGroupName));
        model.addAttribute("prvGroups", prvGroups);
        */
        model.addAttribute("persons", searchApiInterface.persons());

        return "admin/stt/transcribe/group/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @ModelAttribute("form") TranscribeGroupFormRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final TranscribeGroupResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        model.addAttribute("persons", searchApiInterface.persons());
        ReflectionUtils.copy(form, entity);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(String.valueOf(entity.getSeq())));
/*
        final Map<Integer, String> prvTypes = apiInterface.prvType().stream().collect(Collectors.toMap(CommonTypeResponse::getSeq, CommonTypeResponse::getName));
        model.addAttribute("prvTypes", prvTypes);

        final Map<Integer, String> resultTypes = apiInterface.resultType().stream().collect(Collectors.toMap(CommonTypeResponse::getSeq, CommonTypeResponse::getName));
        model.addAttribute("resultTypes", resultTypes);

        final List<PrvGroupResponse> prvGroupList = apiInterface.prvGroup();
        final Map<Integer, String> prvGroups = prvGroupList.stream().collect(Collectors.toMap(PrvGroupResponse::getSeq, PrvGroupResponse::getName));
        model.addAttribute("prvGroups", prvGroups);

        final Map<String, String> memberKinds = FormUtils.optionsOfCode(PrvMemberKind.class);
        model.addAttribute("memberKinds", memberKinds);

        final Map<String, String> persons = callbackHistoryApiInterface.addPersons().stream().collect(Collectors.toMap(SummaryCallbackDistPersonResponse::getId, SummaryPersonResponse::getIdName));
        model.addAttribute("persons", persons);

        final Map<String, String> members = entity.getMemberDataList().stream().collect(Collectors.toMap(CommonMemberResponse::getId, CommonMemberResponse::getIdName));
        model.addAttribute("members", members);

        for (String id : members.keySet())
            persons.remove(id);
*/
        return "admin/stt/transcribe/group/modal";
    }

    @GetMapping("{seq}/modal-upload")
    public String modalUpload(Model model, @PathVariable Integer seq, @ModelAttribute("form") FileForm form) throws IOException, ResultFailException {
        final TranscribeGroupResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        model.addAttribute("persons", searchApiInterface.persons());

        return "admin/stt/transcribe/group/modal-upload";
    }
}
