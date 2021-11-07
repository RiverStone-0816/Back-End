package kr.co.eicn.ippbx.front.service.api.service.help;

import kr.co.eicn.ippbx.front.model.form.TaskScriptForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.TaskScriptCategoryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TaskScriptDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TaskScriptSummaryResponse;
import kr.co.eicn.ippbx.model.form.TaskScriptCategoryFormRequest;
import kr.co.eicn.ippbx.model.search.TaskScriptSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskScriptApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TaskScriptApiInterface.class);
    private static final String subUrl = "/api/v1/admin/help/script/";

    public Pagination<TaskScriptSummaryResponse> pagination(TaskScriptSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, TaskScriptSummaryResponse.class).getData();
    }

    public TaskScriptDetailResponse get(Long id) throws IOException, ResultFailException {
        return getData(subUrl + id, null, TaskScriptDetailResponse.class).getData();
    }

    public void post(TaskScriptForm form) throws IOException, ResultFailException {
        final Map<String, FileResource> files = new HashMap<>();
        if (form.getAddingFiles() != null)
            for (int i = 0; i < form.getAddingFiles().size(); i++)
                files.put("files[" + i + "]", new FileResource(form.getAddingFiles().get(i).getFilePath(), form.getAddingFiles().get(i).getOriginalName()));

        form.setAddingFiles(null);
        sendByMultipartFile(HttpMethod.POST, subUrl, form, String.class, files);
    }

    public void put(Long id, TaskScriptForm form) throws IOException, ResultFailException {
        final Map<String, FileResource> files = new HashMap<>();
        if (form.getAddingFiles() != null)
            for (int i = 0; i < form.getAddingFiles().size(); i++)
                files.put("files[" + i + "]", new FileResource(form.getAddingFiles().get(i).getFilePath(), form.getAddingFiles().get(i).getOriginalName()));

        form.setAddingFiles(null);
        sendByMultipartFile(HttpMethod.POST, subUrl + id, form, String.class, files);

        if (form.getDeletingFiles() != null)
            for (Long deletingFileId : form.getDeletingFiles()) {
                try {
                    deleteSpecificFile(deletingFileId);
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
    }

    public void deleteSpecificFile(Long fileId) throws IOException, ResultFailException {
        delete(subUrl + "delete-specific-file/" + fileId);
    }

    public void delete(Long id) throws IOException, ResultFailException {
        delete(subUrl + id);
    }

    public void insertTaskScriptCategory(TaskScriptCategoryFormRequest form) throws IOException, ResultFailException {
        post(subUrl + "post-script-category", form);
    }

    public void updateTaskScriptCategory(Long id, TaskScriptCategoryFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "put-script-category/" + id, form);
    }

    public void deleteTaskScriptCategory(Long id) throws IOException, ResultFailException {
        delete(subUrl + "delete-script-category/" + id);
    }

    public List<TaskScriptCategoryResponse> taskScriptCategoryList() throws IOException, ResultFailException {
        return getList(subUrl + "category-list", null, TaskScriptCategoryResponse.class).getData();
    }
}
