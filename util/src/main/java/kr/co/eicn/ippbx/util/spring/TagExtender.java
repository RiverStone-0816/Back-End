package kr.co.eicn.ippbx.util.spring;

import kr.co.eicn.ippbx.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class TagExtender {
    @Autowired
    HttpServletRequest httpServletRequest;

    public void stackBody(String sourceName, String bucketName) {
        final String body = (String) httpServletRequest.getAttribute(sourceName);
        if (body == null) {
            return;
        }
        httpServletRequest.removeAttribute(sourceName);
        getBody(bucketName).add(body);
    }

    public List<String> getBody(String bucketName) {
        return RequestUtils.getAttribute(bucketName, ArrayList::new);
    }

    public List<String> getScriptsTags() {
        return getBody("TAG_SCRIPTS_LIST");
    }
}
