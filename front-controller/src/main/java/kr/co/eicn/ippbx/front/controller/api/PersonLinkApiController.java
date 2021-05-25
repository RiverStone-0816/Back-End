package kr.co.eicn.ippbx.front.controller.api;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.PersonLinkApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonLink;
import kr.co.eicn.ippbx.model.form.PersonLinkFormRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "api/person-link", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonLinkApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PersonLinkApiController.class);

    private final PersonLinkApiInterface apiInterface;

    @GetMapping("")
    public List<PersonLink> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping("{seq}")
    public PersonLink get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public Integer post(@Valid @RequestBody PersonLinkFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@PathVariable Integer seq, @Valid @RequestBody PersonLinkFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }
}
