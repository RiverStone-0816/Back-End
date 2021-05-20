package kr.co.eicn.ippbx.server.repository.pds;

import kr.co.eicn.ippbx.meta.jooq.pds.tables.CommonExecutePDSCustomInfo;
import kr.co.eicn.ippbx.model.dto.pds.ExecutePDSCustomInfoCountResponse;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.pds.tables.ExecutePdsCustomInfo.EXECUTE_PDS_CUSTOM_INFO;

@Getter
public class ExecutePDSCustomInfoRepository extends PDSDbBaseRepository<CommonExecutePDSCustomInfo, kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.CommonExecutePDSCustomInfo, Integer> {
    private final Logger logger = LoggerFactory.getLogger(ExecutePDSCustomInfoRepository.class);

    private final CommonExecutePDSCustomInfo TABLE;

    public ExecutePDSCustomInfoRepository(String name) {
        super(new CommonExecutePDSCustomInfo(name), new CommonExecutePDSCustomInfo(name).SEQ, kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.CommonExecutePDSCustomInfo.class);
        TABLE = new CommonExecutePDSCustomInfo(name);
    }

    public ExecutePDSCustomInfoCountResponse findAllCount(String executeId) {
        return findAllCount(executeId, dsl);
    }

    public ExecutePDSCustomInfoCountResponse findAllCount(String executeId, DSLContext dslContext) {
        ExecutePDSCustomInfoCountResponse response = new ExecutePDSCustomInfoCountResponse();

        List<kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.CommonExecutePDSCustomInfo> list = findAll(dslContext, TABLE.EXECUTE_ID.eq(executeId));
        response.setTotalTry((int) list.stream().filter(execute -> !execute.getStatus().equals("")).count());
        response.setTotalMod(getCount(list, "", ""));
        response.setRings(getCount(list, "R", ""));
        response.setDialed(getCount(list, "D", ""));
        response.setHanged(getCount(list, "H", ""));
        response.setRst1(getCount(list, "H", "16"));
        response.setRst2(getCount(list, "H", "19"));
        response.setRst3(getCount(list, "H", "17"));
        response.setRst4(getCount(list, "H", "220"));
        response.setRst5(response.getHanged() - (response.getRst1() + response.getRst2() + response.getRst3() + response.getRst4()));

        return response;
    }

    private Integer getCount(List<kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.CommonExecutePDSCustomInfo> list, String status, String hangupCause) {
        int size = 0;
        if (StringUtils.isEmpty(hangupCause))
            size = (int) list.stream().filter(execute -> execute.getStatus().equals(status)).count();
        else if (StringUtils.isNotEmpty(hangupCause))
            size = (int) list.stream().filter(execute -> execute.getStatus().equals(status) && execute.getHangupCause().equals(hangupCause)).count();

        return size;
    }

    public int createTableIfNotExists() {
        return createTableIfNotExists(dsl);
    }

    public int createTableIfNotExists(DSLContext dslContext) {
        return dslContext.createTableIfNotExists(TABLE)
                .columns(EXECUTE_PDS_CUSTOM_INFO.fields())
                .constraint(DSL.constraint(TABLE.SEQ.getName()).primaryKey(TABLE.SEQ.getName()))
                .indexes(EXECUTE_PDS_CUSTOM_INFO.getIndexes().stream().filter(index -> !"PRIMARY".equals(index.getName())).collect(Collectors.toList()))
                .storage("ENGINE=MyISAM")
                .execute();
    }
}
