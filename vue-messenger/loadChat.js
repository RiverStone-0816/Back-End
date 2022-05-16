const CHAT_SERVER_URL = "http://localhost:8080";
const CHAT_ICON_IMG_URL = "C:\\Users\\sunny\\Downloads\\chat-icon.png";

window.onload = function () {
    let senderKey = document.getElementById("chat-bot-script").getAttribute("data-sender-key");
    let isIframe = document.getElementById("chat-bot-script").getAttribute("data-isIframe");
    let chatStyle = document.createElement('style',);
    chatStyle.type = "text/css";
    chatStyle.innerHTML = "\n #chat-load {display: none;position: fixed !important;right: 50px !important;bottom: 50px !important;width: fit-content;min-width: 380px;height: 80% !important;min-height: 600px !important;max-height: 850px !important;overflow: hidden !important;background-color: transparent !important;border-radius: 1.5rem !important;box-shadow: rgb(0 0 0 / 20%) 0px 12px 30px 5px !important;} #chat-iframe {position: relative !important;height: 100%;width: 100% !important;border: none !important;} #chat-button {position: absolute;bottom: 50px;right: 50px;width: 64px;height: 64px;margin: 0;padding: 0;border: 0;}"
    document.body.appendChild(chatStyle);

    if (!document.getElementById('chat-bot')) {
        let chatBot = document.createElement('div');
        chatBot.id = "chat-bot";
        document.body.appendChild(chatBot);
    }
    if (!document.getElementById('chat-button')) {
        let chatButton = document.createElement('button');
        chatButton.id = "chat-button";
        chatButton.onclick = showChat;
        document.getElementById('chat-bot').appendChild(chatButton);
        document.getElementById('chat-button').innerHTML = '<img src=' + CHAT_ICON_IMG_URL + ' style="width: 100%; height: 100%">';
    }
    if (!document.getElementById('chat-load')) {
        let chatLoad = document.createElement('div');
        chatLoad.id = "chat-load";
        document.getElementById('chat-bot').appendChild(chatLoad);
        document.getElementById('chat-load').innerHTML = '<iframe id="chat-iframe" src=' + CHAT_SERVER_URL + '/?senderKey=' + senderKey + '&isIframe=' + isIframe + '></iframe>';
    }

    window.addEventListener('message', receivePostMessage);
    window.addEventListener('focus', hideChat);
};

function receivePostMessage(event) {
    if (event.origin === CHAT_SERVER_URL) {
        if (event.data === 'closeIframe') hideChat();
    }
}

function showChat() {
    document.getElementById('chat-load').style.display = 'block';
    document.getElementById('chat-button').style.display = 'none';
    document.getElementById('chat-iframe').focus();
}

function hideChat() {
    document.getElementById('chat-load').style.display = 'none';
    document.getElementById('chat-button').style.display = 'block';
}

