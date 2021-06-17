package kr.co.eicn.ippbx.front.controller.web.admin.monitor.display;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.ScreenDataForByService;
import kr.co.eicn.ippbx.front.model.ScreenDataForIntegration;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.PartMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.display.ScreenConfigApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.display.ScreenDataApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.ScreenConfigExpressionType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CmpMemberStatusCode;
import kr.co.eicn.ippbx.model.entity.eicn.ScreenConfigEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequiredArgsConstructor
@RequestMapping("admin/monitor/screen")
public class ScreenController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ScreenController.class);

    private final ScreenConfigApiInterface apiInterface;
    private final ScreenDataApiInterface dataApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final PartMonitoringApiInterface partMonitoringApiInterface;

    @GetMapping("{seq}")
    public String popup(Model model, @PathVariable Integer seq) throws IOException, ResultFailException {
        final ScreenConfigEntity config = apiInterface.get(seq);
        model.addAttribute("config", config);

        if (ScreenConfigExpressionType.INTEGRATION.equals(config.getExpressionType()))
            return popupIntegration(model);

        if (ScreenConfigExpressionType.BY_HUNT.equals(config.getExpressionType()))
            return popupByHunt(model);

        if (ScreenConfigExpressionType.INTEGRATION_VARIATION.equals(config.getExpressionType()))
            return popupIntegrationValidation(model);

        if (ScreenConfigExpressionType.BY_HUNT_VARIATION.equals(config.getExpressionType()))
            return popupByHuntValidation(model);

        if (ScreenConfigExpressionType.INBOUND_CHART.equals(config.getExpressionType()))
            return popupInboundChart(model);

        if (ScreenConfigExpressionType.LIST_CONSULTANT.equals(config.getExpressionType()))
            return popupListConsultant(model);

        return popupByService(model);
    }

    @SneakyThrows
    private String popupIntegration(Model model) {
        model.addAttribute("data", dataApiInterface.integration());
        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));

        return "admin/monitor/screen/popup-integration";
    }

    @SneakyThrows
    private String popupByHunt(Model model) {
        model.addAttribute("data", dataApiInterface.byHunt());
        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));

        return "admin/monitor/screen/popup-by-hunt";
    }

    @SneakyThrows
    private String popupByService(Model model) {
        final ScreenDataForByService byServiceData = dataApiInterface.byService();
        model.addAttribute("data", byServiceData);
        model.addAttribute("serviceNumberToName", byServiceData.getServices().stream().collect(Collectors.toMap(ScreenDataForIntegration::getServiceNumber, ScreenDataForIntegration::getServiceName)));
        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));

        return "admin/monitor/screen/popup-by-service";
    }

    @SneakyThrows
    private String popupIntegrationValidation(Model model) {
        model.addAttribute("data", dataApiInterface.integration());
        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));

        return "admin/monitor/screen/popup-integration-validation";
    }

    @SneakyThrows
    private String popupByHuntValidation(Model model) {
        model.addAttribute("data", dataApiInterface.integration());
        model.addAttribute("huntData", dataApiInterface.byHunt());
        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));

        return "admin/monitor/screen/popup-by-hunt-validation";
    }

    @SneakyThrows
    private String popupInboundChart(Model model) {
        model.addAttribute("data", dataApiInterface.integration());
        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));

        return "admin/monitor/screen/popup-inbound-chart";
    }

    @SneakyThrows
    private String popupListConsultant(Model model) {
        model.addAttribute("personStatuses", partMonitoringApiInterface.getIndividualStat());
        val statusCodes = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("statusCodes", statusCodes);
        model.addAttribute("statusCodeKeys", new ArrayList<>(statusCodes.keySet()));

        return "admin/monitor/screen/popup-list-consultant";
    }
}
