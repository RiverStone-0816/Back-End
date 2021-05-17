package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.ConGroup;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.model.form.ConGroupFormRequest;
import kr.co.eicn.ippbx.server.model.search.ConGroupSearchRequest;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.CON_GROUP;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class ConGroupRepository extends EicnBaseRepository<ConGroup, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConGroup, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ConGroupRepository.class);

    private final CompanyTreeRepository companyTreeRepository;

    public ConGroupRepository(CompanyTreeRepository companyTreeRepository) {
        super(CON_GROUP, CON_GROUP.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConGroup.class);
        this.companyTreeRepository = companyTreeRepository;
    }

    public Pagination<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConGroup> pagination(ConGroupSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public Record insertOnGeneratedKey(ConGroupFormRequest formRequest) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConGroup record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConGroup();
        record.setName(formRequest.getName());
        record.setConType(formRequest.getConType());
        record.setCompanyId(getCompanyId());
        record.setInfo(formRequest.getInfo());

        if (isNotEmpty(formRequest.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(formRequest.getGroupCode());
            if (companyTree != null ) {
                record.setGroupCode(companyTree.getGroupCode());
                record.setGroupTreeName(companyTree.getGroupTreeName());
                record.setGroupLevel(companyTree.getGroupLevel());
            }
        }

        return super.insertOnGeneratedKey(record);

    }

    public void updateGroup(ConGroupFormRequest formRequest, Integer seq) {
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConGroup record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConGroup();
        record.setName(formRequest.getName());
        record.setConType(formRequest.getConType());
        record.setCompanyId(getCompanyId());
        record.setInfo(formRequest.getInfo());

        if (isNotEmpty(formRequest.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(formRequest.getGroupCode());
            if (companyTree != null) {
                record.setGroupCode(companyTree.getGroupCode());
                record.setGroupTreeName(companyTree.getGroupTreeName());
                record.setGroupLevel(companyTree.getGroupLevel());
            }
        }

        updateByKey(record, seq);
    }

    private List<Condition> conditions(ConGroupSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if(search.getConType() != null)
            conditions.add(CON_GROUP.CON_TYPE.eq(search.getConType()));

        if(isNotEmpty(search.getName()))
            conditions.add(CON_GROUP.NAME.like("%" + search.getName() + "%"));

        return conditions;
    }

}
