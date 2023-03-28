package kr.co.eicn.ippbx.front.controller.web.admin.user.tel;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.tel.PhoneApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.LicenseInfo;
import kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchCidResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchGwInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchNumber070Response;
import kr.co.eicn.ippbx.model.enums.*;
import kr.co.eicn.ippbx.model.form.PhoneInfoFormRequest;
import kr.co.eicn.ippbx.model.search.PhoneSearchRequest;
import kr.co.eicn.ippbx.model.search.search.SearchCidRequest;
import kr.co.eicn.ippbx.model.search.search.SearchGwInfoRequest;
import kr.co.eicn.ippbx.model.search.search.SearchNumber070Request;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/user/tel/extension")
public class ExtensionController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionController.class);

    @Autowired
    private PhoneApiInterface apiInterface;
    @Autowired
    private SearchApiInterface searchApiInterface;
    @Autowired
    private CompanyApiInterface companyApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PhoneSearchRequest search) throws IOException, ResultFailException {
        final Pagination<PhoneInfoSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final LicenseInfo licenseInfo = companyApiInterface.getLicense().getRecordLicense();
        final LicenseInfo sttLicenseInfo = companyApiInterface.getLicense().getSttLicense();
        final LicenseInfo softPhoneLicenseInfo = companyApiInterface.getLicense().getSoftPhoneLicense();
        model.addAttribute("licenseInfo", licenseInfo);
        model.addAttribute("sttLicenseInfo", sttLicenseInfo);
        model.addAttribute("softPhoneLicenseInfo", softPhoneLicenseInfo);

        return "admin/user/tel/extension/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") PhoneInfoFormRequest form) throws IOException, ResultFailException {
        final LicenseInfo licenseInfo = companyApiInterface.getLicense().getRecordLicense();
        final LicenseInfo sttLicenseInfo = companyApiInterface.getLicense().getSttLicense();
        final LicenseInfo softPhoneLicenseInfo = companyApiInterface.getLicense().getSoftPhoneLicense();
        model.addAttribute("licenseInfo", licenseInfo);
        model.addAttribute("sttLicenseInfo", sttLicenseInfo);
        model.addAttribute("softPhoneLicenseInfo", softPhoneLicenseInfo);

        SearchNumber070Request number070Request = new SearchNumber070Request();
        number070Request.setStatus(Number070Status.NON_USE.getCode());
        number070Request.setType(NumberType.PERSONAL.getCode());
        final List<SearchNumber070Response> numbers = searchApiInterface.numbers(number070Request);
        model.addAttribute("numbers", numbers);

        final SearchGwInfoRequest gwInfoRequest = new SearchGwInfoRequest();
        final List<SearchGwInfoResponse> gateways = searchApiInterface.gateways(gwInfoRequest);
        model.addAttribute("gateways", gateways);

        final SearchCidRequest cidRequest = new SearchCidRequest();
        final List<SearchCidResponse> cids = searchApiInterface.cids(cidRequest);
        model.addAttribute("cids", cids.stream().filter(e -> !e.getCidNumber().equals(form.getCid())).sorted(Comparator.comparing(SearchCidResponse::getCidNumber)).collect(Collectors.toList()));

        final Map<String, String> forwardKinds = FormUtils.optionsOfCode(ForwardKind.INNER, ForwardKind.REPRESENTATIVE, ForwardKind.OUTER);
        model.addAttribute("forwardKinds", forwardKinds);
        model.addAttribute("forwardWhen", FormUtils.optionsOfCode(ForwardWhen.class));

        number070Request.setStatus(Number070Status.NON_USE.getCode());
        number070Request.setType(NumberType.SERVICE.getCode());
        final List<SearchNumber070Response> serviceNumbers = searchApiInterface.numbers(number070Request);
        model.addAttribute("serviceNumbers", serviceNumbers);

        SearchNumber070Request number070RequestBO = new SearchNumber070Request();
        final List<SearchNumber070Response> serviceNumbersBO = searchApiInterface.numbers(number070RequestBO);
        model.addAttribute("billingNumbers",serviceNumbersBO.stream().filter(e -> e.getKind().contains("B")).sorted(Comparator.comparing(Number_070::getNumber)).collect(Collectors.toList()));

        final PhoneSearchRequest phoneSearchRequest = new PhoneSearchRequest();
        phoneSearchRequest.setLimit(10000);
        final List<String> extensions = apiInterface.pagination(phoneSearchRequest).getRows().stream().map(PhoneInfoSummaryResponse::getExtension).collect(Collectors.toList());
        model.addAttribute("extensions", extensions);

        return "admin/user/tel/extension/modal";
    }

    @GetMapping("{peer}/modal")
    public String modal(Model model, @PathVariable String peer, @ModelAttribute("form") PhoneInfoFormRequest form) throws IOException, ResultFailException {
        final LicenseInfo licenseInfo = companyApiInterface.getLicense().getRecordLicense();
        final LicenseInfo sttLicenseInfo = companyApiInterface.getLicense().getSttLicense();
        final LicenseInfo softPhoneLicenseInfo = companyApiInterface.getLicense().getSoftPhoneLicense();
        model.addAttribute("licenseInfo", licenseInfo);
        model.addAttribute("sttLicenseInfo", sttLicenseInfo);
        model.addAttribute("softPhoneLicenseInfo", softPhoneLicenseInfo);        final PhoneInfoDetailResponse entity = apiInterface.get(peer);

        ReflectionUtils.copy(form, entity);
        form.setNumber(entity.getNumber070().getNumber());

        model.addAttribute("entity", entity);

        if (StringUtils.isNotEmpty(entity.getForwarding())) {
            final String[] split = entity.getForwarding().split("[|]");
            if (split.length >= 1)
                form.setForwardKind(split[0]);
            if (split.length >= 2)
                form.setForwarding(split[1]);
        }

        return modal(model, form);
    }
}
