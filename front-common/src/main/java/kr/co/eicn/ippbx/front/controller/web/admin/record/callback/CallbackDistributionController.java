package kr.co.eicn.ippbx.front.controller.web.admin.record.callback;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.callback.CallbackDistributionApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.CallbackHuntDistFormRequest;
import kr.co.eicn.ippbx.model.form.CallbackUserDistFormRequest;
import kr.co.eicn.ippbx.model.search.RecordEncSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.jooq.tools.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
@RequestMapping("admin/record/callback/distribution")
public class CallbackDistributionController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CallbackDistributionController.class);

    @Autowired
    private CallbackDistributionApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") RecordEncSearchRequest search) throws IOException, ResultFailException {
        final List<CallbackDistListResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/record/callback/distribution/ground";
    }

    @GetMapping("modal-hunt-distribution")
    public String modalHuntDistribution(Model model, @ModelAttribute("form") CallbackHuntDistFormRequest form) throws IOException, ResultFailException {
        final List<CallbackDistListResponse> serviceList = apiInterface.list();
        final List<SummaryQueueResponse> huntList = apiInterface.addHunts();

        final Map<String, String> services = new HashMap<>();
        serviceList.forEach(e -> services.put(e.getSvcNumber(), e.getSvcName()));
        model.addAttribute("services", services);

        final JSONObject addableHunts = new JSONObject(
                serviceList.stream().collect(Collectors.toMap(CallbackDistListResponse::getSvcNumber, service ->
                                huntList.stream().
                                        filter(addHunt -> addHunt.getSvcNumber().equals(service.getSvcNumber()) &&
                                                service.getHunts().stream().
                                                        filter(addedHunt -> addedHunt.getQueueNumber().equals(addHunt.getNumber())).
                                                        findFirst().orElse(null) == null
                                        ).
                                        collect(Collectors.toMap(SummaryQueueResponse::getNumber, SummaryQueueResponse::getHanName))
                        )
                ));

        final JSONObject addedHunts = new JSONObject(serviceList.stream().collect(Collectors.toMap(
                CallbackDistListResponse::getSvcNumber, service -> service.getHunts().stream().collect(Collectors.toMap(
                        CallbackHuntListResponse::getQueueNumber, CallbackHuntListResponse::getHanName)
                )
        )));

        model.addAttribute("addableHunts", addableHunts);
        model.addAttribute("addedHunts", addedHunts);
        return "admin/record/callback/distribution/modal-hunt-distribution";
    }

    @GetMapping("modal-user-distribution")
    public String modalUserDistribution(Model model, @ModelAttribute("form") CallbackUserDistFormRequest form, @RequestParam String huntNumber, @RequestParam String svcNumber) throws IOException, ResultFailException {
        model.addAttribute("svcNumber", svcNumber);
        model.addAttribute("huntNumber", huntNumber);

        final Map<String, String> addPersons = apiInterface.addPersons().stream().collect(Collectors.toMap(SummaryCallbackDistPersonResponse::getId, SummaryPersonResponse::getIdName));

        apiInterface.list().stream().filter(e -> e.getSvcNumber().equals(svcNumber))
                .map(CallbackDistListResponse::getHunts).findFirst()
                .flatMap(e -> e.stream().filter(h -> h.getQueueNumber().equals(huntNumber)).findFirst()).ifPresent(h -> {
                    final List<CallbackPersonResponse> userList = h.getIdNames().stream().filter(n -> StringUtils.isNotEmpty(n.getUserid())).collect(Collectors.toList());
                    model.addAttribute("addedPersonList", userList);

                    for (CallbackPersonResponse idName : userList) {
                        addPersons.remove(idName.getUserid());
                    }
                }
        );

        model.addAttribute("addPersons", addPersons);

        return "admin/record/callback/distribution/modal-user-distribution";
    }
}
