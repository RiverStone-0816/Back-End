package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatServiceInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.WebchatIntroChannelListRecord;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceSummaryInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatServiceInfoFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatIntroChannelListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.WebchatServiceInfoRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Service
public class WebChattingConfigService extends ApiBaseService {
    private final WebchatServiceInfoRepository webchatServiceInfoRepository;
    private final WebchatIntroChannelListRepository webchatIntroChannelListRepository;
    private final ImageFileStorageService imageFileStorageService;

    @Value("${file.path.chatbot}")
    private String savePath;

    protected WebchatServiceInfoResponse convertEntityToResponse(WebchatServiceInfo entity, List<WebchatServiceInfoResponse.IntroChannel> introChannelList) {
        WebchatServiceInfoResponse response = new WebchatServiceInfoResponse();

        response.setSeq(entity.getSeq());
        response.setChannelName(entity.getWebchatServiceName());
        response.setSenderKey(entity.getSenderKey());
        response.setEnableChat("Y".equals(entity.getIsChattEnable()));
        response.setDisplayCompanyName(entity.getDisplayCompanyName());
        response.setMessage(entity.getMsg());
        response.setImageFileName(entity.getImage());
        response.setBackgroundColor(entity.getBgcolor());
        response.setProfileFileName(entity.getProfile());
        response.setIntroChannelList(introChannelList);

        return response;
    }

    public List<WebchatServiceSummaryInfoResponse> getAll() {
        List<WebchatServiceInfo> data = webchatServiceInfoRepository.findAll();

        return data.stream().map(e -> {
            WebchatServiceSummaryInfoResponse row = convertDto(e, WebchatServiceSummaryInfoResponse.class);

            row.setChannelName(e.getWebchatServiceName());
            row.setEnableChat("Y".equals(e.getIsChattEnable()));
            row.setDisplayCompanyName(e.getDisplayCompanyName());

            return row;
        }).collect(Collectors.toList());
    }

    public WebchatServiceInfoResponse get(Integer seq) {
        WebchatServiceInfo data = webchatServiceInfoRepository.findOne(seq);
        List<WebchatServiceInfoResponse.IntroChannel> introChannelList = webchatIntroChannelListRepository.findAllByIntroId(seq);

        return convertEntityToResponse(data, introChannelList);
    }

    public void insert(WebchatServiceInfoFormRequest form) {
        Integer id = webchatServiceInfoRepository.insert(form);

        insertIntroChannelList(id, form.getIntroChannelList());
    }

    public void update(Integer seq, WebchatServiceInfoFormRequest form) {
        webchatServiceInfoRepository.update(seq, form);

        webchatIntroChannelListRepository.deleteByIntroId(seq);
        insertIntroChannelList(seq, form.getIntroChannelList());
    }

    private void insertIntroChannelList(Integer introId, List<WebchatServiceInfoFormRequest.IntroChannel> introChannelList) {
        List<WebchatIntroChannelListRecord> data = introChannelList.stream().map(e -> {
            WebchatIntroChannelListRecord record = new WebchatIntroChannelListRecord();

            record.setIntroId(introId);
            record.setChannelId(e.getId());
            record.setChannelType(e.getType().getCode());
            record.setCompanyId(g.getUser().getCompanyId());

            return record;
        }).collect(Collectors.toList());
        webchatIntroChannelListRepository.insertAll(data);
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
