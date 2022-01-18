package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.util.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static org.springframework.util.StringUtils.cleanPath;

@Slf4j
@Service
public class ImageFileStorageService extends FileSystemStorageService {
    public String uploadImage(Path path, MultipartFile image) {
        if (image == null || image.isEmpty())
            throw new IllegalArgumentException("파일을 확인할 수 없습니다.");
        if (!StringUtils.endsWithAny(Objects.requireNonNull(image.getOriginalFilename()).toLowerCase(), ".jpg", "jpeg", ".png"))
            throw new IllegalArgumentException("알 수 없는 파일 확장자입니다.");

        final String saveFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).concat("_") + UrlUtils.decode(cleanPath(Objects.requireNonNull(image.getOriginalFilename()).replaceAll("[ ()]", "")));

        store(path, saveFileName, image);

        return saveFileName;
    }

    public Resource loadImage(Path path, String fileName) {
        if (!StringUtils.endsWithAny(Objects.requireNonNull(fileName).toLowerCase(), ".jpg", "jpeg", ".png"))
            throw new IllegalArgumentException("알 수 없는 파일 확장자입니다.");

        return loadAsResource(path, fileName);
    }
}
