package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.model.dto.customdb.QaResultResponse;
import kr.co.eicn.ippbx.model.dto.customdb.StatQaResultCodeResponse;
import kr.co.eicn.ippbx.model.dto.customdb.StatQaResultIndividualResponse;
import kr.co.eicn.ippbx.model.search.StatQaResultIndividualSearchRequest;
import kr.co.eicn.ippbx.model.search.StatQaResultSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.StatQaResultRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CommonCodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

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

    public StatQaResultService(CommonCodeRepository commonCodeRepository) {
        this.commonCodeRepository = commonCodeRepository;
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

    public StatQaResultCodeResponse convertToStatQaResultField(List<CommonResultCustomInfo> resultList, StatQaResultCodeResponse codeResponse, CommonCode code, StatQaResultSearchRequest search) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<QaResultResponse> statList = new ArrayList<>();

        long dateDiff = (search.getEndDate().getTime() - search.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
        Calendar resultDate = Calendar.getInstance();
        resultDate.setTime(search.getStartDate());

        for (int i = 0; i <= dateDiff; i++) {
            QaResultResponse stat = new QaResultResponse();

            stat.setStatDate(dateFormat.format(resultDate.getTime()));
            stat.setCount((int) resultList.stream().filter(result -> result.getResultType().equals(code.getType())
                    && (code.getCodeId().equals(result.getRsCode_1()) || code.getCodeId().equals(result.getRsCode_2()) || code.getCodeId().equals(result.getRsCode_3())
                    || code.getCodeId().equals(result.getRsCode_4()) || code.getCodeId().equals(result.getRsCode_5()))
                    && result.getResultDate().after(resultDate.getTime())
                    && result.getResultDate().before(Timestamp.valueOf(dateFormat.format(resultDate.getTime()) + " 23:59:59"))).count()
            );

            statList.add(stat);
            resultDate.add(Calendar.DATE, 1);
        }

        codeResponse.setQaResultStat(statList);
        return codeResponse;
    }

    public List<StatQaResultIndividualResponse> convertIndividualResult(StatQaResultIndividualSearchRequest search) {
        final List<CommonCode> codeList = commonCodeRepository.individualCodeList(search);
        final Map<Integer, List<CommonResultCustomInfo>> resultMap = this.getRepository().findAllIndividualResult(search);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return codeList.stream().map(code -> {
            final StatQaResultIndividualResponse response = convertDto(code, StatQaResultIndividualResponse.class);

            final long dateDiff = (search.getEndDate().getTime() - search.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(search.getStartDate());

            for (int i = 0; i <= dateDiff; i++) {
                final QaResultResponse stat = new QaResultResponse();
                if (Objects.nonNull(resultMap.get(code.getType()))) {
                    long count = resultMap.get(code.getType()).stream()
                            .filter(e -> {
                                String sadads = code.getFieldId().equals("RS_CODE_1") ? e.getRsCode_1() : code.getFieldId().equals("RS_CODE_2") ? e.getRsCode_2()
                                        : code.getFieldId().equals("RS_CODE_3") ? e.getRsCode_3() : code.getFieldId().equals("RS_CODE_4") ? e.getRsCode_4()
                                        : e.getRsCode_5();

                                return code.getCodeId().equals(sadads) && e.getResultDate().after(startDate.getTime())
                                        && e.getResultDate().before(Timestamp.valueOf(dateFormat.format(startDate.getTime()) + " 23:59:59"));
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
