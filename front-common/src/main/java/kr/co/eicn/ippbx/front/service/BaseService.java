package kr.co.eicn.ippbx.front.service;

import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.util.spring.RequestMessage;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService {
    @Autowired
    protected RequestGlobal g;

    @Autowired
    protected RequestMessage message;
}
