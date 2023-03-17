package kr.co.eicn.ippbx.front.controller.web.admin.wtalk.group;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.wtalk.group.WtalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkGroupPersonResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMemberGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMemberGroupSummaryResponse;
import kr.co.eicn.ippbx.model.enums.TalkMemberDistributionType;
import kr.co.eicn.ippbx.model.form.TalkMemberGroupFormRequest;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
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
@RequestMapping("admin/wtalk/group/reception-group")
public class WtalkReceptionGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkReceptionGroupController.class);

    @Autowired
    private WtalkReceptionGroupApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<WtalkMemberGroupSummaryResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/wtalk/group/reception-group/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") TalkMemberGroupFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> addOnPersons = new HashMap<>();
        apiInterface.addOnPersons().forEach(e -> addOnPersons.put(e.getId(), e.getIdName()));
        model.addAttribute("addOnPersons", new MapToLinkedHashMap().toLinkedHashMapByValue(addOnPersons));
        final Map<String, String> talkServices = apiInterface.talkServices().stream().collect(Collectors.toMap(SummaryWtalkServiceResponse::getSenderKey, SummaryWtalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", new MapToLinkedHashMap().toLinkedHashMapByValue(talkServices));
        model.addAttribute("distributionType", FormUtils.options(TalkMemberDistributionType.class));

        return "admin/wtalk/group/reception-group/modal";
    }

    @GetMapping("{groupId}/modal")
    public String modal(Model model, @PathVariable Integer groupId, @ModelAttribute("form") TalkMemberGroupFormRequest form) throws IOException, ResultFailException {
        final WtalkMemberGroupDetailResponse entity = apiInterface.get(groupId);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        final Map<String, String> addOnPersons = new HashMap<>();
        apiInterface.addOnPersons(entity.getGroupId()).forEach(e -> addOnPersons.put(e.getId(), e.getIdName()));
        model.addAttribute("addOnPersons", new MapToLinkedHashMap().toLinkedHashMapByValue(addOnPersons));
        model.addAttribute("distributionType", FormUtils.options(TalkMemberDistributionType.class));

        for (SummaryWtalkGroupPersonResponse person : entity.getPersons())
            addOnPersons.remove(person.getId());

        return "admin/wtalk/group/reception-group/modal";
    }
}
