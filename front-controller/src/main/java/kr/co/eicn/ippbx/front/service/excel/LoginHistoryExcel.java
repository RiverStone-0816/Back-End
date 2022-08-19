package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.util.spring.RequestMessage;
import kr.co.eicn.ippbx.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.model.dto.eicn.LoginHistoryResponse;
import kr.co.eicn.ippbx.model.search.LoginHistorySearchRequest;

import java.util.List;

public class LoginHistoryExcel extends AbstractExcel {
    private LoginHistorySearchRequest search;
    private List<LoginHistoryResponse> list;

    public LoginHistoryExcel(LoginHistorySearchRequest search, List<LoginHistoryResponse> list) {
        this.search = search;
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "로그인명", "아이디", "로그인", "로그아웃", "로그인내선", "내선부서", "로그인 시 전화 끊은 후 상태", "로그아웃상태");

        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (LoginHistoryResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getUserName()),
                    niceFormat(e.getUserId()),
                    niceFormat(e.getLoginDate()),
                    niceFormat(e.getLogoutDate()),
                    niceFormat(e.getExtension()),
                    e.getCompanyTrees().stream().reduce((v, a) -> v + " > " + a).orElse(""),
                    e.getDialStatus() != null ? message.getEnumText(e.getDialStatus()) : "",
                    e.getLogoutStatus() != null ? message.getEnumText(e.getLogoutStatus()) : ""
            );
        }
    }
}
