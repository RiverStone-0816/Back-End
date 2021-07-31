package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ScreenConfig;
import kr.co.eicn.ippbx.model.entity.eicn.ScreenConfigEntity;
import kr.co.eicn.ippbx.model.form.ScreenConfigFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.SCREEN_CONFIG;

@Getter
@Repository
public class ScreenConfigRepository extends EicnBaseRepository<ScreenConfig, ScreenConfigEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ScreenConfigRepository.class);

    public ScreenConfigRepository() {
        super(SCREEN_CONFIG, SCREEN_CONFIG.SEQ, ScreenConfigEntity.class);
    }

    public Integer insertOnGeneratedKey(ScreenConfigFormRequest form) {
        final ScreenConfigEntity record = new ScreenConfigEntity();

        record.setName(form.getName());
        record.setLookAndFeel(form.getLookAndFeel());
        record.setExpressionType(form.getExpressionType());
        record.setShowSlidingText(form.getShowSlidingText());
        record.setSlidingText(form.getSlidingText());
        record.setCompanyId(getCompanyId());
//        if(Objects.nonNull(form.getSlidingSec()))
//            record.setSlidingSec(form.getSlidingSec());

        return super.insertOnGeneratedKey(record).get(SCREEN_CONFIG.SEQ);
    }

    public void update(Integer seq, ScreenConfigFormRequest form) {
        final ScreenConfigEntity record = new ScreenConfigEntity();

        record.setName(form.getName());
        record.setLookAndFeel(form.getLookAndFeel());
        record.setExpressionType(form.getExpressionType());
        record.setShowSlidingText(form.getShowSlidingText());
        record.setSlidingText(form.getSlidingText());
        record.setCompanyId(getCompanyId());
//        if(Objects.nonNull(form.getSlidingSec()))
//            record.setSlidingSec(form.getSlidingSec());

        super.updateByKey(record, seq);
    }
}
