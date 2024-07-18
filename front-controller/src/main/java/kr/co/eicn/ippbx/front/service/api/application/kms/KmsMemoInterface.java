package kr.co.eicn.ippbx.front.service.api.application.kms;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.Memo;
import kr.co.eicn.ippbx.model.form.MemoFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Service
public class KmsMemoInterface extends ApiServerInterface {
    private static final String subUrl = "/api/v1/consultation/main";

    /**
     * 메모 생성
     */
    public void createMemo(MemoFormRequest formRequest) throws IOException, ResultFailException {
        post(subUrl+"/kms/memo", formRequest);
    }

    /**
     * 메모 조회
     */
    public List<Memo> getMemoList(String keyword) throws IOException, ResultFailException {
        return getList(subUrl+"/kms/memo?keyword=" + keyword, null, Memo.class).getData();
    }

    /**
     * 메모 수정
     */
    public void updateMemo(@PathVariable Long id, MemoFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "/kms/memo/" + id, form);
    }

    /**
     * 메모 삭제
     */
    public void deleteMemo(Long id) throws IOException, ResultFailException {
        delete(subUrl + "/kms/memo/" + id);
    }

    /**
     * 북마크
     */
    public void bookmarkMemo(Long id, boolean bookmarked) throws IOException, ResultFailException {
        put(subUrl + "/kms/memo/" + id + "/bookmarked/" + bookmarked, null);
    }
}
