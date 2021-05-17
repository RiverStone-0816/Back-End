package kr.co.eicn.ippbx.front.service;

import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbDataApiInterface;
import kr.co.eicn.ippbx.server.model.search.MaindbDataSearchRequest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;

public class MaindbDataApiInterfaceTest extends AbstractApiInterfaceTest {
    private static final Logger logger = LoggerFactory.getLogger(MaindbDataApiInterfaceTest.class);

    private MaindbDataApiInterface apiInterface;

    @Before
    public void setup() {
        apiInterface = getInterface(MaindbDataApiInterface.class);
    }

    @Test
    public void dummy() {
    }

    // @Test
    public void search() throws IOException, ResultFailException {
        final MaindbDataSearchRequest search = new MaindbDataSearchRequest();
        search.setCreatedStartDate(new Date(System.currentTimeMillis()));
        search.setCreatedEndDate(new Date(System.currentTimeMillis()));

        final HashMap<String, MaindbDataSearchRequest.FieldCondition> dbTypeFields = new HashMap<>();
        dbTypeFields.put("MAINDB_DATE_1", new MaindbDataSearchRequest.FieldCondition(null, null, search.getCreatedStartDate(), search.getCreatedEndDate()));
        dbTypeFields.put("MAINDB_TIMESTAMP_1", new MaindbDataSearchRequest.FieldCondition(null, null, search.getCreatedStartDate(), search.getCreatedEndDate()));
        dbTypeFields.put("MAINDB_STRING_1", new MaindbDataSearchRequest.FieldCondition("1234", null, search.getCreatedStartDate(), search.getCreatedEndDate()));
        dbTypeFields.put("MAINDB_CODE_1", new MaindbDataSearchRequest.FieldCondition("null", null, search.getCreatedStartDate(), search.getCreatedEndDate()));
        dbTypeFields.put("MAINDB_MULTICODE_1", new MaindbDataSearchRequest.FieldCondition("null12", null, search.getCreatedStartDate(), search.getCreatedEndDate()));

        search.setDbTypeFields(dbTypeFields);

        apiInterface.getPagination(1, search);
    }
}
