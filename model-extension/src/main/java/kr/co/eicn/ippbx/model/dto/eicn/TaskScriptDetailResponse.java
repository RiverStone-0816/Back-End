package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TaskScriptDetailResponse {
    private Long id;
    private Long categoryId;   //카테고리
    private String title;   //제목
    private Timestamp createdAt; //등록일
    private String tag;     //태그
    private String content;      //내용
    private List<FileNameDetailResponse> fileInfo;     //첨부파일

    private static boolean imageFilename(String filename) {
        final List<String> IMAGE_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp", ".svg", ".bmp", ".heif");

        if (StringUtils.isEmpty(filename))
            return false;

        final String lowerCase = filename.toLowerCase();

        for (String extension : IMAGE_EXTENSIONS)
            if (lowerCase.endsWith(extension))
                return true;

        return false;
    }

    public List<FileNameDetailResponse> getImageFileInfos() {
        if (fileInfo == null)
            return new ArrayList<>();

        return fileInfo.stream().filter(e -> imageFilename(e.getOriginalName())).collect(Collectors.toList());
    }

    public List<FileNameDetailResponse> getNonImageFileInfos() {
        if (fileInfo == null)
            return new ArrayList<>();

        return fileInfo.stream().filter(e -> !imageFilename(e.getOriginalName())).collect(Collectors.toList());
    }
}
