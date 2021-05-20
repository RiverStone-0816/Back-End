package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.VocKeymap;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.VocKeymap.VOC_KEYMAP;

@Getter
@Repository
public class VOCKeyMapRepository extends EicnBaseRepository<VocKeymap, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocKeymap, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(VOCKeyMapRepository.class);

	public VOCKeyMapRepository() {
		super(VOC_KEYMAP, VOC_KEYMAP.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocKeymap.class);
	}
}
