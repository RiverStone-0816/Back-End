package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.MoContent;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MoContent.MO_CONTENT;

@Getter
@Repository
public class MoContentRepository extends EicnBaseRepository<MoContent, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MoContent, Long>{
    private final Logger logger = LoggerFactory.getLogger(MoContentRepository.class);

    public MoContentRepository() {
        super(MO_CONTENT, MO_CONTENT.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MoContent.class);
    }
}
