package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatServiceInfo;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatServiceInfoFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatServiceInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.replace;

@Slf4j
@AllArgsConstructor
@Service
public class WebChattingConfigService extends ApiBaseService {
    private final WebchatServiceInfoRepository webchatServiceInfoRepository;
    private final ImageFileStorageService imageFileStorageService;

    @Value("${file.path.chatbot}")
    private String savePath;

    protected WebchatServiceInfoResponse convertEntityToResponse(WebchatServiceInfo entity) {
        WebchatServiceInfoResponse response = new WebchatServiceInfoResponse();

        response.setSeq(entity.getSeq());
        response.setChannelName(entity.getWebchatServiceName());
        response.setSenderKey(entity.getSenderKey());
        response.setEnableChat(entity.getIsChattEnable());
        response.setDisplayCompanyName(entity.getDisplayCompanyName());
        response.setMessage(entity.getMsg());
        response.setImageFileName(entity.getImage());
        response.setBackgroundColor(entity.getBgcolor());
        response.setProfileFileName(entity.getProfile());

        return response;
    }

    public List<WebchatServiceInfoResponse> getAll() {
        List<WebchatServiceInfo> data = webchatServiceInfoRepository.findAll();

        return data.stream().map(this::convertEntityToResponse).collect(Collectors.toList());
    }

    public WebchatServiceInfoResponse get(Integer seq) {
        WebchatServiceInfo data = webchatServiceInfoRepository.findOne(seq);

        return convertEntityToResponse(data);
    }

    public void insert(WebchatServiceInfoFormRequest form) {
        webchatServiceInfoRepository.insert(form);
    }

    public void update(Integer seq, WebchatServiceInfoFormRequest form) {
        webchatServiceInfoRepository.update(seq, form);
    }

    public void delete(Integer seq) {
        webchatServiceInfoRepository.delete(seq);
    }

    public String uploadImage(MultipartFile image) {
        final Path newPath = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));

        return imageFileStorageService.uploadImage(newPath, image);
    }

    public Resource getImage(String fileName) {
        final Path newPath = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));

        return imageFileStorageService.loadImage(newPath, fileName);
    }
}
