package kr.co.eicn.ippbx.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.model.Constants;
import kr.co.eicn.ippbx.util.CodeHasable;
import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkRoomSearchRequest extends PageForm {
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date startDate = new Date(System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000);
    @PageQueryable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
    private Date endDate = new Date(System.currentTimeMillis());
    @PageQueryable
    private String id;          //상담원 id
    /**
     * @see kr.co.eicn.ippbx.model.enums.RoomStatus
     * */
    @PageQueryable
    private String roomStatus = "C";  //대화방상태
    @PageQueryable
    private String senderKey;   //상담톡서비스
    @PageQueryable
    private String roomName;    //대화방명
    @PageQueryable
    private String sorts = Sorts.START_TIME.getCode();         //정렬데이터
    @PageQueryable
    private String sequence = "desc";    //정렬순서 desc | asc

    /**
     *  START_TIME : 시작시간, LAST_TIME : 마지막메시지시간
     * */
    public enum Sorts implements CodeHasable<String> {
        START_TIME("room_start_time"), LAST_TIME("room_last_time");

        private String code;

        Sorts(String field) { this.code = field;}

        public static Sorts of(String value) {
            for (Sorts type : Sorts.values()) {
                if (Objects.equals(type.code, value))
                    return type;
            }

            return null;
        }

        public String getCode() {
            return code;
        }
    }
}
