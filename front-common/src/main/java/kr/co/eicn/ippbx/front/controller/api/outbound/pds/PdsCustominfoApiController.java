package kr.co.eicn.ippbx.front.controller.api.outbound.pds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsCustominfoApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.entity.pds.PDSCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PDSCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.PDSDataSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author tinywind
 */
@AllArgsConstructor
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/pds-custominfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class PdsCustominfoApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsCustominfoApiController.class);

    private final PdsCustominfoApiInterface apiInterface;

    //리스트
    @GetMapping("{groupSeq}/data")
    public Pagination<PDSCustomInfoEntity> getPagination(@PathVariable Integer groupSeq, PDSDataSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.getPagination(groupSeq, search);
    }

    @GetMapping("{groupSeq}/data/{id}")
    public PDSCustomInfoEntity getPagination(@PathVariable Integer groupSeq, @PathVariable String id) throws IOException, ResultFailException {
        return apiInterface.get(groupSeq, id);
    }

    //고객정보Insert
    @PostMapping("")
    public void post(@Valid @RequestBody PDSCustomInfoFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    //고객정보Update
    @PutMapping("{id}")
    public void put(@Valid @RequestBody PDSCustomInfoFormRequest form, BindingResult bindingResult, @PathVariable String id) throws IOException, ResultFailException {
        apiInterface.put(id, form);
    }

    //삭제
    @DeleteMapping("")
    public void deleteData(@RequestParam Integer groupSeq, @RequestParam String id) throws IOException, ResultFailException {
        apiInterface.deleteData(groupSeq, id);
    }
}
