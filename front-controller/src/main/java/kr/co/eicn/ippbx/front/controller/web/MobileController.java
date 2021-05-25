package kr.co.eicn.ippbx.front.controller.web;

import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.util.spring.RequestMessage;
import kr.co.eicn.ippbx.front.model.ScreenDataForByService;
import kr.co.eicn.ippbx.front.model.form.LoginForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.DaemonInfoInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.PartMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.display.ScreenDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.callback.CallbackHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.api.service.etc.MonitApiInterface;
import kr.co.eicn.ippbx.exception.UnauthorizedException;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CmpMemberStatusCode;
import kr.co.eicn.ippbx.model.dto.eicn.CallbackHistoryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MonitControlResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MonitorQueuePersonStatResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CmpMemberStatusCodeEntity;
import kr.co.eicn.ippbx.model.search.CallbackHistorySearchRequest;
import kr.co.eicn.ippbx.model.search.MonitControlSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("m")
public class MobileController {

    protected final RequestGlobal g;
    protected final RequestMessage message;
    private final ScreenDataApiInterface dataApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final MonitApiInterface monitApiInterface;
    private final DaemonInfoInterface daemonInfoInterface;
    private final CallbackHistoryApiInterface callbackHistoryApiInterface;
    private final PartMonitoringApiInterface partMonitoringApiInterface;

    @Value("${eicn.admin.socket.id}")
    private String adminSocketId;

    @GetMapping("login")
    public String login(Model model, @ModelAttribute("form") LoginForm form) throws IOException, ResultFailException {
        if (g.isLogin())
            return "redirect:/m/login";

        final Map<String, String> socketMap = daemonInfoInterface.getSocketList();

        model.addAttribute("adminSocketUrl", socketMap.get(adminSocketId));

        return "mobile/login";
    }

    @GetMapping("")
    public String main(Model model) throws IOException, ResultFailException {
        if (!g.isLogin())
            return "redirect:/m/login";

        g.setLoginConfirm(true);

        return "mobile/main";
    }

    @GetMapping("operation")
    public String operation(Model model) throws IOException, ResultFailException {
        if (!g.isLogin())
            return "redirect:/m/login";

        model.addAttribute("data", dataApiInterface.integration());
        final List<CmpMemberStatusCodeEntity> statusCodes = companyApiInterface.getMemberStatusCodes();
        statusCodes.sort(Comparator.comparing(CmpMemberStatusCode::getStatusNumber));
        model.addAttribute("statusCodes", statusCodes);

        return "mobile/operation";
    }

    @GetMapping("consultant")
    public String consultant(Model model) throws IOException, ResultFailException {
        if (!g.isLogin())
            return "redirect:/m/login";

        final List<MonitControlResponse> list = monitApiInterface.list(new MonitControlSearchRequest());
        model.addAttribute("list", list);
        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));

        return "mobile/consultant";
    }

    @GetMapping("stat")
    public String hunt(Model model) throws IOException, ResultFailException {
        if (!g.isLogin())
            return "redirect:/m/login";

        final ScreenDataForByService byServiceData = dataApiInterface.byService();
        model.addAttribute("data", byServiceData);

        final List<MonitorQueuePersonStatResponse> list = partMonitoringApiInterface.getIndividualStat();
        model.addAttribute("list", list);

        return "mobile/stat";
    }

    @GetMapping("callback")
    public String callback(Model model, @ModelAttribute("search") CallbackHistorySearchRequest search) throws IOException, ResultFailException {
        if (!g.isLogin())
            return "redirect:/m/login";

        search.setEndDate(null);
        search.setStartDate(null);

        final Pagination<CallbackHistoryResponse> pagination = callbackHistoryApiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "mobile/callback";
    }

    protected String redirectMain() {
        return "redirect:/m/";
    }

    protected String closingPopup() {
        return "closing-popup";
    }

    @ExceptionHandler(UnauthorizedException.class)
    public void unauthorizedException(HttpServletResponse response, UnauthorizedException e, HttpServletRequest request) throws IOException {
        g.invalidateSession();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(
                "<!doctype html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta http-equiv=\"refresh\" content=\"0;url=" + (request.getContextPath() + "/m/") + "\" data-state=\"false\">\n" +
                        "    <title>Redirecting</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "</body>\n" +
                        "</html>"
        );

        g.alertString(StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "허가되지 않은 접근");
    }
}
