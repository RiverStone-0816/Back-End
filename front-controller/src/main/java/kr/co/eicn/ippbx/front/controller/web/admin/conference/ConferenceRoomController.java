package kr.co.eicn.ippbx.front.controller.web.admin.conference;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.conference.ConferenceRoomApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.ConfRoomDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ConfRoomSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.model.form.ConfRoomFormRequest;
import kr.co.eicn.ippbx.model.search.ConfRoomSearchRequest;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/conference/room/room")
public class ConferenceRoomController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ConferenceRoomController.class);

    @Autowired
    private ConferenceRoomApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") ConfRoomSearchRequest search) throws IOException, ResultFailException {
        final Pagination<ConfRoomSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/conference/room/room/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") ConfRoomFormRequest form) throws IOException, ResultFailException {
        final List<String> confRoomNumbers = apiInterface.getConfRoomNumber().stream().map(SummaryNumber070Response::getNumber).collect(Collectors.toList());
        model.addAttribute("confRoomNumbers", confRoomNumbers);

        return "admin/conference/room/room/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @ModelAttribute("form") ConfRoomFormRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final ConfRoomDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(entity.getGroupCode()));

        final List<String> confRoomNumbers = apiInterface.getConfRoomNumber().stream().map(SummaryNumber070Response::getNumber).collect(Collectors.toList());
        model.addAttribute("confRoomNumbers", confRoomNumbers);

        if (!confRoomNumbers.contains(entity.getRoomNumber()))
            confRoomNumbers.add(entity.getRoomNumber());

        return "admin/conference/room/room/modal";
    }
}
