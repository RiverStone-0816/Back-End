package kr.co.eicn.ippbx.front.controller.api.stt.keyword;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.stt.keyword.ProhibitedKeywordApiInterface;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/stt/keyword/prohibited-keyword", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProhibitedKeywordApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ProhibitedKeywordApiController.class);

    @Autowired
    private ProhibitedKeywordApiInterface apiInterface;

    @PostMapping("prohibit/{keyword}")
    public void prohibitPost(@PathVariable String keyword) throws IOException, ResultFailException {
        apiInterface.prohibitPost(keyword);
    }

    @PostMapping("keyword/{keyword}")
    public void keywordPost(@PathVariable String keyword) throws IOException, ResultFailException {
        apiInterface.keywordPost(keyword);
    }

    @DeleteMapping("{keyword}")
    public void delete(@PathVariable String keyword) throws IOException, ResultFailException {
        apiInterface.deleteKeyword(keyword);
    }

}
