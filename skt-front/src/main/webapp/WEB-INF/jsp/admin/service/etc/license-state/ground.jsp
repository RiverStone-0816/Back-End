<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="g" type="kr.co.eicn.ippbx.front.config.RequestGlobal"--%>
<%--@elvariable id="message" type="kr.co.eicn.ippbx.util.spring.RequestMessage"--%>
<%--@elvariable id="user" type="kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse"--%>
<%--@elvariable id="version" type="java.lang.String"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/service/etc/license-state/"/>
        <div class="sub-content ui container fluid unstackable">
            <div class="panel panel-search">
                <div class="panel-heading">
                    <div class="pull-left">
                        <div class="panel-label">IPCC 라이센스 현황</div>
                    </div>
                </div>
            </div>
            <div class="panel">
                <div class="panel-heading">
                    <div class="pull-left">
                        <h3 class="panel-title">[신규] 남품일자 : 2020년 4월 20일</h3>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="ui celled table structured compact unstackable border-top">
                        <thead>
                        <tr>
                            <th>구분</th>
                            <th>납품내역</th>
                            <th>비고</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>공급사</td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>제조(개발)사</td>
                            <td>(주)이아이씨엔</td>
                            <td>
                                <table class="include-table">
                                    <tbody>
                                    <tr>
                                        <td>(주)이아이씨엔 (인)</td>
                                        <td>
                                            <img src="<c:url value="/resources/images/loading.svg"/>" style="max-width: 50px; max-height: 50px;" alt="(주)이아이씨엔 (인)"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>납품형태</td>
                            <td>신규 설치</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>제품명</td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>IPCC 공급 라이센스</td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>S/W Version</td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>S/W Serial No.</td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>납품일자</td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td rowspan="2">설치 H/W 정보</td>
                            <td>(Server) 모델명</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Service Tag</td>
                            <td></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</tags:tabContentLayout>
