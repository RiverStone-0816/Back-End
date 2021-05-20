package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonVocCustomList;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.customdb.tables.VocCustomList.VOC_CUSTOM_LIST;

@Getter
public class VocCustomListRepository extends CustomDBBaseRepository<CommonVocCustomList, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonVocCustomList, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(VocCustomListRepository.class);

    private final CommonVocCustomList TABLE;

    public VocCustomListRepository(String table) {
        super(new CommonVocCustomList(table), new CommonVocCustomList(table).SEQ, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonVocCustomList.class);
        TABLE = new CommonVocCustomList(table);

        addField(TABLE);
        orderByFields.add(TABLE.INSERT_DATE.asc());
    }

    public int createTableIfNotExists() {
        return createTableIfNotExists(dsl);
    }

    public int createTableIfNotExists(DSLContext dslContext) {
        return dslContext.createTableIfNotExists(TABLE)
                .columns(VOC_CUSTOM_LIST.fields())
                .constraint(DSL.constraint(TABLE.SEQ.getName()).primaryKey(TABLE.SEQ.getName()))
                .indexes(VOC_CUSTOM_LIST.getIndexes().stream().filter(index -> !"PRIMARY".equals(index.getName())).collect(Collectors.toList()))
                .storage("ENGINE=MyISAM")
                .execute();
    }
}
