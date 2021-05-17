package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonOnHunt extends PersonListSummary {
    private String queueName;
    private String queueNumber;
}
