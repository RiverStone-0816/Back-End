package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonMaindbKeyInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.MaindbKeyInfo;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbKeyInfoEntity;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.jooq.impl.DSL.md5;

@Getter
public class MaindbKeyInfoRepository extends CustomDBBaseRepository<CommonMaindbKeyInfo, MaindbKeyInfoEntity, String>{
    protected final Logger logger = LoggerFactory.getLogger(MaindbCustomInfoRepository.class);

    private final CommonMaindbKeyInfo TABLE;

    public MaindbKeyInfoRepository(String companyId) {
        super(new CommonMaindbKeyInfo(companyId), new CommonMaindbKeyInfo(companyId).KEY_VALUE, MaindbKeyInfoEntity.class);
        TABLE = new CommonMaindbKeyInfo(companyId);
    }

    public void deleteByCustomId(String customId) {
        delete(TABLE.CUSTOM_ID.eq(customId));

    }

    public List<MaindbKeyInfoEntity> selectOne(String keyValue, Integer groupId){
        return dsl.select()
                .from(TABLE)
                .where(TABLE.KEY_VALUE.eq(md5(keyValue)))
                .and(TABLE.GROUP_ID.eq(groupId))
                .fetchInto(MaindbKeyInfoEntity.class);
    }

    public void insert(String customId, String keyValue, Integer groupId){
        dsl.insertInto(TABLE)
                .set(TABLE.KEY_VALUE, md5(keyValue))
                .set(TABLE.CUSTOM_ID,customId)
                .set(TABLE.GROUP_ID, groupId)
                .execute();
    }
}
