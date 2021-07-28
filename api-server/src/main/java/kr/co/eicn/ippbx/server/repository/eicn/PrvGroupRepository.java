package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.PrvGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonMember;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.model.enums.CommonTypeKind;
import kr.co.eicn.ippbx.model.form.PrvGroupFormRequest;
import kr.co.eicn.ippbx.model.search.PrvGroupSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.COMMON_MEMBER;
import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.PRV_GROUP;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class PrvGroupRepository extends EicnBaseRepository<PrvGroup, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PrvGroup, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(PrvGroupRepository.class);

    private final CommonMemberRepository commonMemberRepository;
    private final CompanyTreeRepository companyTreeRepository;
    private final Number070Repository numberRepository;

    PrvGroupRepository(CommonMemberRepository commonMemberRepository, CompanyTreeRepository companyTreeRepository, Number070Repository numberRepository) {
        super(PRV_GROUP, PRV_GROUP.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PrvGroup.class);
        this.commonMemberRepository = commonMemberRepository;
        this.companyTreeRepository = companyTreeRepository;
        this.numberRepository = numberRepository;
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PrvGroup> pagination(PrvGroupSearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    public Record insertOnGeneratedKey(PrvGroupFormRequest form) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PrvGroup prvRecord = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PrvGroup();

        prvRecord.setName(form.getName());
        prvRecord.setPrvType(form.getPrvType());
        prvRecord.setResultType(form.getResultType());
        prvRecord.setInfo(form.getInfo());

        prvRecord.setDialTimeout(form.getDialTimeout());
        prvRecord.setCompanyId(getCompanyId());
        prvRecord.setMemberKind(form.getMemberKind());
        prvRecord.setCompanyId(getCompanyId());
        prvRecord.setRidKind(form.getRidKind());
        prvRecord.setBillingKind(form.getBillingKind());

        if (isNotEmpty(form.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
            if (companyTree != null) {
                prvRecord.setGroupCode(companyTree.getGroupCode());
                prvRecord.setGroupTreeName(companyTree.getGroupTreeName());
                prvRecord.setGroupLevel(companyTree.getGroupLevel());
            }
        }

        if (form.getRidKind().equals("CAMPAIGN")) {
            prvRecord.setRidData(form.getRidData());
        }

        if (!form.getBillingKind().equals("PBX")) {
            prvRecord.setBillingData(form.getBillingData());
        }

        if (form.getMemberKind().equals("FIELD")) {
            prvRecord.setMemberData(g.getUser().getId());
        } else {
            prvRecord.setMemberData(String.valueOf(form.getMemberDataList().size()));
        }

        final Record record = super.insertOnGeneratedKey(prvRecord);

        final CommonMember memberRecord = new CommonMember();
        memberRecord.setGroupId(record.getValue(PRV_GROUP.SEQ));
        memberRecord.setGroupKind(CommonTypeKind.PREVIEW.getCode());
        memberRecord.setReserveSeq(1);
        memberRecord.setCompanyId(getCompanyId());

        if (!form.getMemberKind().equals("FIELD")) {
            for (String memberId : form.getMemberDataList()) {
                final Integer nextSeq = commonMemberRepository.nextSEQ();
                memberRecord.setSeq(nextSeq);
                memberRecord.setUserid(memberId);

                commonMemberRepository.insert(memberRecord);
            }
        } else {
            final Integer nextSeq = commonMemberRepository.nextSEQ();
            memberRecord.setSeq(nextSeq);
            memberRecord.setUserid(g.getUser().getId());

            commonMemberRepository.insert(memberRecord);
        }
        return record;
    }

    public void updateByKeyWithCommonMember(PrvGroupFormRequest form, Integer seq) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PrvGroup prvRecord = findOneIfNullThrow(seq);

        prvRecord.setName(form.getName());
        prvRecord.setPrvType(form.getPrvType());
        prvRecord.setResultType(form.getResultType());
        prvRecord.setInfo(form.getInfo());

        prvRecord.setDialTimeout(form.getDialTimeout());
        prvRecord.setCompanyId(getCompanyId());
        prvRecord.setMemberKind(form.getMemberKind());
        prvRecord.setCompanyId(getCompanyId());
        prvRecord.setMemberKind(form.getMemberKind());
        prvRecord.setRidKind(form.getRidKind());
        prvRecord.setBillingKind(form.getBillingKind());

        if (isNotEmpty(form.getGroupCode())) {
            final CompanyTree companyTree = companyTreeRepository.findOneByGroupCode(form.getGroupCode());
            if (companyTree != null) {
                prvRecord.setGroupCode(companyTree.getGroupCode());
                prvRecord.setGroupTreeName(companyTree.getGroupTreeName());
                prvRecord.setGroupLevel(companyTree.getGroupLevel());
            }
        }

        if (form.getRidKind().equals("CAMPAIGN")) {
            prvRecord.setRidData(form.getRidData());
        }

        if (!form.getBillingKind().equals("PBX")) {
            prvRecord.setBillingData(form.getBillingData());
        }

        if (form.getMemberKind().equals("FIELD")) {
            prvRecord.setMemberData(g.getUser().getId());
        } else {
            prvRecord.setMemberData(String.valueOf(form.getMemberDataList().size()));
        }
        updateByKey(prvRecord, seq);

        commonMemberRepository.delete(COMMON_MEMBER.GROUP_ID.eq(seq).and(COMMON_MEMBER.GROUP_KIND.eq(CommonTypeKind.PREVIEW.getCode())));

        final CommonMember memberRecord = new CommonMember();
        memberRecord.setGroupId(prvRecord.getSeq());
        memberRecord.setGroupKind(CommonTypeKind.PREVIEW.getCode());
        memberRecord.setReserveSeq(1);
        memberRecord.setCompanyId(getCompanyId());

        if (!form.getMemberKind().equals("FIELD")) {
            for (String memberId : form.getMemberDataList()) {
                final Integer nextSeq = commonMemberRepository.nextSEQ();
                memberRecord.setSeq(nextSeq);
                memberRecord.setUserid(memberId);

                commonMemberRepository.insert(memberRecord);
            }
        } else {
            final Integer nextSeq = commonMemberRepository.nextSEQ();
            memberRecord.setSeq(nextSeq);
            memberRecord.setUserid(g.getUser().getId());

            commonMemberRepository.insert(memberRecord);
        }
    }

    public void deleteWithCommonMember(Integer seq) {
        deleteOnIfNullThrow(seq);

        commonMemberRepository.delete(COMMON_MEMBER.GROUP_ID.eq(seq).and(COMMON_MEMBER.GROUP_KIND.eq(CommonTypeKind.PREVIEW.getCode())));
    }

    private List<Condition> conditions(PrvGroupSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (search.getPrvType() != null)
            conditions.add(PRV_GROUP.PRV_TYPE.eq(search.getPrvType()));

        if (isNotEmpty(search.getName()))
            conditions.add(PRV_GROUP.NAME.like("%" + search.getName() + "%"));

        return conditions;
    }
}
