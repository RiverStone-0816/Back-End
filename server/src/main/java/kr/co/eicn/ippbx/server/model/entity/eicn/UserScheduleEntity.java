package kr.co.eicn.ippbx.server.model.entity.eicn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.UserSchedule;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserScheduleEntity extends UserSchedule {
    private String userName;

    @JsonIgnore
    public boolean isWholeDay() {
        final Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(getStart().getTime());
        if (calendar.get(Calendar.HOUR_OF_DAY) != 0)
            return false;
        if (calendar.get(Calendar.MINUTE) != 0)
            return false;

        calendar.setTimeInMillis(getEnd().getTime());
        if (calendar.get(Calendar.HOUR_OF_DAY) != 23)
            return false;
        return calendar.get(Calendar.MINUTE) == 59;
    }
}
