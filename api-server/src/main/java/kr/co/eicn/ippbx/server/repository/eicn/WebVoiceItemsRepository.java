package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.WebvoiceItems;
import kr.co.eicn.ippbx.server.model.enums.ContextType;
import kr.co.eicn.ippbx.server.model.enums.IsWebVoiceYn;
import kr.co.eicn.ippbx.server.model.enums.WebVoiceItemType;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.WebvoiceItems.WEBVOICE_ITEMS;

@Getter
@Repository
public class WebVoiceItemsRepository extends EicnBaseRepository<WebvoiceItems, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceItems, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WebVoiceItemsRepository.class);
    private String context;
    private Integer ivrCode;
    private String areaType;
    private String itemType;

    public WebVoiceItemsRepository() {
        super(WEBVOICE_ITEMS, WEBVOICE_ITEMS.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceItems.class);
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceItems> findAllByContext(String context) {
        return findAll(WEBVOICE_ITEMS.CONTEXT.eq(context));
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceItems> findAllByIvrCode(Integer ivrCode) {
        return findAll(WEBVOICE_ITEMS.IVR_CODE.eq(ivrCode));
    }

    public void setItem(String context, Integer ivrCode) {
        this.context = context;
        this.ivrCode = ivrCode;
    }

    public void insertItem(String areaType, String itemType, String itemName, Integer sequence) {
        insertItem(areaType, itemType, itemName, "", "", sequence);
    }

    public void insertItem(String areaType, String itemType, String itemName, String value, String isView, Integer sequence) {
        this.areaType = areaType;
        this.itemType = itemType;

        dsl.insertInto(WEBVOICE_ITEMS)
                .set(WEBVOICE_ITEMS.CONTEXT, Objects.equals(0, ivrCode) ? context : ContextType.INBOUND.getCode())
                .set(WEBVOICE_ITEMS.IVR_CODE, ivrCode)
                .set(WEBVOICE_ITEMS.AREA_TYPE, areaType)
                .set(WEBVOICE_ITEMS.ITEM_TYPE, itemType)
                .set(WEBVOICE_ITEMS.ITEM_VALUE, (WebVoiceItemType.DTMF.getCode().equals(itemType)) ? value : "")
                .set(WEBVOICE_ITEMS.INPUT_MAX_LEN, (WebVoiceItemType.INPUT.getCode().equals(itemType)) ? Integer.parseInt(value) : 0)
                .set(WEBVOICE_ITEMS.ITEM_NAME, itemName)
                .set(WEBVOICE_ITEMS.IS_VIEW, StringUtils.isNotEmpty(isView) ? isView : IsWebVoiceYn.WEB_VOICE_Y.getCode())
                .set(WEBVOICE_ITEMS.SEQUENCE, sequence)
                .set(WEBVOICE_ITEMS.COMPANY_ID, getCompanyId())
                .execute();
    }

    public void deleteByIvrCodeContext(Integer code, String context) {
        dsl.deleteFrom(WEBVOICE_ITEMS)
                .where(compareCompanyId())
                .and(WEBVOICE_ITEMS.IVR_CODE.eq(code))
                .and(WEBVOICE_ITEMS.CONTEXT.eq(context))
                .execute();
    }
}
