package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.MaindbGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogResponse;
import kr.co.eicn.ippbx.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.model.enums.IsDupNeedYn;
import kr.co.eicn.ippbx.model.form.MaindbGroupFormRequest;
import kr.co.eicn.ippbx.model.form.MaindbGroupUpdateRequest;
import kr.co.eicn.ippbx.model.search.MaindbGroupSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.COMPANY_TREE;
import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.MAINDB_GROUP;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class MaindbGroupRepository extends EicnBaseRepository<MaindbGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MaindbGroup, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(MaindbGroupRepository.class);

    private final MaindbUploadRepository uploadRepository;
    private final CommonTypeRepository typeRepository;
    private final CommonFieldRepository fieldRepository;
    private final CompanyTreeRepository companyTreeRepository;

    MaindbGroupRepository(MaindbUploadRepository uploadRepository, CommonTypeRepository typeRepository, CommonFieldRepository fieldRepository, CompanyTreeRepository companyTreeRepository) {
        super(MAINDB_GROUP, MAINDB_GROUP.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MaindbGroup.class);
        this.uploadRepository = uploadRepository;
        this.typeRepository = typeRepository;
        this.fieldRepository = fieldRepository;
        this.companyTreeRepository = companyTreeRepository;
    }

    public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MaindbGroup> findAll(MaindbGroupSearchRequest search) {
        return super.findAll(conditions(search));
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MaindbGroup> pagination(MaindbGroupSearchRequest search) {
        return super.pagination(search, conditions(search), Collections.singletonList(MAINDB_GROUP.SEQ.asc()));
    }

    public Record insertOnGeneratedKey(MaindbGroupFormRequest form) {
        final CommonType commonType = typeRepository.findOne(form.getMaindbType());
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MaindbGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MaindbGroup();
        record.setName(form.getName());
        record.setMakeDate(new Timestamp(System.currentTimeMillis()));
        record.setIsDupUse(form.getIsDupUse());
        record.setResultType(form.getResultType());
        record.setCompanyId(getCompanyId());
        record.setInfo(form.getInfo());

        if (commonType.getKind().equals(CommonTypeKind.MAIN_DB.getCode()) && commonType.getStatus().equals("U"))
            record.setMaindbType(form.getMaindbType());
        else
            throw new IllegalArgumentException("고객정보 유형에 맞지 않습니다.");

        if (form.getIsDupUse().equals(IsDupNeedYn.USE.getCode())) {
            record.setDupKeyKind(form.getDupKeyKind());
            record.setDupIsUpdate(form.getDupIsUpdate());
            record.setDupNeedField(form.getDupNeedField());
        }

        if (isNotEmpty(form.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
            if (companyTree != null) {
                record.setGroupCode(companyTree.getGroupCode());
                record.setGroupTreeName(companyTree.getGroupTreeName());
                record.setGroupLevel(companyTree.getGroupLevel());
            }
        }

        return super.insertOnGeneratedKey(record);
    }

    public void updateGroup(MaindbGroupUpdateRequest formRequest, Integer seq) {
        final CommonType commonType = typeRepository.findOne(formRequest.getMaindbType());
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MaindbGroup record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MaindbGroup();
        record.setName(formRequest.getName());
        record.setResultType(formRequest.getResultType());
        record.setCompanyId(getCompanyId());
        record.setInfo(formRequest.getInfo());

        if (commonType.getKind().equals(CommonTypeKind.MAIN_DB.getCode()) && commonType.getStatus().equals("U"))
            record.setMaindbType(formRequest.getMaindbType());
        else
            throw new IllegalArgumentException("고객정보 유형에 맞지 않습니다.");

        if (isNotEmpty(formRequest.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOne(COMPANY_TREE.GROUP_CODE.eq(formRequest.getGroupCode()));
            if (companyTree != null) {
                record.setGroupCode(companyTree.getGroupCode());
                record.setGroupTreeName(companyTree.getGroupTreeName());
                record.setGroupLevel(companyTree.getGroupLevel());
            }
        }
        updateByKey(record, seq);
    }

    private List<Condition> conditions(MaindbGroupSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if(search.getMaindbType() != null)
            conditions.add(MAINDB_GROUP.MAINDB_TYPE.eq(search.getMaindbType()));

        if(isNotEmpty(search.getName()))
            conditions.add(MAINDB_GROUP.NAME.like("%" + search.getName() + "%"));
        return conditions;
    }

    public GroupUploadLogResponse findOneByKey(Integer seq) {
        return dsl.select(MAINDB_GROUP.SEQ.as("seq"))
                .select(MAINDB_GROUP.NAME.as("name"))
                .select(MAINDB_GROUP.MAKE_DATE.as("makeDate"))
                .from(MAINDB_GROUP)
                .where(compareCompanyId())
                .and(MAINDB_GROUP.SEQ.eq(seq))
                .fetchOneInto(GroupUploadLogResponse.class);
    }
}
