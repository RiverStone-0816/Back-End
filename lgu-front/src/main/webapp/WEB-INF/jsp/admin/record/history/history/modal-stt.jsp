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
<%--@elvariable id="apiServerUrl" type="java.lang.String"--%>
<%--@elvariable id="accessToken" type="java.lang.String"--%>

<div class="ui modal">
    <i class="close icon"></i>
    <div class="header">
        STT
    </div>
    <div class="content">
        <div class="fields">
            <div class="field">
                <div class="ui fluid input">
                    <input type="text" id="word" placeholder="키워드를 입력하세요.">
                </div>
            </div>
        </div>
        <br>
        <div class="ui container">
            <div class="ui segment" id="textbox">
                손흥민은 10일(한국시간) 영국 런던의 토트넘 홋스퍼 스타디움에서 열린 셰필드와의 2019~2020 잉글랜드 프로축구 프리미어리그 12라운드 홈경기에서 후반 13분 선제골을
                터뜨렸다.


                왼쪽 공격수로 나선 그는 델리 알리의 침투 패스가 상대 수비수의 다리에 맞고 흐르자 골지역 오른쪽에서 강한 오른발 슈팅을 해 득점에 성공했다. 지난 7일 츠르베나
                즈베즈다(세르비아)와의 유럽축구연맹(UEFA) 챔피언스리그 조별예선에서 멀티골을 넣은데 이어 두 경기 연속 득점이다.


                정규리그 3호골이자 컵대회 포함 시즌 8호골. 즈베즈다전에서 경신한 유럽 프로축구 한국 선수 최다득점 기록은 124골로 늘었다.


                토트넘은 손흥민의 선제골을 지키지 못하고 후반 33분 셰필드의 조지 빌독에게 동점골을 내줘 1-1로 비겼다. 최근 정규리그 5경기에서 3무2패로 승리를 따내지 못했다.
            </div>
        </div>
    </div>
    <div class="actions">
        <button class="ui left floated button -download-file">다운로드</button>
        <button class="ui blue button modal-close">확인</button>
    </div>
</div>

<script>
    modal.find('.-download-file').click(function () {
        window.open(contextPath + '${apiServerUrl}/api/v1/admin/record/history/${seq}/resource?token=${accessToken}', '_blank');
    });

    var searchCnt = 0;

    var option = {
        color: "black",
        background: "yellow",
        bold: false,
        class: "high",
        ignoreCase: true,
        wholeWord: false
    };

    var originalContent = $("#textbox").html();
    searchCnt = $("#textbox").highlight($("#word").val(), option);
    $("#matcheCnt").text(searchCnt);

    $("#word").keyup(function () {
        $("#textbox").html(originalContent);
        $("#keyword").html(this.value);
        if (this.value == "") return;
        searchCnt = $("#textbox").highlight(this.value, option);
        $("#matcheCnt").text(searchCnt);
    });

    $("#toggleIgnoreCase").change(function () {
        $("#textbox").html(originalContent);
        if ($(this).prop("checked")) {
            option.ignoreCase = true;
            $("#ignoreCase").html("true");
        } else {
            option.ignoreCase = false;
            $("#ignoreCase").html("false");
        }
        searchCnt = $("#textbox").highlight($("#word").val(), option);
        $("#matcheCnt").text(searchCnt);
    });

    $("#toggleWholeWord").change(function () {
        $("#textbox").html(originalContent);
        if ($(this).prop("checked")) {
            option.wholeWord = true;
            $("#wholeWord").html("true");
        } else {
            option.wholeWord = false;
            $("#wholeWord").html("false");
        }
        searchCnt = $("#textbox").highlight($("#word").val(), option);
        $("#matcheCnt").text(searchCnt);
    });

    $("#toggleBold").change(function () {
        $("#textbox").html(originalContent);
        if ($(this).prop("checked")) {
            option.bold = true;
            $("#bold").html("true");
        } else {
            option.bold = false;
            $("#bold").html("false");
        }
        searchCnt = $("#textbox").highlight($("#word").val(), option);
    });

    $("#color").keyup(function () {
        $("#textbox").html(originalContent);
        option.color = this.value;
        searchCnt = $("#textbox").highlight($("#word").val(), option);
        $("#matcheCnt").text(searchCnt);
    });

    $("#background").keyup(function () {
        $("#textbox").html(originalContent);
        option.background = this.value;
        searchCnt = $("#textbox").highlight($("#word").val(), option);
        $("#matcheCnt").text(searchCnt);
    });
</script>
