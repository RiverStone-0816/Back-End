package kr.co.eicn.ippbx.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeWriteResponse;
import kr.co.eicn.ippbx.model.entity.customdb.TranscribeDataEntity;
import kr.co.eicn.ippbx.model.form.TranscribeWriteFormRequest;
import kr.co.eicn.ippbx.server.repository.customdb.TranscribeDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.replaceEach;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranscribeDataService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(TranscribeDataService.class);
    private final Map<String, TranscribeDataRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;
    protected TypeFactory typeFactory = new ObjectMapper().getTypeFactory();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final FileSystemStorageService fileSystemStorageService;

    @Value("${file.path.stt}")
    private String savePath;

    public TranscribeDataRepository getRepository() {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final TranscribeDataRepository repository = new TranscribeDataRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @PostConstruct
    public void setUp() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public TranscribeWriteResponse getWriteInfo(Integer fileSeq) throws JsonProcessingException {
        TranscribeDataEntity entity = getRepository().findOne(fileSeq);
        TranscribeWriteResponse response = new TranscribeWriteResponse();
        JavaType returnType = typeFactory.constructCollectionLikeType(ArrayList.class, TranscribeWriteResponse.HypInfo.class);

        response.setSeq(entity.getSeq());
        response.setDataList(convertJsonToObject(entity.getHypinfo(), returnType));
        response.setRefList(convertJsonToObject(entity.getRefinfo(), returnType));

        return response;
    }

    private List<TranscribeWriteResponse.HypInfo> convertJsonToObject(String content, JavaType type) throws JsonProcessingException {
        if (StringUtils.isNotEmpty(content))
            return objectMapper.readValue(content, type);
        else
            return new ArrayList<>();
    }

    public void update(Integer fileSeq, TranscribeWriteFormRequest request) throws JsonProcessingException {
        String hypInfo = convertInfoToText(request.getHypData(), "hyp_text");
        String refInfo = convertInfoToText(request.getRefData(), "ref_text");

        getRepository().updateInfo(fileSeq, hypInfo, refInfo);
    }

    private String convertInfoToText(List<TranscribeWriteFormRequest.TextInfo> info, String text) {
        return "[" + info.stream().map(e -> {
            try {
                return objectMapper.writeValueAsString(e);
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
            return "";
        }).reduce((e, f) -> e + "," + f).orElse("").replaceAll("text", text) + "]";
    }

    public Resource getResources(Integer fileSeq) {
        TranscribeDataEntity entity = getRepository().findOne(fileSeq);

        return fileSystemStorageService.loadAsResource(Paths.get(replaceEach(savePath, new String[]{"{0}", "{1}"}, new String[]{g.getUser().getCompanyId(), String.valueOf(entity.getGroupcode())})), entity.getFilename());
    }

    public void updateLearnStatus(Integer seq, String status) {
        getRepository().updateLearnStatus(seq, status);
    }
}
