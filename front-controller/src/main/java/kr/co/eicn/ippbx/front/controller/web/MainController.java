package kr.co.eicn.ippbx.front.controller.web;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.LoginForm;
import kr.co.eicn.ippbx.front.service.FileService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ChattingApiInterface;
import kr.co.eicn.ippbx.front.service.api.DaemonInfoInterface;
import kr.co.eicn.ippbx.front.service.api.talk.group.TalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.model.form.ChattingMemberFormRequest;
import kr.co.eicn.ippbx.model.search.ChattingMemberSearchRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author tinywind
 */
@Slf4j
@Controller
public class MainController extends BaseController {
    @Autowired
    private DaemonInfoInterface daemonInfoInterface;
    @Autowired
    private TalkReceptionGroupApiInterface talkReceptionGroupApiInterface;
    @Autowired
    private ChattingApiInterface chattingApiInterface;

    @Value("${eicn.admin.socket.id}")
    private String adminSocketId;
    @Value("${eicn.file.location}")
    private String fileLocation;
    @Value("${eicn.debugging:true}")
    private Boolean devel;
    @Value("${eicn.messenger.socket.id}")
    private String messengerSocketId;

    @GetMapping(FileService.FILE_PATH)
    public void downloadFile(HttpServletResponse response, @RequestParam(FileService.FILE_REQUEST_PARAM_KEY) String fileName) throws IOException {
        fileName = fileName.replaceAll("\\.\\.[/]", "");

        response.setContentType("application/download; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + URLEncoder.encode(fileName, "UTF-8").replaceAll("[+]", "%20") + "\";");
        response.setHeader("Content-Transfer-Encoding", "BINARY");
        IOUtils.copy(new FileInputStream(new File(fileLocation, fileName)), response.getOutputStream());
    }

    @GetMapping("")
    public String loginPage(Model model, @ModelAttribute("form") LoginForm form) throws IOException, ResultFailException {
        if (g.isLogin() && g.checkLogin())
            return "redirect:/main";

        final Map<String, String> socketMap = daemonInfoInterface.getSocketList();

        model.addAttribute("adminSocketUrl", socketMap.get(adminSocketId));

        return "login";
    }

    @LoginRequired
    @GetMapping("main")
    public String mainPage(Model model) {
        return "main";
    }

    @LoginRequired
    @GetMapping("modal-update-password")
    public String modalUpdatePassword(Model model) {
        return "modal-update-password";
    }

    @LoginRequired
    @GetMapping("modal-site-map")
    public String modalSiteMap(Model model) {
        return "admin/modal-site-map";
    }

    @GetMapping("sub-url/**")
    public String page(Model model, HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("favicon.ico")
    public String favicon() {
        return "redirect:/resources/images/favicon.ico";
    }

    @RequestMapping("swagger-ui.html")
    public void swaggerReject(HttpServletResponse httpServletResponse) throws IOException {
        if (!devel) {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    @LoginRequired
    @GetMapping("messenger")
    public String messenger() {
        return "modal-messenger";
    }

    @SneakyThrows
    @LoginRequired
    @GetMapping("modal-messenger-bookmark")
    public String modalMessengerBookmark(Model model, @ModelAttribute("form") ChattingMemberFormRequest form) {
        val addOnPersons = new HashMap<>();
        talkReceptionGroupApiInterface.addOnPersons().forEach(e -> addOnPersons.put(e.getId(), e.getIdName()));
        model.addAttribute("addOnPersons", addOnPersons);

        addOnPersons.remove(g.getUser().getId());

        val memberList = chattingApiInterface.getBookmarkMembers(new ChattingMemberSearchRequest());
        model.addAttribute("memberList", memberList);

        memberList.removeIf(e -> Objects.equals(e.getId(), g.getUser().getId()));
        memberList.stream().map(PersonSummaryResponse::getId).forEach(addOnPersons::remove);

        return "modal-messenger-bookmark";
    }

    @SneakyThrows
    @LoginRequired
    @GetMapping("messenger-upload-file")
    public String uploadFile(Model model, @RequestParam String roomId, @RequestParam String messengerSocketUrl) throws IOException, ResultFailException {
        model.addAttribute("roomId", roomId);
        model.addAttribute("messengerSocketUrl", messengerSocketUrl);
        model.addAttribute("socketUrl", g.getSocketList().get(messengerSocketId));
        return "messenger-fileupload";
    }
}
