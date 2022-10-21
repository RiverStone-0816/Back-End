package kr.co.eicn.ippbx.front.controller.api.application.maindb;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbDataApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.ConCodeFieldResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchMaindbGroupResponse;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.MaindbCustomInfoFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/maindb-data", produces = MediaType.APPLICATION_JSON_VALUE)
public class MaindbDataApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MaindbDataApiController.class);

    @Autowired
    private MaindbDataApiInterface apiInterface;

    @GetMapping("customdb_group")
    public List<SearchMaindbGroupResponse> customdb_group() throws IOException, ResultFailException {
        return apiInterface.customdbGroup();
    }

    //검색항목조회.
    @GetMapping("search_item")
    public List<ConCodeFieldResponse> search_item() throws IOException, ResultFailException {
        return apiInterface.searchItem();
    }

    //고객정보Insert
    @PostMapping("")
    public String post(@Valid @RequestBody MaindbCustomInfoFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    //고객정보Update
    @PutMapping("{id}")
    public void put(@Valid @RequestBody MaindbCustomInfoFormRequest form, BindingResult bindingResult, @PathVariable String id) throws IOException, ResultFailException {
        apiInterface.put(id, form);
    }

    //수정 위한 정보 불러오기.
    @GetMapping("{id}")
    public MaindbCustomInfoEntity get(@PathVariable String id) throws IOException, ResultFailException {
        return apiInterface.get(id);
    }

    @GetMapping("{phoneNumber}/name")
    public String getCustomName(@PathVariable String phoneNumber) throws IOException, ResultFailException {
        return apiInterface.getCustomName(phoneNumber);
    }

    @DeleteMapping("{id}")
    public void deleteData(@PathVariable String id) throws IOException, ResultFailException {
        apiInterface.deleteData(id);
    }
}
