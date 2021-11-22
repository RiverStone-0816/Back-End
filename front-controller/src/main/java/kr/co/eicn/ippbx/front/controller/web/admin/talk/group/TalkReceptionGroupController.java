package kr.co.eicn.ippbx.front.controller.web.admin.talk.group;

import kr.co.eicn.ippbx.model.enums.TalkStrategy;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.group.TalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkGroupPersonResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkMemberGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkMemberGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkMemberGroupFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/talk/group/reception-group")
public class TalkReceptionGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkReceptionGroupController.class);

    @Autowired
    private TalkReceptionGroupApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<TalkMemberGroupSummaryResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/talk/group/reception-group/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") TalkMemberGroupFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> addOnPersons = new HashMap<>();
        apiInterface.addOnPersons().forEach(e -> addOnPersons.put(e.getId(), e.getIdName()));
        model.addAttribute("addOnPersons", addOnPersons);
        model.addAttribute("talkStrategy", FormUtils.optionsOfCode(TalkStrategy.class));


        return "admin/talk/group/reception-group/modal";
    }

    @GetMapping("{groupId}/modal")
    public String modal(Model model, @PathVariable Integer groupId, @ModelAttribute("form") TalkMemberGroupFormRequest form) throws IOException, ResultFailException {
        final TalkMemberGroupDetailResponse entity = apiInterface.get(groupId);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        final Map<String, String> addOnPersons = new HashMap<>();
        apiInterface.addOnPersons(entity.getGroupId()).forEach(e -> addOnPersons.put(e.getId(), e.getIdName()));
        model.addAttribute("addOnPersons", addOnPersons);

        for (SummaryTalkGroupPersonResponse person : entity.getPersons())
            addOnPersons.remove(person.getId());

        model.addAttribute("talkStrategy", FormUtils.optionsOfCode(TalkStrategy.class));

        return "admin/talk/group/reception-group/modal";
    }
}
