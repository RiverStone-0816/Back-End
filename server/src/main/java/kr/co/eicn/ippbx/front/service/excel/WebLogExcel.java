package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.RequestMessage;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.server.model.dto.eicn.LoginHistoryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.WebSecureHistoryResponse;
import kr.co.eicn.ippbx.server.model.search.LoginHistorySearchRequest;
import kr.co.eicn.ippbx.server.model.search.WebSecureHistorySearchRequest;

import java.util.List;

public class WebLogExcel extends AbstractExcel {
    private WebSecureHistorySearchRequest search;
    private List<WebSecureHistoryResponse> list;

    public WebLogExcel(WebSecureHistorySearchRequest search, List<WebSecureHistoryResponse> list) {
        this.search = search;
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "로그인 아이디","사용자명", "시간", "관련내선", "아이디유형", "실행아이피", "실행명", "상세실행명", "실행내용");

        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (WebSecureHistoryResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getUserId()),
                    niceFormat(e.getUserName()),
                    niceFormat(e.getInsertDate()),
                    niceFormat(e.getExtension()),
                    e.getIdType() != null ? message.getEnumText(e.getIdType()) : "",
                    niceFormat(e.getSecureIp()),
                    niceFormat(e.getActionId()),
                    niceFormat(e.getActionSubId()),
                    niceFormat(e.getActionData())
            );
        }
    }
}
