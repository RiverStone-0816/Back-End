package kr.co.eicn.ippbx.model.dto.customdb;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResultCustomInfoFromResponse extends BaseForm {
  private Integer seq;
  private String result_date;
  private String update_date;
  private String custom_number;
  private String userid;
  private String id_name;
}
