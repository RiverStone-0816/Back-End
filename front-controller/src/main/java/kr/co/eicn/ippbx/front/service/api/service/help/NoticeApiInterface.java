package kr.co.eicn.ippbx.front.service.api.service.help;

import kr.co.eicn.ippbx.front.model.form.NoticeForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.BoardSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.NoticeDetailResponse;
import kr.co.eicn.ippbx.model.search.BoardSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class NoticeApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(NoticeApiInterface.class);
    private static final String subUrl = "/api/v1/admin/help/notice/";

    public Pagination<BoardSummaryResponse> pagination(BoardSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, BoardSummaryResponse.class).getData();
    }

    public NoticeDetailResponse get(Long id) throws IOException, ResultFailException {
        return getData(subUrl + id, null, NoticeDetailResponse.class).getData();
    }

    public NoticeDetailResponse getDetail(Long id) throws IOException, ResultFailException {
        return getData(subUrl + id + "/detail", null, NoticeDetailResponse.class).getData();
    }

    public void post(NoticeForm form) throws IOException, ResultFailException {
        final Map<String, FileResource> files = new HashMap<>();
        if (form.getAddingFiles() != null)
            for (int i = 0; i < form.getAddingFiles().size(); i++)
                files.put("files[" + i + "]", new FileResource(form.getAddingFiles().get(i).getFilePath(), form.getAddingFiles().get(i).getOriginalName()));

        sendByMultipartFile(HttpMethod.POST, subUrl, form, String.class, files);
    }

    public void put(Long id, NoticeForm form) throws IOException, ResultFailException {
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

    public Resource getResource(Integer id) throws IOException, ResultFailException {
        return getResourceResponseAll(subUrl + id+"/specific-file-resource");
    }
}
