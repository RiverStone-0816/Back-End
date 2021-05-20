package kr.co.eicn.ippbx.model.dto.eicn.search;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchPhoneInfoResponse extends PhoneInfo {
}
