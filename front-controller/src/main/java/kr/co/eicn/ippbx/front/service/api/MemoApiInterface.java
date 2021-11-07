package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.entity.customdb.MemoMsgEntity;
import kr.co.eicn.ippbx.model.form.MemoMsgFormRequest;
import kr.co.eicn.ippbx.model.search.MemoMsgSearchRequest;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MemoApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MemoApiInterface.class);

    private static final String subUrl = "/api/memo/";

    @SneakyThrows
    public Pagination<MemoMsgEntity> getSendMemoList(MemoMsgSearchRequest search) {
        return getPagination(subUrl + "send", search, MemoMsgEntity.class).getData();
    }

    @SneakyThrows
    public Pagination<MemoMsgEntity> getReceiveMemoList(MemoMsgSearchRequest search) {
        return getPagination(subUrl + "receive", search, MemoMsgEntity.class).getData();
    }

    @SneakyThrows
    public Integer getUnreadMessageCount() {
        return getData(subUrl + "unread-message", null, Integer.class).getData();
    }

    @SneakyThrows
    public MemoMsgEntity getMemoMessage(Integer seq) {
        return getData(subUrl + seq, null, MemoMsgEntity.class).getData();
    }

    @SneakyThrows
    public String sendMemoMessage(MemoMsgFormRequest form) {
        return getData(HttpMethod.POST, subUrl, form, String.class, false).getData();
    }

    @SneakyThrows
    public String receiveMemoMessage(Integer seq, MemoMsgFormRequest form) {
        return getData(HttpMethod.POST, subUrl + seq, form, String.class, false).getData();
    }

    @SneakyThrows
    public void deleteMemoMessages(List<Integer> memoSequences) {
        final JsonResult<?> result = call(subUrl, Collections.singletonMap("memoSequences", memoSequences), JsonResult.class, HttpMethod.DELETE, true);
        if (result.isFailure())
            throw new ResultFailException(result);
    }

    @SneakyThrows
    public void updateProfile(FileForm form) {
        sendByMultipartFile(HttpMethod.POST, subUrl + "profile", form, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));
    }

    @SneakyThrows
    public void deleteSpecificFile() {
        delete(subUrl + "delete-specific-file");
    }
}
