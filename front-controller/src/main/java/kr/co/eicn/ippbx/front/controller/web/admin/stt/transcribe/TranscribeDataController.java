package kr.co.eicn.ippbx.front.controller.web.admin.stt.transcribe;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.stt.transcribe.TranscribeDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.stt.transcribe.TranscribeGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeGroupResponse;
import kr.co.eicn.ippbx.model.search.TranscribeDataSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/stt/transcribe/data")
public class TranscribeDataController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TranscribeDataController.class);

    private final TranscribeDataApiInterface apiInterface;
    private final TranscribeGroupApiInterface groupInterface;
    private final OrganizationService organizationService;
    private final SearchApiInterface searchApiInterface;

    @Value("${stt.transcribe.dbHost}")
    private String dbHost;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TranscribeDataSearchRequest search) throws IOException, ResultFailException {
        if (search.getTranscribeGroup() != null) {
            final List<TranscribeGroupResponse> list = apiInterface.list(search);
            model.addAttribute("list", list);
        }

        final Map<Integer, String> transGroups = groupInterface.transcribeGroup().stream().collect(Collectors.toMap(TranscribeGroupResponse::getSeq, TranscribeGroupResponse::getGroupName));
        model.addAttribute("transGroups", transGroups);
        model.addAttribute("persons", searchApiInterface.persons());

        model.addAttribute("dbHost", dbHost);

        return "admin/stt/transcribe/data/ground";
    }

}
