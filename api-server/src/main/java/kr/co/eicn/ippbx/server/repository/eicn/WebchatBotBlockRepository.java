package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatBotBlock;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotBlockSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.enums.Bool;
import kr.co.eicn.ippbx.model.form.WebchatBotBlockFormRequest;
import lombok.Getter;
import lombok.val;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.*;

@Getter
@Repository
public class WebchatBotBlockRepository extends EicnBaseRepository<WebchatBotBlock, WebchatBotInfoResponse.BlockInfo, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WebchatBotBlockRepository.class);

    public WebchatBotBlockRepository() {
        super(WEBCHAT_BOT_BLOCK, WEBCHAT_BOT_BLOCK.ID, WebchatBotInfoResponse.BlockInfo.class);
        addField(WEBCHAT_BOT_BLOCK);
        addField(WEBCHAT_BOT_TREE.PARENT_BTN_ID);
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .join(WEBCHAT_BOT_TREE).on(WEBCHAT_BOT_TREE.BLOCK_ID.eq(WEBCHAT_BOT_BLOCK.ID))
                .where();
    }

    @Override
    protected RecordMapper<Record, WebchatBotInfoResponse.BlockInfo> getMapper() {
        return record -> {
            val entity = record.into(WEBCHAT_BOT_BLOCK).into(WebchatBotInfoResponse.BlockInfo.class);
            entity.setIsTemplateEnable(Objects.equals("Y", record.getValue(WEBCHAT_BOT_BLOCK.IS_TPL_ENABLE)));
            entity.setParentButtonId(record.getValue(WEBCHAT_BOT_TREE.PARENT_BTN_ID));
            return entity;
        };
    }

    public List<WebchatBotBlockSummaryResponse> getAllTemplateBlockList() {
        return dsl.select(WEBCHAT_BOT_BLOCK.ID.as("block_id"), WEBCHAT_BOT_BLOCK.NAME.as("block_name"),
                WEBCHAT_BOT_INFO.ID.as("bot_id"), WEBCHAT_BOT_INFO.NAME.as("bot_name"))
                .from(WEBCHAT_BOT_BLOCK)
                .join(WEBCHAT_BOT_TREE)
                .on(WEBCHAT_BOT_BLOCK.ID.eq(WEBCHAT_BOT_TREE.BLOCK_ID))
                .join(WEBCHAT_BOT_INFO)
                .on(WEBCHAT_BOT_INFO.ID.eq(WEBCHAT_BOT_TREE.CHATBOT_ID))
                .where(compareCompanyId())
                .and(WEBCHAT_BOT_BLOCK.IS_TPL_ENABLE.eq(Bool.Y.name()))
                .fetchInto(WebchatBotBlockSummaryResponse.class);
    }

    public Integer insert(WebchatBotBlockFormRequest request) {
        return dsl.insertInto(WEBCHAT_BOT_BLOCK)
                .set(WEBCHAT_BOT_BLOCK.POS_X, request.getPosX())
                .set(WEBCHAT_BOT_BLOCK.POS_Y, request.getPosY())
                .set(WEBCHAT_BOT_BLOCK.NAME, request.getName())
                .set(WEBCHAT_BOT_BLOCK.KEYWORD, request.getKeyword())
                .set(WEBCHAT_BOT_BLOCK.IS_TPL_ENABLE, Objects.equals(request.getIsTemplateEnable(), true) ? "Y" : "N")
                .set(WEBCHAT_BOT_BLOCK.COMPANY_ID, getCompanyId())
                .returning()
                .fetchOne()
                .value1();
    }

    public void deleteByBlockIdList(List<Integer> blockIdList) {
        dsl.deleteFrom(WEBCHAT_BOT_BLOCK)
                .where(WEBCHAT_BOT_BLOCK.ID.in(blockIdList))
                .execute();
    }

    public List<WebchatBotInfoResponse.BlockInfo> findBlockInfoByIdInBlockIdList(List<Integer> blockIdList) {
        return findAll(WEBCHAT_BOT_BLOCK.ID.in(blockIdList));
    }
}
