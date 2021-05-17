package kr.co.eicn.ippbx.front.service;

import kr.co.eicn.ippbx.front.service.api.AuthApiInterface;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AuthApiInterfaceTest extends AbstractApiInterfaceTest {
    private static final Logger logger = LoggerFactory.getLogger(AuthApiInterfaceTest.class);

    private AuthApiInterface apiInterface;

    @Before
    public void setup() {
        apiInterface = getInterface(AuthApiInterface.class);
    }

    @Test
    public void logout() throws IOException {
        // apiInterface.logout();
    }
}
