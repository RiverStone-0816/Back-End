package kr.co.eicn.ippbx.front.controller.web.messenger;

import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.QueueSummaryAndMemberPeers;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.model.form.LoginForm;
import kr.co.eicn.ippbx.front.model.search.RecordCallSearchForm;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.*;
import kr.co.eicn.ippbx.front.service.api.acd.QueueApiInterface;
import kr.co.eicn.ippbx.front.service.api.dashboard.DashboardApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.history.RecordingHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.model.dto.eicn.DashHuntMonitorResponse;
import kr.co.eicn.ippbx.model.dto.eicn.DashQueueMemberResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryQueueResponse;
import kr.co.eicn.ippbx.model.entity.eicn.OrganizationMetaChatt;
import kr.co.eicn.ippbx.model.enums.CallStatus;
import kr.co.eicn.ippbx.model.enums.ProtectArs;
import kr.co.eicn.ippbx.model.form.ChattingMemberFormRequest;
import kr.co.eicn.ippbx.model.form.MemoMsgFormRequest;
import kr.co.eicn.ippbx.model.search.ChattingMemberSearchRequest;
import kr.co.eicn.ippbx.model.search.MemoMsgSearchRequest;
import kr.co.eicn.ippbx.model.search.RecordCallSearch;
import kr.co.eicn.ippbx.model.search.search.SearchServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("ipcc-messenger")
public class MobileMainController extends MessengerBaseController {
    private static final Logger logger = LoggerFactory.getLogger(MobileMainController.class);

    private final DaemonInfoInterface daemonInfoInterface;
    private final ChattingApiInterface chattingApiInterface;
    private final RecordingHistoryApiInterface recordingHistoryApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final DashboardApiInterface dashboardApiInterface;
    private final SearchApiInterface searchApiInterface;
    private final QueueApiInterface queueApiInterface;
    private final MemoApiInterface memoApiInterface;
    private final UserApiInterface userApiInterface;

    @Value("${eicn.admin.socket.id}")
    private String adminSocketId;

    @GetMapping("favicon.ico")
    public String favicon() {
        return "redirect:/resources/ipcc-messenger/images/favicon.ico";
    }

    @GetMapping("sub-url/**")
    public String page(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }

    @GetMapping("")
    public String loginPage(Model model, @ModelAttribute("form") LoginForm form) throws IOException, ResultFailException {
        if (g.isLogin() && g.checkLogin())
            return "redirect:/ipcc-messenger/main";

        final Map<String, String> socketMap = daemonInfoInterface.getSocketList();

        model.addAttribute("adminSocketUrl", socketMap.get(adminSocketId));

        return "ipcc-messenger/login";
    }

    @SneakyThrows
    @LoginRequired(mainPage = "/ipcc-messenger")
    @GetMapping("main")
    public String mainPage(Model model) {
        val services = searchApiInterface.services(new SearchServiceRequest());
        if (services.isEmpty()) throw new IllegalStateException("서비스가 존재하지 않습니다.");
        model.addAttribute("cids", services);

        model.addAttribute("ProtectArs", FormUtils.optionsOfCode(ProtectArs.class));
        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().filter(e -> e.getStatusNumber() != 9).collect(Collectors.toList()));
        val peerStatuses = dashboardApiInterface.getCustomWaitMonitor().getQueueMemberList();
        model.addAttribute("peerStatuses", peerStatuses);

        val peerInfos = peerStatuses.stream().collect(Collectors.toMap(DashQueueMemberResponse::getPeer, e -> e));

        val peerToUserId = userApiInterface.getPeerToUserIdMap();
        model.addAttribute("peerToUserId", peerToUserId);

        final List<SummaryQueueResponse> queues = queueApiInterface.addQueueNames();
        model.addAttribute("queues", queues.stream().map(e -> {
            try {
                final QueueSummaryAndMemberPeers queue = new QueueSummaryAndMemberPeers(e);
                final DashHuntMonitorResponse huntInfo = dashboardApiInterface.getHuntMonitor(queue.getNumber());
                queue.setWaitingCustomerCount(huntInfo.getCustomWaitCnt());
                queue.setPeers(huntInfo.getQueueMemberList().stream().map(DashQueueMemberResponse::getPeer).collect(Collectors.toSet()));
                return queue;
            } catch (Exception exception) {
                throw new UncheckedIOException(new IOException(exception.getCause()));
            }
        }).collect(Collectors.toList()));

        model.addAttribute("unreadMemoCount", memoApiInterface.getUnreadMessageCount());

        getPeerToIsLoginMap().forEach((peer, isLogin) -> {
            val info = peerInfos.get(peer);
            if (info == null)
                return;

            info.setLogin(isLogin);
        });

        model.addAttribute("services", searchApiInterface.services(new SearchServiceRequest()).stream().collect(Collectors.toMap(ServiceList::getSvcNumber, ServiceList::getSvcName)));

