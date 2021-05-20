package kr.co.eicn.ippbx.model.entity.eicn;

import lombok.Data;

@Data
public class MemberStatusOfHunt {
    private String name;
    private Integer paused;
    private Integer count;
}
