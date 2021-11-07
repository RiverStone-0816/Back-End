package kr.co.eicn.ippbx.front.controller.api.service.help;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.model.form.TaskScriptCategoryForm;
import kr.co.eicn.ippbx.front.model.form.TaskScriptForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.help.TaskScriptApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.TaskScriptDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TaskScriptSummaryResponse;
import kr.co.eicn.ippbx.model.form.TaskScriptCategoryFormRequest;
import kr.co.eicn.ippbx.model.search.TaskScriptSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/task-script", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskScriptApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TaskScriptApiController.class);

    @Autowired
    private TaskScriptApiInterface apiInterface;

    @GetMapping
    public Pagination<TaskScriptSummaryResponse> pagination(TaskScriptSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{id}")
    public TaskScriptDetailResponse get(@PathVariable Long id) throws IOException, ResultFailException {
        return apiInterface.get(id);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody TaskScriptForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PostMapping("{id}")
    public void put(@Valid @RequestBody TaskScriptForm form, BindingResult bindingResult, @PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.put(id, form);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.delete(id);
    }

    @PostMapping("category")
    public void updateCategory(@Valid @RequestBody TaskScriptCategoryForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        final List<Exception> exceptions = new ArrayList<>();

        for (Long categoryId : form.getModifyingCategoryIdToNameMap().keySet()) {
            final TaskScriptCategoryFormRequest categoryFormRequest = new TaskScriptCategoryFormRequest();
            categoryFormRequest.setName(form.getModifyingCategoryIdToNameMap().get(categoryId));
            try {
                apiInterface.updateTaskScriptCategory(categoryId, categoryFormRequest);
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                exceptions.add(e);
            }
        }

        for (String categoryName : form.getAddingCategoryNames()) {
            final TaskScriptCategoryFormRequest categoryFormRequest = new TaskScriptCategoryFormRequest();
            categoryFormRequest.setName(categoryName);
            try {
                apiInterface.insertTaskScriptCategory(categoryFormRequest);
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                exceptions.add(e);
            }
        }

        for (Long categoryId : form.getDeletingCategoryIds()) {
            try {
                apiInterface.deleteTaskScriptCategory(categoryId);
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                exceptions.add(e);
            }
        }

        if (!exceptions.isEmpty())
            throw new ResultFailException(exceptions.get(0).getMessage());
    }

    /**
     * 카테고리 분류
     */
    @PostMapping("post-script-category")
    public void insertTaskScriptCategory(@Valid @RequestBody TaskScriptCategoryFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.insertTaskScriptCategory(form);
    }

    @PutMapping(value = "put-script-category/{id}")
    public void updateTaskScriptCategory(@Valid @RequestBody TaskScriptCategoryFormRequest form, BindingResult bindingResult, @PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.updateTaskScriptCategory(id, form);
    }

    @DeleteMapping(value = "delete-script-category/{id}")
    public void deleteTaskScriptCategory(@PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.deleteTaskScriptCategory(id);
    }

}
