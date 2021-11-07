package kr.co.eicn.ippbx.front.controller.web.admin.service.etc;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.etc.ExtensionExtraInfoApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.CidInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoResponse;
import kr.co.eicn.ippbx.model.enums.DialStatus;
import kr.co.eicn.ippbx.model.enums.FirstStatus;
import kr.co.eicn.ippbx.model.enums.LogoutStatus;
import kr.co.eicn.ippbx.model.form.CidInfoChangeRequest;
import kr.co.eicn.ippbx.model.form.CidInfoFormRequest;
import kr.co.eicn.ippbx.model.form.CidInfoUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.CidInfoSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/service/etc/extension-extra-info")
public class ExtensionExtraInfoController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionExtraInfoController.class);

    @Autowired
    private ExtensionExtraInfoApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") CidInfoSearchRequest search) throws IOException, ResultFailException {
        final Pagination<PhoneInfoResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/service/etc/extension-extra-info/ground";
    }

    @GetMapping("modal")
    public String modal(Model model, @ModelAttribute("search") CidInfoSearchRequest search, @ModelAttribute("forms") CidInfoUpdateFormRequest forms) throws IOException, ResultFailException {
        final ArrayList<CidInfoFormRequest> rows = new ArrayList<>();
        forms.setCidInfos(rows);
        final List<PhoneInfoResponse> phoneInfos = apiInterface.pagination(search).getRows();

        final Map<String, String> peerToExtensions = phoneInfos.stream().collect(Collectors.toMap(PhoneInfoResponse::getPeer, PhoneInfoResponse::getExtension));
        model.addAttribute("peerToExtensions", peerToExtensions);

        final List<String> peers = new ArrayList<>();
        for (PhoneInfoResponse row : phoneInfos) {
            final CidInfoFormRequest cidInfoForm = new CidInfoFormRequest();
            rows.add(cidInfoForm);
            ReflectionUtils.copy(cidInfoForm, row);
            cidInfoForm.setFirstStatus(row.getFirstStatus().getCode());
            cidInfoForm.setDialStatus(row.getDialStatus().getCode());
            cidInfoForm.setLogoutStatus(row.getLogoutStatus().getCode());

            peers.add(row.getPeer());
        }
        model.addAttribute("peers", peers);

        final Map<Byte, String> firstStatusTypes = FormUtils.optionsOfCode(FirstStatus.class);
        model.addAttribute("firstStatusTypes", firstStatusTypes);
        final Map<Byte, String> dialStatusTypes = FormUtils.optionsOfCode(DialStatus.class);
        model.addAttribute("dialStatusTypes", dialStatusTypes);
        final Map<Byte, String> logoutStatusTypes = FormUtils.optionsOfCode(LogoutStatus.class);
        model.addAttribute("logoutStatusTypes", logoutStatusTypes);

        final List<String> cidInfos = apiInterface.cidList().stream().map(CidInfoResponse::getCid).collect(Collectors.toList());
        model.addAttribute("cidInfos", cidInfos);

        return "admin/service/etc/extension-extra-info/modal";
    }

    @GetMapping("modal-update-cid")
    public String modalUpdateCid(Model model, @ModelAttribute("form") CidInfoChangeRequest form) throws IOException, ResultFailException {
        final List<String> cidInfos = apiInterface.cidList().stream().map(CidInfoResponse::getCid).collect(Collectors.toList());
        model.addAttribute("cidInfos", cidInfos);

        return "admin/service/etc/extension-extra-info/modal-update-cid";
    }

}
