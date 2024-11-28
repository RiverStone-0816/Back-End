package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.model.dto.customdb.QaResultResponse;
import kr.co.eicn.ippbx.model.dto.customdb.StatQaResultCodeResponse;
import kr.co.eicn.ippbx.model.dto.customdb.StatQaResultIndividualResponse;
import kr.co.eicn.ippbx.model.search.StatQaResultSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.StatQaResultRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonCodeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonFieldRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatQaResultService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(StatQaResultService.class);
    private final Map<String, StatQaResultRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;
    private final CommonCodeRepository commonCodeRepository;
    private final CommonFieldRepository commonFieldRepository;

    public StatQaResultService(CommonCodeRepository commonCodeRepository, CommonFieldRepository commonFieldRepository) {
        this.commonCodeRepository = commonCodeRepository;
        this.commonFieldRepository = commonFieldRepository;
    }

    public StatQaResultRepository getRepository() {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final StatQaResultRepository repository = new StatQaResultRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<CommonResultCustomInfo> findAll(StatQaResultSearchRequest search) {
        return getRepository().findAll(search);
    }

    public StatQaResultCodeResponse convertToStatQaResultField(StatQaResultCodeResponse codeResponse, CommonCode code, StatQaResultSearchRequest search, List<CommonResultCustomInfo> dataList) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String[] splitCode = code.getFieldId().split("_");

        List<QaResultResponse> statList = new ArrayList<>();

        long dateDiff = (search.getEndDate().getTime() - search.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
        Calendar resultDate = Calendar.getInstance();
        resultDate.setTime(search.getStartDate());

        for (int i = 0; i <= dateDiff; i++) {
            QaResultResponse stat = new QaResultResponse();

            stat.setStatDate(dateFormat.format(resultDate.getTime()));
            stat.setCount((int) dataList.stream().filter(result -> {
                        try {
                            return result.getResultType().equals(code.getType())
                                    && code.getCodeId().equals(result.getClass().getMethod("getRsCode_" + splitCode[splitCode.length - 1]).invoke(result))
                                    && result.getResultDate().after(resultDate.getTime())
                                    && result.getResultDate().before(Timestamp.valueOf(dateFormat.format(resultDate.getTime()) + " 23:59:59"));
                        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                            logger.error("Exception!", e);
                        }
                        return false;
                    }).count()
            );

            statList.add(stat);
            resultDate.add(Calendar.DATE, 1);
        }

        codeResponse.setQaResultStat(statList);
        return codeResponse;
    }

    public List<StatQaResultIndividualResponse> convertIndividualResult(StatQaResultSearchRequest search) {
        final List<CommonCode> codeList = commonCodeRepository.individualCodeList(search);
        final Map<Integer, List<CommonField>> collect = commonFieldRepository.findAllCodeField().stream().collect(Collectors.groupingBy(CommonField::getType));
        final Map<Integer, List<CommonResultCustomInfo>> resultMap = this.getRepository().findAllIndividualResult(search);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return codeList.stream().filter(code -> collect.get(code.getType()) != null && collect.get(code.getType()).stream().anyMatch(field -> field.getFieldId().equals(code.getFieldId()))).map(code -> {
            final StatQaResultIndividualResponse response = convertDto(code, StatQaResultIndividualResponse.class);
            response.setFieldInfo(collect.get(code.getType()).stream().filter(e -> e.getFieldId().equals(code.getFieldId())).map(CommonField::getFieldInfo).findFirst().orElse(""));

            final String[] splitCode = code.getFieldId().split("_");

            final long dateDiff = (search.getEndDate().getTime() - search.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
            final Calendar startDate = Calendar.getInstance();
            startDate.setTime(search.getStartDate());

            for (int i = 0; i <= dateDiff; i++) {
                final QaResultResponse stat = new QaResultResponse();
                if (Objects.nonNull(resultMap.get(code.getType()))) {
                    long count = resultMap.get(code.getType()).stream()
                            .filter(e -> {
                                try {
                                    return code.getCodeId().equals(e.getClass().getMethod("getRsCode_" + splitCode[splitCode.length - 1]).invoke(e)) && e.getResultDate().after(startDate.getTime())
                                            && e.getResultDate().before(Timestamp.valueOf(dateFormat.format(startDate.getTime()) + " 23:59:59"));
                                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                                    logger.error("Exception! {}", e);
                                }
                                return false;
                            }).count();

                    stat.setCount((int) count);
                }
                stat.setStatDate(dateFormat.format(startDate.getTime()));

                response.getQaResultStat().add(stat);
                startDate.add(Calendar.DATE, 1);
            }

            return response;
        }).collect(Collectors.toList());
    }
}
