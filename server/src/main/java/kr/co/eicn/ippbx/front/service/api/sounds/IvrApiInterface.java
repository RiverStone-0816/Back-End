package kr.co.eicn.ippbx.front.service.api.sounds;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.IvrTree;
import kr.co.eicn.ippbx.server.model.dto.eicn.IvrResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryIvrTreeResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.server.model.dto.eicn.WebVoiceResponse;
import kr.co.eicn.ippbx.server.model.form.IvrFormRequest;
import kr.co.eicn.ippbx.server.model.form.IvrFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.IvrPositionFormRequest;
import kr.co.eicn.ippbx.server.model.form.WebVoiceItemsFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class IvrApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(IvrApiInterface.class);
    private static final String subUrl = "/api/v1/admin/sounds/ivr/";

    public List<IvrResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, IvrResponse.class).getData();
    }

    public List<IvrResponse> rootIvrTrees() throws IOException, ResultFailException {
        return getList(subUrl + "root-ivr-trees", null, IvrResponse.class).getData();
    }

    public List<IvrResponse> buildHierarchyTree() throws IOException, ResultFailException {
        return getList(subUrl + "hierarchy", null, IvrResponse.class).getData();
    }

    public IvrResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, IvrResponse.class).getData();
    }

    public Integer postMenu(IvrFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer seq, IvrFormUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void updatePosition(Integer seq, IvrPositionFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq + "/position", form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public WebVoiceResponse getWebVoice(Integer ivrCode) throws IOException, ResultFailException {
        return getData(subUrl + ivrCode + "/web-voice", null, WebVoiceResponse.class).getData();
    }

    public void apply(Integer ivrCode, WebVoiceItemsFormRequest form) throws IOException, ResultFailException {
        post(subUrl + ivrCode + "/apply", form);
    }

    public List<SummaryIvrTreeResponse> rootNodes() throws IOException, ResultFailException {
        return getList(subUrl + "root-node", null, SummaryIvrTreeResponse.class).getData();
    }

    public List<IvrTree> addIvrTreeList() throws IOException, ResultFailException {
        return getList(subUrl + "add-ivr-list", null, IvrTree.class).getData();
    }

    public List<IvrTree> getIvrList() throws IOException, ResultFailException {
        return getList(subUrl + "/ivr-list", null, IvrTree.class).getData();
    }

    public List<SummaryNumber070Response> addNumber070List() throws IOException, ResultFailException {
        return getList(subUrl + "add-number-list", null, SummaryNumber070Response.class).getData();
    }
}
