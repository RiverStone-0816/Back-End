package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoStatus;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResultCustomInfoFormRequest extends BaseForm {

    private Integer seq;

    private Integer resultType;
    private String callType;
    private String uniqueId;
    private Integer callUnique;
    private String customNumber;
    private String clickKey;
    private String fromOrg;
    private String groupKind;
    private Integer groupId;
    private String hangupCause;
    private String hangupMsg;
    private Integer billSec;
    private String customId;
    private String userIdTr;
    private Integer maindbType;
    private Integer vocGroup;

    private Date date_1;
    private Date date_2;
    private Date date_3;
    private Date day_1;
    private Date day_2;
    private Date day_3;
    private Timestamp datetime_1;
    private Timestamp datetime_2;
    private Timestamp datetime_3;
    private Integer int_1;
    private Integer int_2;
    private Integer int_3;
    private Integer int_4;
    private Integer int_5;
    private String number_1;
    private String number_2;
    private String number_3;
    private String number_4;
    private String number_5;
    private String string_1;
    private String string_2;
    private String string_3;
    private String string_4;
    private String string_5;
    private String string_6;
    private String string_7;
    private String string_8;
    private String string_9;
    private String string_10;
    private String string_11;
    private String string_12;
    private String string_13;
    private String string_14;
    private String string_15;
    private String string_16;
    private String string_17;
    private String string_18;
    private String string_19;
    private String string_20;
    private String code_1;
    private String code_2;
    private String code_3;
    private String code_4;
    private String code_5;
    private String code_6;
    private String code_7;
    private String code_8;
    private String code_9;
    private String code_10;
    private String img_1;
    private String img_2;
    private String img_3;
    private String multicode_1;
    private String multicode_2;
    private String multicode_3;
    private String concode_1;
    private String concode_2;
    private String concode_3;
    private String cscode_1;
    private String cscode_2;
    private String cscode_3;

    private List<Integer> todoSequences = new ArrayList<>();
    private TodoListTodoStatus todoStatus;

    private String roomName;
}
