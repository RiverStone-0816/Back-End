/*https://www.jqueryscript.net/other/HTML5-Audio-Player-maudio.html*/

function maudio(_opt) {
    const opt = {
        obj: _opt.obj ? _opt.obj : 'audio',
        fastStep: _opt.fastStep ? _opt.fastStep : 10
    };
    opt.tpl = '\
    <div class="maudio">\
        <audio src="" initaudio="false"></audio>\
        <div class="audio-control">\
            <a href="javascript:" class="fast-reverse"></a>\
            <a href="javascript:" class="play"></a>\
            <a href="javascript:" class="fast-forward"></a>\
            <div class="progress-bar">\
              <div class="progress-pass"></div>\
            </div>\
            <div class="time-keep">\
              <span class="current-time">00:00</span> / <span class="duration">00:00</span>\
            </div>\
            <a class="mute"></a>\
            <div class="volume-bar">\
              <div class="volume-pass"></div>\
            </div>\
            <select href="javascript:" class="speed-control" title="재생속도">\
                <option value="1.0" selected="selected">1.0x </option>\
                <option value="1.5">1.5x </option>\
                <option value="2.0">2.0x </option>\
                <option value="3.0">3.0x </option>\
            </select>\
        </div>\
      </div>\
    </div>';

    const thisWindow = $(opt.obj).parents(window);

    // 스타일이 이미 초기화된 경우 이벤트를 초기화하면 됩니다.
    if (!$(opt.obj).parent('.maudio').length || !$(opt.obj).next('div.audio-control').length) {
        // 모든 오디오 초기화
        window.tDuration = window.tDuration ? window.tDuration : {};
        $(opt.obj).each(function (i) {
            $(this).before(opt.tpl);
            const thisBox = $(this).prev('div.maudio');
            const thisAudio = thisBox.children('audio')[0];
            thisAudio.src = $(this).attr('src') || $(this).attr('data-src') || $(this).children('source').attr('src');
            window.tDuration[opt.obj + thisAudio.src + '_' + i] = setInterval(function () {
                if (thisAudio.duration) {
                    thisBox.find('.time-keep .duration').text(timeFormat(thisAudio.duration));
                    clearInterval(window.tDuration[opt.obj + thisAudio.src + '_' + i])
                }
            }, 100);
            $(this).remove()
        })
    }

    function progressBar(audio, pgp) {
        const p = 100 * currentAudio.currentTime / currentAudio.duration;
        currentAudioBox.find('.progress-pass').css({'width': p + '%'});
        // 현재 시간 계산
        currentAudioBox.find('.current-time').text(timeFormat(currentAudio.currentTime));
        // 플레이 종료
        if (currentAudio.currentTime >= currentAudio.duration) {
            currentAudioBox.removeClass('playing');
            clearInterval(t)
        }
    }

    function bindAudioCtrl() {
        // 플레이 버튼
        $(thisWindow).find('.audio-control .play').off('click');
        $(thisWindow).find('.audio-control .play').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();

            const audioBox = $(this).parent('.audio-control').parent('.maudio');
            const audio = audioBox.children('audio')[0];
            if (audioBox.hasClass('playing')) {
                audio.pause();
                audioBox.removeClass('playing')
            } else {
                $(thisWindow).find('.playing').each(function () {
                    $(this).children('audio')[0].pause();
                    $(this).removeClass('playing')
                });
                audio.play();
                audioBox.addClass('playing');
                currentAudio = audio;
                currentAudioBox = audioBox;

                const speedControl = $(thisWindow).find('.audio-control .speed-control');
                currentAudio.playbackRate = speedControl.find('option:selected').val();
                console.log("currentAudio.playbackRate: ", currentAudio.playbackRate)

                // 진행률 표시줄
                window.t = window.setInterval(function () {
                    progressBar()
                }, 500)
            }
        });

        // 빨리 감기
        $(thisWindow).find('.audio-control .fast-reverse').off('click');
        $(thisWindow).find('.audio-control .fast-reverse').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();

            currentAudio.currentTime -= opt.fastStep;
        });

        // 되감기
        $(thisWindow).find('.audio-control .fast-forward').off('click');
        $(thisWindow).find('.audio-control .fast-forward').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();

            currentAudio.currentTime += opt.fastStep;
        });

        // 볼륨바
        $(thisWindow).find('.audio-control .volume-bar').off('click');
        $(thisWindow).find('.audio-control .volume-bar').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();

            const audioBox = $(this).parent('.audio-control').parent('.maudio');
            const audio = audioBox.children('audio')[0];
            const p = e.offsetX / audioBox.find('.volume-bar').width();
            audioBox.find('.volume-pass').css({"width": p * 100 + '%'});
            audio.volume = p > 1 ? 1 : p
        });

        // 재생속도
        $(thisWindow).find('.audio-control .speed-control').change(function () {
            const audioBox = $(this).parent('.audio-control').parent('.maudio');
            const audio = audioBox.children('audio')[0];
            audio.playbackRate = $(this).find('option:selected').val();
        });

        // 음소거
        $(thisWindow).find('.audio-control .mute').off('click');
        $(thisWindow).find('.audio-control .mute').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();

            let audioBox = $(this).parent('.audio-control').parent('.maudio');
            let audio = audioBox.children('audio')[0];
            if ($(this).hasClass('muted')) {
                audio.muted = false;
                $(this).removeClass('muted')
            } else {
                audio.muted = true;
                $(this).addClass('muted')
            }
        });

        // 진행바
        $(thisWindow).find('.audio-control .progress-bar').off('click');
        $(thisWindow).find('.audio-control .progress-bar').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();

            let audioBox = $(this).parent('.audio-control').parent('.maudio');
            let audio = audioBox.children('audio')[0];
            let p = e.offsetX / audioBox.find('.progress-bar').width();
            audioBox.find('.progress-pass').css({"width": p * 100 + '%'});
            audio.currentTime = audio.duration * p;
            audioBox.find('.current-time').text(timeFormat(audio.currentTime))
        });

        // 오디오 컨트롤
        $(thisWindow).find('.maudio audio').off('play');
        $(thisWindow).find('.maudio audio').on('play', function () {
            if (!$(this).parent('.maudio').hasClass('playing'))
                $(this).parent('.maudio').addClass('playing')
        });
        $(thisWindow).find('.maudio audio').off('pause');
        $(thisWindow).find('.maudio audio').on('pause', function () {
            if ($(this).parent('.maudio').hasClass('playing'))
                $(this).parent('.maudio').removeClass('playing')
        });
    }

    bindAudioCtrl();

    function timeFormat(sec) {
        const m = parseInt(sec / 60);
        const s = parseInt(sec % 60);
        return (m < 10 ? '0' + m : m) + ':' + (s < 10 ? '0' + s : s)
    }

    return $(thisWindow);
}