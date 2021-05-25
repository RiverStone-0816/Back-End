package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonLink;
import kr.co.eicn.ippbx.model.form.PersonLinkFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PersonLinkApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PersonLinkApiInterface.class);
    private static final String subUrl = "/api/person-link/";

    public List<PersonLink> list() throws IOException, ResultFailException {
        return getList(subUrl, null, PersonLink.class).getData();
    }

    public PersonLink get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, PersonLink.class).getData();
    }

    public Integer post(PersonLinkFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer seq, PersonLinkFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }
}
