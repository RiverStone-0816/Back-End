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
<%--@elvariable id="serviceKind" type="java.lang.String"--%>

<%--@elvariable id="pagination" type="kr.co.eicn.ippbx.util.page.Pagination<kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse>"--%>
<%--@elvariable id="searchOrganizationNames" type="java.util.List"--%>
<%--@elvariable id="search" type="kr.co.eicn.ippbx.model.search.PersonSearchRequest"--%>

<tags:tabContentLayout>
    <div class="content-wrapper-frame">
        <tags:page-menu-tab url="/admin/stt/transcribe/write/"/>
        <div class="sub-content ui container fluid">
            <form:form id="search-form" modelAttribute="search" method="get" class="panel panel-search">
                <div class="panel-heading dp-flex align-items-center justify-content-space-between">
                    <div>
                        검색
                    </div>
                    <div class="dp-flex align-items-center">
                        <div class="ui slider checkbox mr15">
                            <label>접기/펴기</label>
                            <input type="checkbox" name="newsletter" id="_newsletter">
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="ui brand basic button">검색</button>
                            <button type="button" class="ui grey basic button" onclick="refreshPageWithoutParameters()">초기화</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="search-area">
                        <div class="ui grid">
                            <div class="row">
                                <div class="two wide column"><label class="control-label">그룹선택</label></div>
                                <div class="five wide column overflow-unset">
                                    <div class="ui form">
                                        <form:select path="groupCode">
                                            <form:option value="" label="그룹선택"/>
                                            <form:options items="${groupList}" itemLabel="groupName" itemValue="seq"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="two wide column"><label class="control-label">파일선택</label></div>
                                <div class="five wide column overflow-unset">
                                    <div class="ui form">
                                        <form:select path="fileSeq">
                                            <form:option value="" label="파일선택"/>
                                            <c:forEach var="e" items="${dataList}">
                                                <form:option cssStyle="display: none" value="${e.seq}" label="${e.fileName}" data-group="${e.groupCode}"/>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <div class="panel">
                <div class="panel-heading" id="transcribe">
                    <c:if test="${entity != null}">
                        <div>
                            <audio src="${pageContext.request.contextPath}/api/transcribe/write/${entity.seq}/resource" controls></audio>
                        </div>
                    </c:if>
                </div>
                <div class="panel-body">
                    <c:if test="${entity != null}">
                        <div class="stt-m">
                            <c:forEach var="e" items="${entity.dataList}">
                                <div class="data hyp" data-seq="${e.seq}">
                                    <div class="time">
                                        <input class="time-ms" type="text" value="${e.startMs}" data-type="start" onclick="seek($(this).val())"/>
                                        <input class="time-ms" type="text" value="${e.stopMs}" data-type="stop" onclick="seek($(this).val())"/>
                                        <button class="ui button mini compact" type="button" onclick="addBlock($(this).closest('.data').data('seq'))"><i class="plus icon"></i></button>
                                        <button class="ui button mini compact" type="button" onclick="removeBlock($(this).closest('.data').data('seq'))"><i class="delete icon"></i></button>
                                        학습여부 : <input type="checkbox" name="field[test].checked" value="" checked />
                                    </div>
                                    <div class="text">
                                        <textarea class="info" rows="2" onclick="seek($(this).closest('.data').find('.time-ms[data-type=start]').val())">${e.text}</textarea>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="stt-m">
                            <c:forEach var="e" items="${entity.refList}">
                                <div class="data ref" data-seq="${e.seq}">
                                    <div class="time">
                                        <input class="time-ms" type="text" readonly value="${e.startMs}" data-type="start" onclick="seek($(this).val())"/>
                                        <input class="time-ms" type="text" readonly value="${e.stopMs}" data-type="stop" onclick="seek($(this).val())"/>
                                    </div>
                                    <div class="text">
                                        <textarea class="info" rows="2">${e.text}</textarea>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <div align="center">
                            <button class="ui blue button" type="button" onclick="submitData()">등록</button>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <tags:scripts>
        <script>
            <c:if test="${entity != null}">

            function timeFormat(sec){
                var m = parseInt(sec/60);
                var s = parseInt(sec%60);
                return (m < 10 ?  '0' + m : m)+ ':' + (s < 10 ?  '0' + s : s)
            }

            function seek(value) {
                if (!value)
                    return

                const audioBox = $('#transcribe').find('.maudio')
                audioBox.find('.progress-pass').css({"width":(value / (audioBox.find('audio')[0].duration * 1000)) * 100 + '%'});
                audioBox.find('audio')[0].currentTime = value / 1000;
                audioBox.find('.current-time').text(timeFormat(audioBox.find('audio')[0].currentTime))
            }
            </c:if>

            $('#groupCode').change(function () {
                const groupSeq = $(this).find('option:selected').val()

                $('#fileSeq').find('option').each((idx, e) => {
                    if ($(e).val() === '' || $(e).data('group') + '' === groupSeq)
                        $(e).show()
                    else
                        $(e).hide();
                })
            })

            let lastSeq = ${entity.dataList.size() + 1}

                function addBlock(seq) {
                    const original = $(".data[data-seq='" + seq + "']")

                    original.each((i, e) => {
                        const clone = $(e).clone(true, true)
                        clone.attr('data-seq', lastSeq)
                        clone.data('seq', lastSeq)
                        clone.find('.info').val('')

                        if ($(e).hasClass('hyp')) {
                            clone.find('.time-ms').val('')
                            clone.find('.info').val('')

                            $(e).after(clone)
                        } else {
                            clone.find('.time-ms').val('')
                            clone.find('button').remove()

                            $(e).after(clone)
                        }
                    })

                    lastSeq++
                }

            function removeBlock(seq) {
                $('.data').filter(function () {
                    return $(this).data('seq') === seq
                }).each((idx, e) => $(e).remove())
            }

            $('.time-ms').on('input', function () {
                const original = $(this)

                $(".data.ref[data-seq='" + original.closest('.data').data('seq') +"']").find(".time-ms[data-type='" + original.data('type') + "']").val(original.val())
            })

            function submitData() {
                const hypData = [];
                const refData = [];

                $('.data.hyp').each((i, e) => {
                    hypData.push(convertJsonData(i, e))
                })
                $('.data.ref').each((i, e) => {
                    refData.push(convertJsonData(i, e))
                })

                debugger

                /*restSelf.put('/api/transcribe/write/${entity.seq}', {hypData: hypData, refData: refData}).donc(function (response) {
                    if (response.data.result === "OK"){
                        alert("전사 등록하였습니다.");
                    }else{
                        alert("전사 등록에 실패하였습니다. 다시 등록해주시기 바랍니다.");
                    }
                })*/
            }

            function convertJsonData(idx, entity) {
                return {"seq": idx + 1, "start_ms": $(entity).find(".time-ms[data-type='start']").val(), "stop_ms": $(entity).find(".time-ms[data-type='stop']").val(), "text": $(entity).find(".info").val()}
            }

            $(window).on('load', function () {
                $('#groupCode').change()
            })

            $('.stt-m').scroll(function () {
                $('.stt-m').not($(this)).scrollTop($(this).scrollTop())
            })
        </script>
    </tags:scripts>
</tags:tabContentLayout>
