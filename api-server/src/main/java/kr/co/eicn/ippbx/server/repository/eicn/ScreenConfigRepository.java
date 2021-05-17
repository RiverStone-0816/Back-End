package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.ScreenConfig;
import kr.co.eicn.ippbx.server.model.entity.eicn.ScreenConfigEntity;
import kr.co.eicn.ippbx.server.model.form.ScreenConfigFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.SCREEN_CONFIG;

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

        super.updateByKey(record, seq);
    }
}
