package kr.co.eicn.ippbx.front.controller.api.acd.grade;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.model.form.GradeFileForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.acd.grade.GradelistApiInterface;
import kr.co.eicn.ippbx.server.model.form.GradeListFormRequest;
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
@RequestMapping(value = "api/gradelist", produces = MediaType.APPLICATION_JSON_VALUE)
public class GradelistApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(GradelistApiController.class);

    @Autowired
    private GradelistApiInterface apiInterface;

    @PostMapping("")
    public Integer post(@Valid @RequestBody GradeListFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody GradeListFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    @PostMapping("fields/by-excel")
    public void postFieldsByExcel(@RequestBody GradeFileForm form) throws IOException, ResultFailException {
        apiInterface.postFieldsByExcel(form);
    }
}
