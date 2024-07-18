package kr.co.eicn.ippbx.front.service.api.stt.keyword;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.ProhibitedKeyword;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProhibitedKeywordApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ProhibitedKeywordApiInterface.class);
    private static final String subUrl = "/api/v1/admin/keyword/";

    public List<ProhibitedKeyword> getList() throws IOException, ResultFailException {
        return getList(subUrl, null, ProhibitedKeyword.class).getData();
    }

    public void prohibitPost(String keyword) throws IOException, ResultFailException {
        post(subUrl +"prohibit/"+ keyword, null);
    }

    public void keywordPost(String keyword) throws IOException, ResultFailException {
        post(subUrl +"keyword/"+ keyword, null);
    }

    public void deleteKeyword(String keyword) throws IOException, ResultFailException {
        delete(subUrl+keyword);
    }
}
