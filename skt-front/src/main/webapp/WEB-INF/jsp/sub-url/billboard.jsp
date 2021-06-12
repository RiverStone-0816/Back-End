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

<c:set var="hasExtension" value="${user.extension != null && user.extension != ''}"/>
<c:set var="isStat" value="${user.isStat == 'Y'}"/>

<tags:tabContentLayout>


    <div class="billboard-wrap">
        <div class="header">
            <div class="pull-left">
                전광판 타이틀
            </div>
            <div class="pull-right">
                <div class="menu-btn-wrap"><button class="menu-open-btn" id="billboard-popup-btn" onclick="billboardPopup()"></button></div>
                <div class="time-wrap">2021-05-16 22:34:23</div>
            </div>
        </div>
        <div class="content">



            <ul class="billboard-tabs">
                <li class="tab-link current" data-tab="billboard-tab1"></li>
                <li class="tab-link" data-tab="billboard-tab2"></li>
                <li class="tab-link" data-tab="billboard-tab3"></li>
                <li class="tab-link" data-tab="billboard-tab4"></li>
            </ul>


                <div id="billboard-tab1" class="billboard-tab-content current">
                    <div class="ui grid full-height remove-margin">
                  <div class="twelve wide column remove-pb">
                      <div class="ui equal width grid full-height flex-flow-column">
                          <div class="equal width row flex-130">
                              <div class="column">
                                  <div class="board-box incoming-call full-height">
                                      <div class="board-title flex-100">인입콜</div>
                                      <div class="board-number large flex-160">185</div>
                                  </div>
                              </div>
                              <div class="column">
                                  <div class="board-box connection-request full-height">
                                      <div class="board-title flex-100">연결요청</div>
                                      <div class="board-number large flex-160">20</div>
                                  </div>
                              </div>
                          </div>
                          <div class="row flex-100 remove-pb">
                              <div class="column">
                                  <div class="board-box reception full-height">
                                      <div class="board-title flex-100">수신</div>
                                      <div class="board-number flex-140">185</div>
                                  </div>
                              </div>
                              <div class="column">
                                  <div class="board-box non-reception full-height">
                                      <div class="board-title flex-100">비수신</div>
                                      <div class="board-number flex-140">185</div>
                                  </div>
                              </div>
                              <div class="column">
                                  <div class="board-box response-rate full-height">
                                      <div class="board-title flex-100">응대율</div>
                                      <div class="board-number flex-140">100%</div>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div class="four wide column remove-padding">
                      <div class="ui one column grid full-height remove-margin">
                          <div class="column">
                              <div class="board-label">
                                  <div class="left">대기</div>
                                  <div class="right">100</div>
                              </div>
                          </div>
                          <div class="column">
                              <div class="board-label">
                                  <div class="left">통화중</div>
                                  <div class="right">100</div>
                              </div>
                          </div>
                          <div class="column">
                              <div class="board-label">
                                  <div class="left">후처리</div>
                                  <div class="right">100</div>
                              </div>
                          </div>
                          <div class="column">
                              <div class="board-label">
                                  <div class="left">로그아웃</div>
                                  <div class="right">100</div>
                              </div>
                          </div>
                          <div class="column">
                              <div class="board-label">
                                  <div class="left">휴식</div>
                                  <div class="right">100</div>
                              </div>
                          </div>
                          <div class="column">
                              <div class="board-label">
                                  <div class="left">식사</div>
                                  <div class="right">100</div>
                              </div>
                          </div>
                          <div class="column">
                              <div class="board-label">
                                  <div class="left">이석</div>
                                  <div class="right">100</div>
                              </div>
                          </div>
                          <div class="column">
                              <div class="board-label">
                                  <div class="left">기타</div>
                                  <div class="right">100</div>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
                </div>
                <div id="billboard-tab2" class="billboard-tab-content">
                    <div class="ui grid full-height remove-margin">
                  <div class="equal width row">
                      <div class="column">
                          <div class="board-box incoming-call full-height">
                              <div class="board-title flex-100">인입콜</div>
                              <div class="board-number flex-160">185</div>
                          </div>
                      </div>
                      <div class="column">
                          <div class="board-box connection-request full-height">
                              <div class="board-title flex-100">연결요청</div>
                              <div class="board-number flex-160">185</div>
                          </div>
                      </div>
                      <div class="column">
                          <div class="board-box reception full-height">
                              <div class="board-title flex-100">수신</div>
                              <div class="board-number flex-160">185</div>
                          </div>
                      </div>
                      <div class="column">
                          <div class="board-box non-reception full-height">
                              <div class="board-title flex-100">비수신</div>
                              <div class="board-number flex-160">185</div>
                          </div>
                      </div>
                      <div class="column">
                          <div class="board-box response-rate full-height">
                              <div class="board-title flex-100">응대율</div>
                              <div class="board-number flex-160">185</div>
                          </div>
                      </div>
                  </div>
                  <div class="equal width row">
                      <div class="column">
                          <div class="board-label-vertical">
                              <div class="top">헌트명</div>
                              <div class="bottom">185</div>
                          </div>
                      </div>
                      <div class="column">
                          <div class="board-label-vertical">
                              <div class="top">고객대기</div>
                              <div class="bottom">185</div>
                          </div>
                      </div>
                      <div class="column">
                          <div class="board-label-vertical">
                              <div class="top">상담대기</div>
                              <div class="bottom">185</div>
                          </div>
                      </div>
                      <div class="column">
                          <div class="board-label-vertical">
                              <div class="top">통화중</div>
                              <div class="bottom">185</div>
                          </div>
                      </div>
                      <div class="column">
                          <div class="board-label-vertical">
                              <div class="top">후처리</div>
                              <div class="bottom">185</div>
                          </div>
                      </div>
                      <div class="column">
                          <div class="board-label-vertical">
                              <div class="top">기타</div>
                              <div class="bottom">185</div>
                          </div>
                      </div>
                      <div class="column">
                          <div class="board-label-vertical">
                              <div class="top">응대율</div>
                              <div class="bottom">185</div>
                          </div>
                      </div>
                  </div>
              </div>
                </div>
                <div id="billboard-tab3" class="billboard-tab-content">
                    <div class="ui grid full-height remove-margin">
                   <div class="equal width row">
                       <div class="column">
                           <div class="board-box incoming-call full-height">
                               <div class="board-title flex-100">인입콜</div>
                               <div class="board-number flex-160">185</div>
                           </div>
                       </div>
                       <div class="column">
                           <div class="board-box connection-request full-height">
                               <div class="board-title flex-100">연결요청</div>
                               <div class="board-number flex-160">185</div>
                           </div>
                       </div>
                       <div class="column">
                           <div class="board-box reception full-height">
                               <div class="board-title flex-100">수신</div>
                               <div class="board-number flex-160">185</div>
                           </div>
                       </div>
                       <div class="column">
                           <div class="board-box non-reception full-height">
                               <div class="board-title flex-100">비수신</div>
                               <div class="board-number flex-160">185</div>
                           </div>
                       </div>
                       <div class="column">
                           <div class="board-box response-rate full-height">
                               <div class="board-title flex-100">응대율</div>
                               <div class="board-number flex-160">185</div>
                           </div>
                       </div>
                   </div>
                   <div class="sixteen wide column">
                       <div class="billboard-chart-wrap flex-flow-column full-height">
                           <div class="flex-100">
                               dd
                           </div>
                           <div class="billboard-chart-label-wrap">
                               <ul>
                                   <li><span class="symbol color-1"></span><span class="text">응대율</span></li>
                                   <li><span class="symbol color-2"></span><span class="text">I/B전체</span></li>
                                   <li><span class="symbol color-3"></span><span class="text">연결요청</span></li>
                                   <li><span class="symbol color-4"></span><span class="text">응대호</span></li>
                                   <li><span class="symbol color-5"></span><span class="text">포기호</span></li>
                               </ul>
                           </div>
                       </div>
                   </div>
               </div>
                </div>
                <div id="billboard-tab4" class="billboard-tab-content">
                    <div class="ui grid full-height remove-margin">
                        <div class="equal width row">
                            <div class="column">
                                <div class="board-label-vertical">
                                    <div class="top">가용율</div>
                                    <div class="bottom">185</div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="board-label-vertical">
                                    <div class="top">대기</div>
                                    <div class="bottom">185</div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="board-label-vertical">
                                    <div class="top">통화중</div>
                                    <div class="bottom">185</div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="board-label-vertical">
                                    <div class="top">후처리</div>
                                    <div class="bottom">185</div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="board-label-vertical">
                                    <div class="top">휴식</div>
                                    <div class="bottom">185</div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="board-label-vertical">
                                    <div class="top">식사</div>
                                    <div class="bottom">185</div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="board-label-vertical">
                                    <div class="top">이석</div>
                                    <div class="bottom">185</div>
                                </div>
                            </div>
                            <div class="column">
                                <div class="board-label-vertical">
                                    <div class="top">기타</div>
                                    <div class="bottom">185</div>
                                </div>
                            </div>
                        </div>
                        <div class="sixteen wide column remove-pb">
                            <div class="ui five column grid full-height">
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left call"></div>
                                        <div class="right">
                                            <div class="time">00:00</div>
                                            <div class="name">김이상담</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left after"></div>
                                        <div class="right">
                                            <div class="time">00:00</div>
                                            <div class="name">김이상담</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left etc"></div>
                                        <div class="right">
                                            <div class="time">00:00</div>
                                            <div class="name">김이상담</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left meal"></div>
                                        <div class="right">
                                            <div class="time">00:00</div>
                                            <div class="name">김이상담</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left move"></div>
                                        <div class="right">
                                            <div class="time">00:00</div>
                                            <div class="name">김이상담</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left rest"></div>
                                        <div class="right">
                                            <div class="time">00:00</div>
                                            <div class="name">김이상담</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left stay"></div>
                                        <div class="right">
                                            <div class="time">00:00</div>
                                            <div class="name">김이상담</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="column">
                                    <div class="user-label">
                                        <div class="left"></div>
                                        <div class="right">
                                            <div class="time"></div>
                                            <div class="name"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>



        </div>
        <div class="footer">
            공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구 공지문구
        </div>
    </div>

    <div class="ui mini modal basic3" id="modal-billboard">
        <i class="close icon"></i>
        <div class="content">
            <div class="billboard-inner">
                <label class="title">상태별 강조 시간</label>
                <div class="inner">
                    <ul>
                        <li>
                            <div class="left"><span class="symbol color-1"></span>대기</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol color-2"></span>통화</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol color-3"></span>후처리</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol color-4"></span>휴식</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol color-4"></span>식사</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol color-4"></span>이석</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                        <li>
                            <div class="left"><span class="symbol color-4"></span>기타</div>
                            <div class="right"><input type="text">분</div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="actions">
            <button type="button" class="ui fluid button">저장</button>
        </div>
    </div>



    <tags:scripts>

        <script>

            $('.billboard-tabs li').click(function(){
                var tab_id = $(this).attr('data-tab');

                $('.billboard-tabs li').removeClass('current');
                $('.billboard-tab-content').removeClass('current');

                $(this).addClass('current');
                $("#"+tab_id).addClass('current');
            })

            function billboardPopup() {
                $('#modal-billboard').modalShow();
            }


        </script>
    </tags:scripts>
</tags:tabContentLayout>
