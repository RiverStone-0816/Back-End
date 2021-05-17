package kr.co.eicn.ippbx.server.model.entity.eicn;

import lombok.Data;

@Data
public class CenterMemberStatusCountEntity {
    private Integer waitCount = 0;
    private Integer inCallingCount = 0;
    private Integer outCallingCount = 0;
    private Integer postProcessCount = 0;
    private Integer etcCount = 0;
    private Integer workingPerson = 0;
    private Integer loginCount = 0;
    private Integer logoutCount = 0;
}
