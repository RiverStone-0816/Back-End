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

<style>
    .progress-bar {
        width: 80% !important;
    }
</style>

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
                                <div class="two wide column">
                                    <button class="tab-arrow tab-arrow-left" type="button" onclick="movePrev()"><i class="material-icons"> keyboard_arrow_left </i></button>
                                    <button class="tab-arrow tab-arrow-right" type="button" onclick="moveNext()"><i class="material-icons"> keyboard_arrow_right </i></button>
                                    <span class="file-index">0 / 0</span>
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
                            <c:forEach var="e" varStatus="status" items="${entity.dataList}">
                                <div class="data hyp" data-seq="${e.seq}">
                                    <div>
                                        <span class="idx">${status.index}</span>
                                        <div class="time">
                                            <input class="time-ms" type="text" value="${e.startMs}" data-type="start" onclick="seek($(this).val())"/>
                                            <input class="time-ms" type="text" value="${e.stopMs}" data-type="stop" onclick="seek($(this).val())"/>
                                            <button class="ui button mini compact" type="button" onclick="addBlock($(this).closest('.data'), 'hyp')"><i class="plus icon"></i></button>
                                            <button class="ui button mini compact" type="button" onclick="removeBlock($(this).closest('.data'))"><i class="delete icon"></i></button>
                                        </div>
                                    </div>
                                    <div class="text">
                                        <textarea class="info" rows="2" onclick="seek($(this).closest('.data').find('.time-ms[data-type=start]').val())">${e.text}</textarea>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="stt-m">
                            <c:forEach var="e" varStatus="status" items="${entity.refList}">
                                <div class="data ref" data-seq="${e.seq}">
                                    <span class="idx">${status.index}</span>
                                    <div class="time">
                                        <input class="time-ms" type="text" value="${e.startMs}" data-type="start" onclick="seek($(this).val())"/>
                                        <input class="time-ms" type="text" value="${e.stopMs}" data-type="stop" onclick="seek($(this).val())"/>
                                        <button class="ui button mini compact" type="button" onclick="addBlock($(this).closest('.data'), 'ref')"><i class="plus icon"></i></button>
                                        <button class="ui button mini compact" type="button" onclick="removeBlock($(this).closest('.data'))"><i class="delete icon"></i></button>
                                        학습여부 : <input type="checkbox" class="learn" ${e.learn == 'y' ? 'checked' : ''} />
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

            const options = {
                <c:forEach var="group" items="${groupList}">
                "${group.seq}": [
                    <c:forEach var="option" items="${dataList}">
                    <c:if test="${group.seq == option.groupCode}">
                    '<option value="${option.seq}" data-group="${option.groupCode}">${option.fileName}</option>',
                    </c:if>
                    </c:forEach>
                ],
                </c:forEach>
            }

            $('#groupCode').change(function () {
                const groupSeq = $(this).find('option:selected').val()

                $('#fileSeq').empty()
                $('#fileSeq').append('<option value="">파일선택</option>')

                $('#fileSeq').append(options[groupSeq])

                $('#fileSeq').change()
            })

            $('#fileSeq').change(function () {
                $('.file-index').text(document.getElementById("fileSeq").selectedIndex + '/' + ($('#fileSeq option').length - 1))
            })

            let lastHypSeq = ${entity.dataList.size() + 1};
            let lastRefSeq = ${entity.dataList.size() + 1};

            function addBlock(block, type) {
                const clone = block.clone(true, true)
                let lastSeq = type === 'hyp' ? lastHypSeq : lastRefSeq

                clone.attr('data-seq', lastSeq)
                clone.data('seq', lastSeq)
                clone.find('.info').val('')
                clone.find('.time-ms').val('')

                block.after(clone)

                if (type === 'hyp')
                    lastHypSeq++
                else
                    lastRefSeq++

                changeIndex(block, true)
            }

            function removeBlock(block) {
                changeIndex(block, false)
                block.remove();
            }

            function changeIndex(block, isAdd) {
                let idx = Number(block.find('.idx').text()) + (isAdd ? 1 : 0)

                block.nextAll().each((i, e) => {
                    $(e).find('.idx').text(idx++)
                })
            }

            function submitData() {
                const hypData = [];
                const refData = [];

                $('.data.hyp').each((i, e) => {
                    hypData.push(convertJsonData(i, e, $(e).find('.text').find(".info").val()))
                })
                $('.data.ref').each((i, e) => {
                    refData.push(convertJsonData(i, e, $(e).find('.text').find(".info").val()))
                })

                restSelf.put('/api/transcribe/write/${entity.seq}', {hypData: hypData, refData: refData}).done(function (response) {
                    if (response.data === "OK"){
                        const nextFile = $("#fileSeq option[value=${search.fileSeq}]").next()

                        if (nextFile.length) {
                            confirm('전사 등록하였습니다.\n다음 파일로 넘어가시겠습니까?').done(function () {
                                $("#fileSeq option[value=" + nextFile.val() + "]").prop('selected', true)
                                $('#search-form').submit()
                            })
                        } else {
                            alert("전사 등록하였습니다. 다음 전사파일이 없습니다..");
                        }
                    }else{
                        alert("전사 등록에 실패하였습니다. 다시 등록해주시기 바랍니다.");
                    }
                })
            }

            function convertJsonData(idx, entity, text) {
                return {"seq": idx + 1, "start_ms": $(entity).find(".time-ms[data-type='start']").val(), "stop_ms": $(entity).find(".time-ms[data-type='stop']").val(), "text": text,
                    "learn": $(entity).find(".learn").is(':checked') ? 'y' : 'n'}
            }

            function moveNext() {
                console.log(1)
                const nextFile = $("#fileSeq option[value=${search.fileSeq}]").next()

                if (nextFile.length) {
                    $("#fileSeq option[value=" + nextFile.val() + "]").prop('selected', true)
                    $('#search-form').submit()
                }
            }

            function movePrev() {
                console.log(2)
                const prevFile = $("#fileSeq option[value=${search.fileSeq}]").prev()

                if (prevFile.length && prevFile.val() !== '') {
                    $("#fileSeq option[value=" + prevFile.val() + "]").prop('selected', true)
                    $('#search-form').submit()
                }
            }

            $(window).on('load', function () {
                $('#groupCode').change()
                <c:if test="${search.fileSeq != null}">
                $("#fileSeq option[value=${search.fileSeq}]").prop('selected', true)
                </c:if>
                $('#fileSeq').change()
            })

            $('.stt-m').scroll(function () {
                $('.stt-m').not($(this)).scrollTop($(this).scrollTop())
            })

        </script>
    </tags:scripts>
</tags:tabContentLayout>
