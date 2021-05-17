package kr.co.eicn.ippbx.front.controller.api.user.tel;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.user.tel.PhoneApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.PhoneInfoSummaryResponse;
import kr.co.eicn.ippbx.server.model.form.PhoneInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.PhoneInfoUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.search.PhoneSearchRequest;
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
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/extension", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExtensionApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionApiController.class);

    @Autowired
    private PhoneApiInterface apiInterface;

    @GetMapping("exists")
    public Boolean existsExtension(@RequestParam String extension) throws IOException, ResultFailException {
        final PhoneSearchRequest search = new PhoneSearchRequest();
        search.setExtension(extension);
        final Pagination<PhoneInfoSummaryResponse> pagination = apiInterface.pagination(search);

        return pagination.getTotalCount() > 0;
    }

    @PostMapping("")
    public void post(@Valid @RequestBody PhoneInfoFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PutMapping("{peer}")
    public void update(@Valid @RequestBody PhoneInfoUpdateFormRequest form, BindingResult bindingResult, @PathVariable String peer) throws IOException, ResultFailException {
        form.setOldPeer(peer);
        apiInterface.update(peer, form);
    }

    @DeleteMapping("{peer}")
    public void delete(@PathVariable String peer) throws IOException, ResultFailException {
        apiInterface.delete(peer);
    }
}
