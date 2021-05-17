package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.ConfRoom;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.model.form.ConfRoomFormRequest;
import kr.co.eicn.ippbx.server.model.search.ConfRoomSearchRequest;
import kr.co.eicn.ippbx.server.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ConfRoom.CONF_ROOM;

@Getter
@Repository
public class ConfRoomRepository extends EicnBaseRepository<ConfRoom, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfRoom, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ConfRoomRepository.class);

    private final CompanyTreeRepository companyTreeRepository;

    public ConfRoomRepository(CompanyTreeRepository companyTreeRepository) {
        super(CONF_ROOM, CONF_ROOM.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfRoom.class);
        this.companyTreeRepository = companyTreeRepository;
        orderByFields.add(CONF_ROOM.SEQ.desc());
    }

    public Pagination<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfRoom> pagination(ConfRoomSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(ConfRoomSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        return conditions;
    }


    public void insert(ConfRoomFormRequest form) {

        final CompanyTree companyTree = companyTreeRepository.findOneGroupCodeIfNullThrow(form.getGroupCode());

        kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfRoom record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfRoom();

        ReflectionUtils.copy(record, form);

        record.setGroupCode(companyTree.getGroupCode());
        record.setGroupTreeName(companyTree.getGroupTreeName());
        record.setGroupLevel(companyTree.getGroupLevel());

        record.setCompanyId(getCompanyId());

        super.insert(record);
    }

    public void updateByKey(ConfRoomFormRequest form, @PathVariable Integer seq) {

        final CompanyTree companyTree = companyTreeRepository.findOneGroupCodeIfNullThrow(form.getGroupCode());

        kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfRoom record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfRoom();

        ReflectionUtils.copy(record, form);

        record.setGroupCode(companyTree.getGroupCode());
        record.setGroupTreeName(companyTree.getGroupTreeName());
        record.setGroupLevel(companyTree.getGroupLevel());

        super.updateByKey(record, seq);
    }
}
