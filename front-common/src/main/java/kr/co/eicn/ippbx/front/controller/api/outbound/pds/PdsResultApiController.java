package kr.co.eicn.ippbx.front.controller.api.outbound.pds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsResultApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.entity.eicn.ExecutePDSGroupEntity;
import kr.co.eicn.ippbx.model.entity.pds.PDSResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PDSResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.ExecutePDSGroupSearchRequest;
import kr.co.eicn.ippbx.model.search.PDSResultCustomInfoSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author tinywind
 */
@AllArgsConstructor
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/pds-result", produces = MediaType.APPLICATION_JSON_VALUE)
public class PdsResultApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsResultApiController.class);

    private final PdsResultApiInterface apiInterface;

    @GetMapping("excutepds_info")
    public List<ExecutePDSGroupEntity> getExecutingPdsList(ExecutePDSGroupSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.getExecutingPdsList(search);
    }

    @GetMapping("{executeId}/data")
    public Pagination<PDSResultCustomInfoEntity> getPagination(@PathVariable String executeId, PDSResultCustomInfoSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.getPagination(executeId, search);
    }

    @GetMapping("{executeId}/data/{seq}")
    public PDSResultCustomInfoEntity get(@PathVariable String executeId, @PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(executeId, seq);
    }

    @PutMapping("{executeId}/data/{seq}")
    public void put(@Valid @RequestBody PDSResultCustomInfoFormRequest form, BindingResult bindingResult,
                    @PathVariable String executeId, @PathVariable Integer seq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        apiInterface.put(executeId, seq, form);
    }

    @DeleteMapping("{executeId}/data/{seq}")
    public void delete(@PathVariable String executeId, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(executeId, seq);
    }
}
