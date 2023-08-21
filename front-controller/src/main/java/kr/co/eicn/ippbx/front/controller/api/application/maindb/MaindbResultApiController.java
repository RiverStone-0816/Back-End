package kr.co.eicn.ippbx.front.controller.api.application.maindb;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CounselApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbResultApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchMaindbGroupResponse;
import kr.co.eicn.ippbx.model.entity.customdb.ResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.form.TodoListUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.ResultCustomInfoSearchRequest;
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
@RequestMapping(value = "api/maindb-result", produces = MediaType.APPLICATION_JSON_VALUE)
public class MaindbResultApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MaindbResultApiController.class);

    private final MaindbResultApiInterface apiInterface;
    private final CounselApiInterface counselApiInterface;

    @GetMapping("customdb_group")
    public List<SearchMaindbGroupResponse> customdb_group() throws IOException, ResultFailException {
        return apiInterface.customdbGroup();
    }

    //리스트
    @GetMapping("{groupSeq}/data")
    public Pagination<ResultCustomInfoEntity> getPagination(@PathVariable Integer groupSeq, ResultCustomInfoSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.getPaginationCounsel(groupSeq, search);
    }

    //수정정보SEQ
    @GetMapping("{seq}")
    public ResultCustomInfoEntity get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody ResultCustomInfoFormRequest form, BindingResult bindingResult) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        if (form.getTodoStatus() != null && !form.getTodoSequences().isEmpty()) {
            for (Integer todoSequence : form.getTodoSequences()) {
                counselApiInterface.updateTodoStatus(new TodoListUpdateFormRequest(todoSequence, form.getTodoStatus()));
            }
        }

        apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody ResultCustomInfoFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        if (form.getTodoStatus() != null && !form.getTodoSequences().isEmpty()) {
            for (Integer todoSequence : form.getTodoSequences()) {
                counselApiInterface.updateTodoStatus(new TodoListUpdateFormRequest(todoSequence, form.getTodoStatus()));
            }
        }
        apiInterface.put(seq, form);
    }

    //삭제
    @DeleteMapping("{seq}")
    public void deleteData(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.deleteData(seq);
    }
}