        return "ipcc-messenger/main";
    }

    @GetMapping("modal-calling")
    public String modalCalling(Model model) {
        return "ipcc-messenger/modal-calling";
    }

    private Map<String, Boolean> getPeerToIsLoginMap() {
        final Map<String, Boolean> map = new HashMap<>();
        val organizationMetaChatt = chattingApiInterface.getOrganizationMetaChatt(new ChattingMemberSearchRequest());

        for (OrganizationMetaChatt e : organizationMetaChatt)
            map.putAll(getPeerToIsLoginMap(e));

        return map;
    }

    private Map<String, Boolean> getPeerToIsLoginMap(OrganizationMetaChatt organizationMetaChatt) {
        final Map<String, Boolean> map = new HashMap<>();

        for (val person : organizationMetaChatt.getPersonList())
            if (StringUtils.isNotEmpty(person.getPeer()))
                map.put(person.getPeer(), Objects.equals(person.getIsLoginChatt(), "Y"));

        if (organizationMetaChatt.getOrganizationMetaChatt() != null)
            for (OrganizationMetaChatt e : organizationMetaChatt.getOrganizationMetaChatt())
                map.putAll(getPeerToIsLoginMap(e));

        return map;
    }

    @SneakyThrows
    @LoginRequired(type = LoginRequired.Type.POPUP)
    @GetMapping("modal-messenger-bookmark")
    public String modalMessengerBookmark(Model model, @ModelAttribute("form") ChattingMemberFormRequest form) {
        val addOnPersons = new HashMap<>();
        chattingApiInterface.getOrganizationMetaBookmark().forEach(e -> addOnPersons.put(e.getId(), e.getIdName()));
        model.addAttribute("addOnPersons", addOnPersons);

        addOnPersons.remove(g.getUser().getId());

        val memberList = chattingApiInterface.getBookmarkMembers(new ChattingMemberSearchRequest());
        model.addAttribute("memberList", memberList);

        memberList.removeIf(e -> Objects.equals(e.getId(), g.getUser().getId()));
        memberList.stream().map(PersonSummaryResponse::getId).forEach(addOnPersons::remove);

        return "ipcc-messenger/modal-messenger-bookmark";
    }

    @LoginRequired(mainPage = "/ipcc-messenger", type = LoginRequired.Type.POPUP)
    @GetMapping("modal-room/{id}")
    public String modalRoom(Model model, @PathVariable String id) {
        model.addAttribute("id", id);
        return "ipcc-messenger/modal-room";
    }

    @LoginRequired(mainPage = "/ipcc-messenger", type = LoginRequired.Type.POPUP)
    @GetMapping("modal-invitation")
    public String modalInvitation(Model model) {
        return "ipcc-messenger/modal-invitation";
    }

    @LoginRequired(mainPage = "/ipcc-messenger", type = LoginRequired.Type.POPUP)
    @GetMapping("modal-memo/{seq}")
    public String modalMemo(Model model, @PathVariable Integer seq, @ModelAttribute("form") MemoMsgFormRequest form) {
        model.addAttribute("entity", memoApiInterface.getMemoMessage(seq));

        return "ipcc-messenger/modal-memo";
    }

    @LoginRequired(mainPage = "/ipcc-messenger", type = LoginRequired.Type.POPUP)
    @GetMapping("modal-send-memo")
    public String modalSendMemo(Model model, @RequestParam List<String> members, @ModelAttribute("form") MemoMsgFormRequest form) {
        model.addAttribute("members", members);
        return "ipcc-messenger/modal-send-memo";
    }

    @LoginRequired(mainPage = "/ipcc-messenger", type = LoginRequired.Type.POPUP)
    @GetMapping("user-modal-send-memo")
    public String userModalSendMemo(Model model, @ModelAttribute("form") MemoMsgFormRequest form) {
        model.addAttribute("members", "");
        return "ipcc-messenger/modal-send-memo";
    }

    @SneakyThrows
    @LoginRequired(mainPage = "/ipcc-messenger")
    @GetMapping("tab-call-history")
    public String callHistory(Model model, @ModelAttribute("search") RecordCallSearchForm search) {
        model.addAttribute("pagination", recordingHistoryApiInterface.pagination(search));
        model.addAttribute("callTypes", FormUtils.optionsOfCode(RecordCallSearch.SearchCallType.class));
        model.addAttribute("callStatuses", FormUtils.optionsOfCode(CallStatus.normal_clear, CallStatus.no_answer, CallStatus.user_busy, CallStatus.fail, CallStatus.local_forward));

        return "ipcc-messenger/call-history";
    }

    @SneakyThrows
    @LoginRequired(mainPage = "/ipcc-messenger")
    @GetMapping("tab-received-memo")
    public String receivedMessage(Model model, @ModelAttribute("search") MemoMsgSearchRequest search) {
        model.addAttribute("pagination", memoApiInterface.getReceiveMemoList(search));

        return "ipcc-messenger/received-memo";
    }

    @SneakyThrows
    @LoginRequired(mainPage = "/ipcc-messenger")
    @GetMapping("tab-sent-memo")
    public String sendMessage(Model model, @ModelAttribute("search") MemoMsgSearchRequest search) {
        model.addAttribute("pagination", memoApiInterface.getSendMemoList(search));

        return "ipcc-messenger/sent-memo";
    }

    @SneakyThrows
    @LoginRequired(mainPage = "/ipcc-messenger")
    @GetMapping("tab-profile")
    public String profile(Model model) {
        return "ipcc-messenger/profile";
    }


    @SneakyThrows
    @LoginRequired(mainPage = "/ipcc-messenger")
    @GetMapping("tab-navigation")
    public String navigation(Model model) {
        return "ipcc-messenger/navigation";
    }

    @LoginRequired(mainPage = "/ipcc-messenger", type = LoginRequired.Type.POPUP)
    @GetMapping("modal-profile-picture")
    public String modalProfilePicture(Model model, @ModelAttribute("form") FileForm form) {
        return "ipcc-messenger/modal-profile-picture";
    }
}
