package kr.co.eicn.ippbx.model.entity.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMemoMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemoMsgEntity extends CommonMemoMsg {
    private String sendUserName;
    private List<String> receiveUserNames = new ArrayList<>();
    private List<UserInfo> readReceiveUserInfos = new ArrayList<>();
    private List<UserInfo> unreadReceiveUserInfos = new ArrayList<>();

    @Data
    public static class UserInfo {
        private String userId;
        private String userName;
    }
}
