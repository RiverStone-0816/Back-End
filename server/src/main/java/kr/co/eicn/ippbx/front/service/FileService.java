package kr.co.eicn.ippbx.front.service;

import kr.co.eicn.ippbx.front.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * @author tinywind
 * @since 2018-01-14
 */
@Service
@PropertySource("classpath:application.properties")
public class FileService {
    public static final String FILE_PATH = "files/download";
    public static final String FILE_REQUEST_PARAM_KEY = "file";
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Value("${application.file.location}")
    private String fileLocation;

    public String url(String path) {
        if (path == null)
            return null;

        return baseUrl() + "?" + UrlUtils.encodeQueryParams(FILE_REQUEST_PARAM_KEY, path);
    }

    public String baseUrl() {
        return ("/" + FILE_PATH + "/").replaceAll("[/]+", "/");
    }

}
