package kr.co.eicn.ippbx.front.controller.web.admin.stt.learn;

import io.micrometer.core.instrument.util.StringUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.stt.learn.LearnGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.stt.transcribe.TranscribeGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.LearnGroupResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeGroupResponse;
import kr.co.eicn.ippbx.model.form.LearnGroupFormRequest;
import kr.co.eicn.ippbx.model.search.LearnGroupSearchRequest;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/stt/learn/group")
public class LearnGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(LearnGroupController.class);

    private final LearnGroupApiInterface apiInterface;
    private final TranscribeGroupApiInterface transcribeGroupApiInterface;

    @Value("${stt.transcribe.dbHost}")
    private String dbHost;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") LearnGroupSearchRequest search) throws IOException, ResultFailException {
        final Pagination<LearnGroupResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<Integer, String> learnGroups = apiInterface.learnGroup().stream().collect(Collectors.toMap(LearnGroupResponse::getSeq, LearnGroupResponse::getGroupName));
        model.addAttribute("learnGroups", learnGroups);

        model.addAttribute("dbHost", dbHost);

        return "admin/stt/learn/group/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") LearnGroupFormRequest form) throws IOException, ResultFailException {
        logger.info("LearnGroupFormRequest form ===>>> " + form);
        model.addAttribute("groupList", transcribeGroupApiInterface.transcribeGroup());

        return "admin/stt/learn/group/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @ModelAttribute("form") LearnGroupFormRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final LearnGroupResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        if (StringUtils.isNotEmpty(entity.getLearnGroupCode())) {
            List<TranscribeGroupResponse> groupList = new ArrayList<>();
            List<TranscribeGroupResponse> addedGroupList = new ArrayList<>();

            List<String> groupCodeList = Arrays.asList(entity.getLearnGroupCode().split("\\|"));
            transcribeGroupApiInterface.transcribeGroup().forEach(e -> {
                if (groupCodeList.contains(e.getSeq().toString())) {
                    addedGroupList.add(e);
                } else {
                    groupList.add(e);
                }
            });

            model.addAttribute("groupList", groupList);
            model.addAttribute("addedGroupList", addedGroupList);
        }

        return "admin/stt/learn/group/modal";
    }
}
