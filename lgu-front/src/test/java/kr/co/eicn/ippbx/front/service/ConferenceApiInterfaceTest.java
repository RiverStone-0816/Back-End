package kr.co.eicn.ippbx.front.service;

import kr.co.eicn.ippbx.front.service.api.conference.ConferenceApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.ConfInfoMinutesResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ConfInfoSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ConfRoomSummaryResponse;
import kr.co.eicn.ippbx.model.form.ConfInfoFormRequest;
import kr.co.eicn.ippbx.model.form.ConfInfoMinutesSaveFormRequest;
import kr.co.eicn.ippbx.model.form.ConfMemberOutPersonFormRequest;
import kr.co.eicn.ippbx.model.search.ConfInfoSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConferenceApiInterfaceTest extends AbstractApiInterfaceTest {
    private static final Logger logger = LoggerFactory.getLogger(ConferenceApiInterfaceTest.class);

    private ConferenceApiInterface apiInterface;

    @Before
    public void setup() {
        apiInterface = getInterface(ConferenceApiInterface.class);
    }

    @Test
    public void dummy() {
    }

    // @Test
    public void checkWholeListSize() throws IOException, ResultFailException {
        final List<ConfInfoSummaryResponse> wholeList = apiInterface.list(new ConfInfoSearchRequest());
        logger.info("wholeList size: " + wholeList.size());

        for (ConfRoomSummaryResponse room : apiInterface.getConfRoomList()) {
            final ConfInfoSearchRequest search = new ConfInfoSearchRequest();
            search.setRoomNumber(room.getRoomNumber());
            final List<ConfInfoSummaryResponse> listByRoomNumber = apiInterface.list(search);
            logger.info("list size by roomNumber(" + search.getRoomNumber() + "): " + listByRoomNumber.size());
        }
    }

    // @Test
    public void checkWholeListSizeByMonth() throws IOException, ResultFailException {
        final ConfInfoSearchRequest search = new ConfInfoSearchRequest();
        search.setReserveYear(2020);
        final List<ConfInfoSummaryResponse> wholeList = apiInterface.list(search);
        logger.info("wholeList size: " + wholeList.size());

        for (int i = 1; i <= 12; i++) {
            search.setReserveMonth(i);
            final List<ConfInfoSummaryResponse> listByRoomNumber = apiInterface.list(search);
            logger.info("list size by month(" + search.getReserveMonth() + "): " + listByRoomNumber.size());
        }
    }

    // @Test
    public void postMinutes() throws IOException, ResultFailException {
        final String MINUTES_CONTENTS = "테스트 회의록";

        final List<ConfRoomSummaryResponse> roomList = apiInterface.getConfRoomList();
        if (roomList.size() == 0)
            return;

        final ConfInfoFormRequest form = new ConfInfoFormRequest();
        form.setConfName("테스트 회의");
        form.setReserveFromTime(60 * 9);
        form.setReserveToTime(60 * 11);
        form.setIsRecord("N");
        form.setConfCid("0000");
        form.setIsMachineDetect("N");
        final Set<ConfMemberOutPersonFormRequest> members = new HashSet<>();
        UrlUtils.makeParamMap(
                "참석자1", "01011112222",
                "참석자2", "01011112223",
                "참석자3", "01011112224",
                "참석자4", "01011112225").forEach((name, number) -> {
            final ConfMemberOutPersonFormRequest person = new ConfMemberOutPersonFormRequest();
            person.setMemberName(name);
            person.setMemberNumber(number);
            members.add(person);
        });
        form.setConfOutMembers(members);
        form.setRoomNumber(roomList.get(0).getRoomNumber());
        form.setReserveDate(new Date(System.currentTimeMillis()));

        final Integer id = apiInterface.post(form);

        final ConfInfoMinutesResponse preMinutes = apiInterface.minutes(id);
        logger.info("preMinutes: {}", preMinutes.getMinutesText());

        final ConfInfoMinutesSaveFormRequest minutesSaveFormRequest = new ConfInfoMinutesSaveFormRequest();
        minutesSaveFormRequest.setConfId(id);
        minutesSaveFormRequest.setConfMinute(MINUTES_CONTENTS);
        apiInterface.minutesSave(minutesSaveFormRequest);
        logger.info("MINUTES_CONTENTS: {}", MINUTES_CONTENTS);

        final ConfInfoMinutesResponse postMinutes = apiInterface.minutes(id);
        logger.info("postMinutes: {}", postMinutes.getMinutesText());

        apiInterface.delete(id);

        assert StringUtils.isNotEmpty(postMinutes.getMinutesText()) && postMinutes.getMinutesText().startsWith(MINUTES_CONTENTS);
    }

}
