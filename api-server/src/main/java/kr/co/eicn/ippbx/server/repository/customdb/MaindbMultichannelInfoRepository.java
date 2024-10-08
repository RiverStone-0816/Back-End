package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonMaindbMultichannelInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.MaindbGroup;
import kr.co.eicn.ippbx.model.dto.customdb.CommonMultiChannelInfoResponse;
import kr.co.eicn.ippbx.model.dto.customdb.CustomMultichannelInfoResponse;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbMultichannelInfoEntity;
import kr.co.eicn.ippbx.model.enums.MultichannelChannelType;
import kr.co.eicn.ippbx.model.form.MaindbCustomInfoFormRequest;
import kr.co.eicn.ippbx.server.service.MaindbCustomInfoService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Getter
public class MaindbMultichannelInfoRepository extends CustomDBBaseRepository<CommonMaindbMultichannelInfo, MaindbMultichannelInfoEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(MaindbMultichannelInfoRepository.class);

    private final CommonMaindbMultichannelInfo TABLE;
    private final MaindbGroup MAINDB_GROUP_TABLE;
    @Autowired
    private MaindbCustomInfoService maindbCustomInfoService;

    public MaindbMultichannelInfoRepository(String companyId) {
        super(new CommonMaindbMultichannelInfo(companyId), new CommonMaindbMultichannelInfo(companyId).SEQ, MaindbMultichannelInfoEntity.class);
        TABLE = new CommonMaindbMultichannelInfo(companyId);
        MAINDB_GROUP_TABLE = new MaindbGroup();
    }

    public void insert(String customId, String customName, MaindbCustomInfoFormRequest.ChannelForm channel) {
        final MaindbCustomInfoEntity customInfo = maindbCustomInfoService.getRepository().findOne(customId);

        final MaindbMultichannelInfoEntity maindbMultichannelInfoEntity1 = dsl.selectFrom(TABLE)
                .where(TABLE.MAINDB_CUSTOM_ID.eq(customId))
                .and(TABLE.CHANNEL_TYPE.eq(channel.getType().name())
                        .and(TABLE.CHANNEL_DATA.eq(channel.getValue())))
                .fetchOneInto(MaindbMultichannelInfoEntity.class);

        if (maindbMultichannelInfoEntity1 == null) {

            dsl.insertInto(TABLE)
                    .set(TABLE.MAINDB_CUSTOM_ID, customId)
                    .set(TABLE.MAINDB_CUSTOM_NAME, customName)
                    .set(TABLE.CHANNEL_TYPE, channel.getType().name())
                    .set(TABLE.CHANNEL_DATA, channel.getValue())
                    .set(TABLE.MAINDB_GROUP_ID, customInfo.getMaindbSysGroupId())
                    .set(TABLE.COMPANY_ID, getCompanyId())
                    .execute();
        } else {
            dsl.update(TABLE)
                    .set(TABLE.CHANNEL_TYPE, channel.getType().name())
                    .set(TABLE.CHANNEL_DATA, channel.getValue())
                    .where(TABLE.SEQ.eq(maindbMultichannelInfoEntity1.getSeq()))
                    .execute();
        }
    }

    public void updateCustomName(String customId, String customName) {
        dsl.update(TABLE)
                .set(TABLE.MAINDB_CUSTOM_NAME, customName)
                .where(TABLE.MAINDB_CUSTOM_ID.eq(customId))
                .execute();
    }

    public List<MaindbMultichannelInfoEntity> findAllByCustomIds(List<String> customIds) {
        return findAll(TABLE.MAINDB_CUSTOM_ID.in(customIds));
    }

    public void deleteByCustomId(String customId) {
        delete(TABLE.MAINDB_CUSTOM_ID.eq(customId));

    }

    public List<CommonMultiChannelInfoResponse> findAllByChannelData(String channel_data) {
        return dsl.selectFrom(TABLE)
                .where(compareCompanyId())
                .and(TABLE.CHANNEL_TYPE.eq("PHONE"))
                .and(TABLE.CHANNEL_DATA.eq(channel_data))
                .fetchInto(CommonMultiChannelInfoResponse.class);
    }

    public List<CustomMultichannelInfoResponse> findAllCustomInfo(String channel_data) {
        return dsl.select(MAINDB_GROUP_TABLE.NAME.as("groupName"))
                .select(TABLE.fields())
                .from(TABLE)
                .leftJoin(MAINDB_GROUP_TABLE)
                .on(TABLE.MAINDB_GROUP_ID.eq(MAINDB_GROUP_TABLE.SEQ))
                .where(compareCompanyId())
                .and(TABLE.CHANNEL_DATA.eq(channel_data))
                .fetchInto(CustomMultichannelInfoResponse.class);
    }

    public void deleteByTypeAndData(MultichannelChannelType channelType, String channelData) {
        dsl.deleteFrom(TABLE)
                .where(compareCompanyId())
                .and(TABLE.CHANNEL_TYPE.eq(channelType.getCode()))
                .and(TABLE.CHANNEL_DATA.eq(channelData))
                .execute();
    }
}
