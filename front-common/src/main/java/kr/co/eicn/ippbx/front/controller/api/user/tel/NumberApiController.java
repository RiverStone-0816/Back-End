package kr.co.eicn.ippbx.front.controller.api.user.tel;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.model.form.NumberTypeChangeForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.user.tel.NumberApiInterface;
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
@RequestMapping(value = "api/number", produces = MediaType.APPLICATION_JSON_VALUE)
public class NumberApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(NumberApiController.class);

    @Autowired
    private NumberApiInterface apiInterface;

    /**
     * 번호 타입 변경
     */
    @PatchMapping("{number}/type")
    public void typeChange(@Valid @RequestBody NumberTypeChangeForm form, BindingResult bindingResult,
                           @PathVariable String number) throws IOException, ResultFailException {
        apiInterface.changeType(number, form.getType());
    }
}
