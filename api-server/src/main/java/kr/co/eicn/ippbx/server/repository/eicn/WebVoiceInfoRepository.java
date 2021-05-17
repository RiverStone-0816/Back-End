package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.WebvoiceInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceItems;
import kr.co.eicn.ippbx.server.model.enums.ContextType;
import kr.co.eicn.ippbx.server.model.enums.WebVoiceAreaType;
import kr.co.eicn.ippbx.server.model.enums.WebVoiceInfoYn;
import kr.co.eicn.ippbx.server.model.enums.WebVoiceItemType;
import kr.co.eicn.ippbx.server.model.form.WebVoiceItemsDtmfFormRequest;
import kr.co.eicn.ippbx.server.model.form.WebVoiceItemsFormRequest;
import kr.co.eicn.ippbx.server.model.form.WebVoiceItemsInputFormRequest;
import kr.co.eicn.ippbx.server.service.IpccUrlConnection;
import lombok.Getter;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.WEBVOICE_INFO;

@Getter
@Repository
public class WebVoiceInfoRepository extends EicnBaseRepository<WebvoiceInfo, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceInfo, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(WebVoiceInfoRepository.class);

    private final WebVoiceItemsRepository webVoiceItemsRepository;
    private final CompanyServerRepository companyServerRepository;
    private final CompanyInfoRepository companyInfoRepository;

    public WebVoiceInfoRepository(WebVoiceItemsRepository webVoiceItemsRepository, CompanyServerRepository companyServerRepository, CompanyInfoRepository companyInfoRepository) {
        super(WEBVOICE_INFO, WEBVOICE_INFO.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceInfo.class);
        this.webVoiceItemsRepository = webVoiceItemsRepository;
        this.companyServerRepository = companyServerRepository;
        this.companyInfoRepository = companyInfoRepository;
    }

    public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceInfo> findAll(String context) {
        return findAll(WEBVOICE_INFO.CONTEXT.eq(context));
    }

    public kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceInfo findOneByContext(String context) {
        return findOne(WEBVOICE_INFO.CONTEXT.eq(context));
    }

    public kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceInfo findOneByIvrCode(Integer ivrCode) {
        return findOne(WEBVOICE_INFO.IVR_CODE.eq(ivrCode));
    }

    public void updateContextName(String context, String newContextName) {
        dsl.update(WEBVOICE_INFO)
                .set(WEBVOICE_INFO.CONTEXT, newContextName)
                .where(compareCompanyId())
                .and(WEBVOICE_INFO.CONTEXT.eq(context))
                .execute();
    }

    public void deleteContext(String context) {
        delete(WEBVOICE_INFO.CONTEXT.eq(context));
        webVoiceItemsRepository.deleteByIvrCodeContext(0, context);
    }

    public void deleteIvrCode(Integer ivrCode) {
        delete(WEBVOICE_INFO.IVR_CODE.eq(ivrCode));
        webVoiceItemsRepository.deleteByIvrCodeContext(ivrCode, "");
    }

    public void updateContext(String context, WebVoiceItemsFormRequest form) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceInfo entity = findOneByContext(context);
        if (entity == null) {
            form.setContext(context);
            form.setCompanyId(getCompanyId());
            form.setTailUse(WebVoiceInfoYn.UNUSED.getCode());

            final Record record = super.insertOnGeneratedKey(form);
            updateApply(record.getValue(WEBVOICE_INFO.SEQ), form);
        } else {
            super.updateByKey(form, entity.getSeq());
            updateApply(entity.getSeq(), form);
        }
    }

    public void updateIvrCode(Integer ivrCode, WebVoiceItemsFormRequest form) {
        if (findOne(WEBVOICE_INFO.IVR_CODE.eq(ivrCode)) == null) {
            form.setIvrCode(ivrCode);
            form.setContext(ContextType.INBOUND.getCode());
            form.setCompanyId(getCompanyId());
            form.setTailUse(WebVoiceInfoYn.UNUSED.getCode());

            final Record record = super.insertOnGeneratedKey(form);
            updateApply(record.getValue(WEBVOICE_INFO.SEQ), form);
        } else {
            final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceInfo entity = findOneByIvrCode(ivrCode);
            super.updateByKey(form, entity.getSeq());
            updateApply(entity.getSeq(), form);
        }
    }

    public void updateApply(Integer seq, WebVoiceItemsFormRequest form) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceInfo webVoiceInfo = findOne(seq);
        webVoiceItemsRepository.deleteByIvrCodeContext(webVoiceInfo.getIvrCode(), webVoiceInfo.getContext());
        int sequence = 0;
        webVoiceItemsRepository.setItem(webVoiceInfo.getContext(), webVoiceInfo.getIvrCode());
        if (form.getHeaderUse().equals(WebVoiceInfoYn.USE.getCode())) {
            webVoiceItemsRepository.insertItem(WebVoiceAreaType.HEADER.getCode(), WebVoiceItemType.HEADER.getCode(), form.getHeaderStr(), sequence++);
        }
        if (form.getTextareaUse().equals(WebVoiceInfoYn.USE.getCode())) {
            webVoiceItemsRepository.insertItem(WebVoiceAreaType.TEXT.getCode(), WebVoiceItemType.TEXT.getCode(), form.getTextStr(), sequence++);
        }

        int count = 0;
        if (Objects.nonNull(form.getTitles())) {
            for (WebVoiceItemsInputFormRequest input : form.getTitles()) {
                if (count > 3)
                    break;
                webVoiceItemsRepository.insertItem(WebVoiceAreaType.INPUT.getCode(), WebVoiceItemType.INPUT.getCode(), input.getInputTitle(), input.getMaxLen().toString(), input.getIsView(), sequence++);
                count++;
            }
        }

        count = 0;
        if (Objects.nonNull(form.getDtmfs())) {
            for (WebVoiceItemsDtmfFormRequest dtmf : form.getDtmfs()) {
                if (count > 9)
                    break;
                webVoiceItemsRepository.insertItem(WebVoiceAreaType.INPUT.getCode(), WebVoiceItemType.DTMF.getCode(), dtmf.getDtmfTitle(), dtmf.getDtmfValue(), dtmf.getIsView(), sequence++);
                count++;
            }
        }
    }

    public String apply(String serviceKey, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceInfo info, String context) {
        String fetch = "";
        fetch = insertRelay("info", serviceKey, info, "");
        if (!fetch.startsWith("NOK")) {
            if (fetch.length() > 3 && fetch.contains("TYPE")) {
                String relayUiType = fetch.substring(3);
                companyInfoRepository.updateTypeByContext(relayUiType);
            }
            List<WebvoiceItems> items = webVoiceItemsRepository.findAllByContext(context);
            for (WebvoiceItems item : items) {
                fetch = insertRelay("item", serviceKey, item, "");
                if (fetch.startsWith("NOK"))
                    break;
            }
        }

        return fetch;
    }

    public String insertRelay(String mode, String service_key, Object entity, String context_title) {
        String wvServer = "wv_server";
        if (companyServerRepository.findAllCompanyId(getCompanyId()) != null)
            wvServer = Objects.requireNonNull(companyServerRepository.findAllCompanyId(getCompanyId()).stream().filter(e -> e.getType().equals("V")).findFirst().orElse(null)).getHost();
        String urlString = "http://" + wvServer + "/ipcc/wv_mng/wv_ivr_insert.jsp";
        Map<String, Object> map = new HashMap<>();

        if (mode.equals("info")) {
            final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceInfo info = (kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.WebvoiceInfo) entity;

            map.put("service_key", service_key);
            map.put("ipcc_seq", info.getSeq());
            map.put("ivr_code", info.getIvrCode());
            map.put("context_title", context_title);
            map.put("context", info.getContext());
            map.put("ui_type", info.getUiType());
            map.put("header_use", info.getHeaderUse());
            map.put("control_use", info.getControlUse());
            map.put("tail_use", info.getTailUse());
            map.put("textarea_use", info.getTextareaUse());
            map.put("inputarea_use", info.getInputareaUse());
            map.put("ipcc_company_id", info.getCompanyId());
            map.put("banner_url", info.getBannerUrl());
            map.put("banner_img_file", info.getBannerImgFile());
        } else {
            WebvoiceItems item = (WebvoiceItems) entity;
            map.put("service_key", service_key);
            map.put("ipcc_seq", item.getSeq());
            map.put("ivr_code", item.getIvrCode());
            map.put("context", item.getContext());
            map.put("area_type", item.getAreaType());
            map.put("item_type", item.getItemType());
            map.put("item_name", item.getItemName());
            map.put("item_value", item.getItemValue());
            map.put("ivr_name", item.getIvrName());
            map.put("input_max_len", item.getInputMaxLen());
            map.put("sequence", item.getSequence());
            map.put("is_view", item.getIsView());
            map.put("ipcc_company_id", item.getCompanyId());
        }

        return IpccUrlConnection.fetch(urlString, map);
    }
}
