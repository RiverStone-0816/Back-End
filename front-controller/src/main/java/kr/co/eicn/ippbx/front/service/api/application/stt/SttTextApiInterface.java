package kr.co.eicn.ippbx.front.service.api.application.stt;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.SttCdr;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.SttMessage;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SttTextApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(SttTextApiInterface.class);

    private static final String subUrl = "/api/v1/consultation/main/";

    public void updateRemind(String messageId) throws IOException, ResultFailException {
        super.put(subUrl + "stt-text/remind/"+messageId, null);
    }

    public String adminMonit(String userId) throws IOException, ResultFailException {
        return getData(subUrl + "stt/admin-monit/"+userId,null, String.class).getData();
    }

    public SttCdr getSttCdr(String callUniqueId) throws IOException, ResultFailException {
        return getData(subUrl + "stt/call-unique-id/"+callUniqueId,null, SttCdr.class).getData();
    }

    public List<SttMessage> getSttChatMessage(String callUniqueId) throws IOException, ResultFailException {
        return getList(subUrl + "stt-chat/call-unique-id/"+callUniqueId,null, SttMessage.class).getData();
    }
}
