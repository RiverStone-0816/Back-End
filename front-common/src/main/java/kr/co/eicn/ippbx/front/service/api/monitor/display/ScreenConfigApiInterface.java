package kr.co.eicn.ippbx.front.service.api.monitor.display;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.entity.eicn.ScreenConfigEntity;
import kr.co.eicn.ippbx.model.form.ScreenConfigFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ScreenConfigApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ScreenConfigApiInterface.class);
    private static final String subUrl = "/api/v1/admin/monitor/screen/";

    public List<ScreenConfigEntity> list() throws IOException, ResultFailException {
        return getList(subUrl, null, ScreenConfigEntity.class).getData();
    }

    public ScreenConfigEntity get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, ScreenConfigEntity.class).getData();
    }

    public void post(ScreenConfigFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, ScreenConfigFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }
}
