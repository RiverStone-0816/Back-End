package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.ConfInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfMember;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.records.ConfInfoRecord;
import kr.co.eicn.ippbx.server.model.dto.eicn.ConfInfoSummaryResponse;
import kr.co.eicn.ippbx.server.model.form.ConfInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.ConfMemberOutPersonFormRequest;
import kr.co.eicn.ippbx.server.model.search.ConfInfoSearchRequest;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.CONF_INFO;
import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.CONF_MEMBER;

@Getter
@Repository
public class ConfInfoRepository extends EicnBaseRepository<ConfInfo, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfInfo, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ConfInfoRepository.class);

    private final ConfMemberRepository confMemberRepository;
    private final Number070Repository numberRepository;

    ConfInfoRepository(ConfMemberRepository confMemberRepository, Number070Repository numberRepository) {
        super(CONF_INFO, CONF_INFO.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfInfo.class);

        this.confMemberRepository = confMemberRepository;
        this.numberRepository = numberRepository;

        orderByFields.add(CONF_INFO.RESERVE_FROMTIME.asc());

    }

    public Record insertOnGeneratedKey(ConfInfoFormRequest form) {
        int peerMemberSize = 0;
        if(form.getConfPeerMembers() != null && form.getConfPeerMembers().size() > 0) {
            peerMemberSize = form.getConfPeerMembers().size();
        }
        int outMemberSize = 0;
        if(form.getConfOutMembers() != null && form.getConfOutMembers().size() > 0) {
            outMemberSize = form.getConfOutMembers().size();
        }

        if((peerMemberSize + outMemberSize) < 2) {
            throw new IllegalArgumentException("참여자가 2명 이상이어야 합니다.");
        }

        final Number_070 number = numberRepository.findOneIfNullThrow(getCompanyId(), form.getRoomNumber());

        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfInfo confInfo = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfInfo();

        confInfo.setConfName(form.getConfName());
        confInfo.setReserveFromtime(form.getReserveFromTime());
        confInfo.setReserveTotime(form.getReserveToTime());
        confInfo.setConfSound(form.getConfSound());
        confInfo.setIsRecord(form.getIsRecord());
        confInfo.setConfCid(form.getConfCid());
        confInfo.setIsMachineDetect(form.getIsMachineDetect());
        confInfo.setConfPasswd(form.getConfPasswd());
        confInfo.setStatus("A");
/*        confInfo.setInputDate(Timestamp.valueOf(LocalDateTime.now()));
        confInfo.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        confInfo.setEndDate(Timestamp.valueOf(LocalDateTime.now()));*/
        confInfo.setRoomNumber(form.getRoomNumber());
        confInfo.setConfType("");
        confInfo.setReserveDate(form.getReserveDate());
        confInfo.setReserveAdmin(g.getUser().getId());
        confInfo.setTotalMemberCnt((byte) 0);
        confInfo.setAttendedMemberCnt((byte) 0);
        confInfo.setHost(number.getHost());
        confInfo.setCompanyId(getCompanyId());

        ConfInfoRecord record = dsl.insertInto(CONF_INFO)
                .set(CONF_INFO.INPUT_DATE, DSL.now())
                .set(CONF_INFO.START_DATE, DSL.now())
                .set(CONF_INFO.END_DATE, DSL.now())

                .set(dsl.newRecord(CONF_INFO, confInfo))
                .returning()
                .fetchOne();

        final ConfMember confMember = new ConfMember();

        confMember.setConfKey(record.getValue(CONF_INFO.SEQ));
        confMember.setRoomNumber(form.getRoomNumber());
        confMember.setIsChef("");
        confMember.setIsAttended("");
        confMember.setCompanyId(getCompanyId());

        if(form.getConfPeerMembers() != null && form.getConfPeerMembers().size() > 0) {
            for(String memberNumber : form.getConfPeerMembers()) {
                confMember.setMemberNumber(memberNumber);
                confMember.setMemberName("");
                confMember.setMemberType("I");
                confMemberRepository.insert(confMember);
            }
        }
        if(form.getConfOutMembers() != null && form.getConfOutMembers().size() > 0) {
            for(ConfMemberOutPersonFormRequest memberInfo : form.getConfOutMembers()) {
                confMember.setMemberNumber(memberInfo.getMemberNumber());
                confMember.setMemberName(memberInfo.getMemberName()+"("+memberInfo.getMemberNumber()+")");
                confMember.setMemberType("O");
                confMemberRepository.insert(confMember);
            }
        }

        return record;
    }

    public void confInfoUpdate(ConfInfoFormRequest form, Integer seq) {

        int peerMemberSize = 0;
        if(form.getConfPeerMembers() != null && form.getConfPeerMembers().size() > 0) {
            peerMemberSize = form.getConfPeerMembers().size();
        }
        int outMemberSize = 0;
        if(form.getConfOutMembers() != null && form.getConfOutMembers().size() > 0) {
            outMemberSize = form.getConfOutMembers().size();
        }

        if((form.getConfPeerMembers().size() + form.getConfOutMembers().size()) < 2) {
            throw new IllegalArgumentException("참여자가 2명 이상이어야 합니다.");
        }

//        final Number_070 number = numberRepository.findOneIfNullThrow(getCompanyId(), form.getRoomNumber());

        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfInfo record = findOneIfNullThrow(seq);

        record.setConfName(form.getConfName());
        record.setReserveFromtime(form.getReserveFromTime());
        record.setReserveTotime(form.getReserveToTime());
        record.setConfSound(form.getConfSound());
        record.setIsRecord(form.getIsRecord());
        record.setConfCid(form.getConfCid());
        record.setIsMachineDetect(form.getIsMachineDetect());
        record.setConfPasswd(form.getConfPasswd());
//        record.setStatus("A");
/*        record.setInputDate(Timestamp.valueOf(LocalDateTime.now()));
        record.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        record.setEndDate(Timestamp.valueOf(LocalDateTime.now()));*/
        record.setRoomNumber(form.getRoomNumber());
//        record.setConfType("");
        record.setReserveDate(form.getReserveDate());
        record.setReserveAdmin(g.getUser().getId());
//        record.setTotalMemberCnt((byte) 0);
//        record.setAttendedMemberCnt((byte) 0);
//        record.setHost(number.getHost());
//        record.setCompanyId(getCompanyId());

        super.updateByKey(record, seq);

        confMemberRepository.delete(CONF_MEMBER.CONF_KEY.eq(seq));

        final ConfMember confMember = new ConfMember();

        confMember.setConfKey(seq);
        confMember.setRoomNumber(form.getRoomNumber());
        confMember.setIsChef("");
        confMember.setIsAttended("");
        confMember.setCompanyId(getCompanyId());

        if(form.getConfPeerMembers() != null && form.getConfPeerMembers().size() > 0) {
            for(String memberNumber : form.getConfPeerMembers()) {
                confMember.setMemberNumber(memberNumber);
                confMember.setMemberName("");
                confMember.setMemberType("I");
                confMemberRepository.insert(confMember);
            }
        }
        if(form.getConfOutMembers() != null && form.getConfOutMembers().size() > 0) {
            for(ConfMemberOutPersonFormRequest memberInfo : form.getConfOutMembers()) {
                confMember.setMemberNumber(memberInfo.getMemberNumber());
                confMember.setMemberName(memberInfo.getMemberName()+"("+memberInfo.getMemberNumber()+")");
                confMember.setMemberType("O");
                confMemberRepository.insert(confMember);
            }
        }

    }

    public void confInfoDelete(Integer seq) {
        deleteOnIfNullThrow(seq);

        confMemberRepository.delete(CONF_MEMBER.CONF_KEY.eq(seq));

    }



    public List<ConfInfoSummaryResponse> search(ConfInfoSearchRequest search) {
        return findAll(searchConditions(search)).stream()
                .map(e -> modelMapper.map(e, ConfInfoSummaryResponse.class))
                .collect(Collectors.toList());
    }

    private List<Condition> searchConditions(ConfInfoSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if(search.getRoomNumber() != null)
            conditions.add(CONF_INFO.ROOM_NUMBER.eq(search.getRoomNumber()));

        if(search.getReserveYear() != null && search.getReserveMonth() != null) {
            String reserveMonth = String.valueOf(search.getReserveMonth());
            if(search.getReserveMonth() < 10) {
                reserveMonth = "0"+reserveMonth;
            }
            conditions.add(CONF_INFO.RESERVE_DATE.like(search.getReserveYear() + "-" + reserveMonth + "%"));
        }

        return conditions;
    }
}
