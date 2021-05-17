package kr.co.eicn.ippbx.front.controller.api.user.tel;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.user.tel.RandomRidApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.RandomCidResponse;
import kr.co.eicn.ippbx.server.model.form.RandomCidFormRequest;
import kr.co.eicn.ippbx.server.model.search.RandomCidSearchRequest;
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
@RequestMapping(value = "api/random-rid", produces = MediaType.APPLICATION_JSON_VALUE)
public class RandomRidApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(RandomRidApiController.class);

    @Autowired
    private RandomRidApiInterface apiInterface;

    @GetMapping("")
    public Pagination<RandomCidResponse> pagination(RandomCidSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{seq}")
    public RandomCidResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public void register(@Valid @RequestBody RandomCidFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.register(form);
    }

    @PutMapping("{seq}")
    public void update(@Valid @RequestBody RandomCidFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.update(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }
}
