package kr.co.eicn.ippbx.front.controller.api.service.etc;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.etc.ExtensionExtraInfoApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoResponse;
import kr.co.eicn.ippbx.model.form.CidInfoChangeRequest;
import kr.co.eicn.ippbx.model.form.CidInfoUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.CidInfoSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author tinywind
 */
@LoginRequired
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/extension-extra-info", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExtensionExtraInfoApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionExtraInfoApiController.class);

    @Autowired
    private ExtensionExtraInfoApiInterface apiInterface;

    @GetMapping("")
    public Pagination<PhoneInfoResponse> pagination(CidInfoSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    /**
     * 내선정보수정
     **/
    @PutMapping("")
    public void put(@Valid @RequestBody CidInfoUpdateFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.put(form);
    }

    /**
     * cid 번호수정
     **/
    @PutMapping("/cid")
    public void putCid(@Valid @RequestBody CidInfoChangeRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.putCid(form);
    }
}
