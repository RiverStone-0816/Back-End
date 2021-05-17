package kr.co.eicn.ippbx.front.service.api.service.help;

import kr.co.eicn.ippbx.front.model.form.ManualForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.BoardSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.ManualDetailResponse;
import kr.co.eicn.ippbx.server.model.search.BoardSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ManualApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ManualApiInterface.class);
    private static final String subUrl = "/api/v1/admin/help/manual/";

    public Pagination<BoardSummaryResponse> pagination(BoardSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, BoardSummaryResponse.class).getData();
    }

    public ManualDetailResponse get(Long id) throws IOException, ResultFailException {
        return getData(subUrl + id, null, ManualDetailResponse.class).getData();
    }

    public ManualDetailResponse getDetail(Long id) throws IOException, ResultFailException {
        return getData(subUrl + id + "/detail", null, ManualDetailResponse.class).getData();
    }

    public void post(ManualForm form) throws IOException, ResultFailException {
        final Map<String, FileResource> files = new HashMap<>();
        if (form.getAddingFiles() != null)
            for (int i = 0; i < form.getAddingFiles().size(); i++)
                files.put("files[" + i + "]", new FileResource(form.getAddingFiles().get(i).getFilePath(), form.getAddingFiles().get(i).getOriginalName()));

        sendByMultipartFile(HttpMethod.POST, subUrl, form, String.class, files);
    }

    public void put(Long id, ManualForm form) throws IOException, ResultFailException {
        final Map<String, FileResource> files = new HashMap<>();
        if (form.getAddingFiles() != null)
            for (int i = 0; i < form.getAddingFiles().size(); i++)
                files.put("files[" + i + "]", new FileResource(form.getAddingFiles().get(i).getFilePath(), form.getAddingFiles().get(i).getOriginalName()));

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
}
