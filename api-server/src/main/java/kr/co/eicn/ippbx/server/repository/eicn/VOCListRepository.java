package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.VocList;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.VocList.VOC_LIST;

@Getter
@Repository
public class VOCListRepository extends EicnBaseRepository<VocList, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.VocList, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(VOCListRepository.class);

	public VOCListRepository() {
		super(VOC_LIST, VOC_LIST.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.VocList.class);
	}
}
