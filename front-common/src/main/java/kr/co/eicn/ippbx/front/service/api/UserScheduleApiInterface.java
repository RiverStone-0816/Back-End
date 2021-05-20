package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.model.entity.eicn.UserScheduleEntity;
import kr.co.eicn.ippbx.model.form.UserScheduleFormRequest;
import kr.co.eicn.ippbx.model.search.UserScheduleSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UserScheduleApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserScheduleApiInterface.class);
    private static final String subUrl = "/api/user-schedule/";

    public List<UserScheduleEntity> search(UserScheduleSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, UserScheduleEntity.class).getData();
    }

    public UserScheduleEntity get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, UserScheduleEntity.class).getData();
    }

    public void post(UserScheduleFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, UserScheduleFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }
}
