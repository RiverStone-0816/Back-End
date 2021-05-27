const ipcRenderer = window.ipcRenderer;

function MessengerInvitation() {
    const messengerInvitation = this;

    ipcRenderer.on('init', function (event, arg) {
        messengerInvitation.init(arg);
    });
}

MessengerInvitation.prototype.invite = function () {
    const messengerInvitation = this;

    const users = [];
    const userNames = [];
    const userMap = {};

    $('.-messenger-user').filter('.active').removeClass('active').each(function () {
        const id = $(this).attr('data-id');
        const name = $(this).attr('data-name');

        if (users.indexOf(id) >= 0)
            return;

        if (keys(messengerInvitation.members).filter(function (userid) {
            return userid === id;
        }).length > 0)
            return;

        users.push(id);
        userNames.push(name);
        userMap[id] = name;
    });

    if (!users.length)
        return;

    ipcRenderer.send('inviteUsers', {'roomId' : messengerInvitation.roomId, 'users' : users, 'userNames' : userNames});
    restSelf.put('/api/chatt/' + encodeURIComponent(messengerInvitation.roomId) + '/chatt-member', {memberList: users}).done(function () {
        ipcRenderer.send('inviteSuccess', {'roomId' : messengerInvitation.roomId, 'users' : users, 'userMap' : userMap});
        self.close();
    });
}

MessengerInvitation.prototype._loadOrganization = function (response, userStatuses, statusCodes) {
    const messengerInvitation = this;

    function attachFolder(container, e, level) {
        level = level || 1;

        const item = $('<div/>', {class: 'item -messenger-folder'})
            .append($('<span/>', {class: 'material-icons', text: 'filter_' + (level > 9 ? '9_plus' : level)}))
            .appendTo(container);

        const content = $('<div/>', {class: 'content'})
            .append(
                $('<div/>', {class: 'header level -messenger-folder-header', text: e.groupName})
                    .click(function (event) {
                        const item = $(this).closest('.item');
                        if (event.ctrlKey) {
                            item.toggleClass('active');
                        } else {
                            messengerInvitation.ui.organizationPanel.find('.-messenger-user').removeClass('active');
                            messengerInvitation.ui.organizationPanel.find('.-messenger-folder').removeClass('active');
                            item.addClass('active');
                        }
                    })
                    .dblclick(function () {
                        const item = $(this).closest('.item');
                        messengerInvitation.ui.organizationPanel.find('.-messenger-user').removeClass('active');
                        messengerInvitation.ui.organizationPanel.find('.-messenger-folder').removeClass('active');
                        item.addClass('active');
                        messengerInvitation.openRoom();
                    })
            )
            .appendTo(item);

        const childrenContainer = $('<div/>', {class: 'list'}).css({'display': groupTree.contains(e.groupTreeName) ? '' : 'none'})
            .appendTo(content);

        if (e.personList)
            e.personList.map(function (e) {
                attachPerson(childrenContainer, e);
            });

        if (e.organizationMetaChatt)
            e.organizationMetaChatt.map(function (e) {
                attachFolder(childrenContainer, e, level + 1);
            });
    }

    function attachPerson(container, e) {
        if (e.id === userId)
            return;

        const header = $('<div/>', {class: 'header'})
            .append($('<span/>', {class: 'name', text: e.idName}));

        if (e.extension)
            header.append($('<span/>', {class: 'detail word', text: '내선:' + e.extension}));
        if (e.hpNumber && messengerInvitation.isPosition === 'N')
            header.append($('<span/>', {class: 'detail word', text: e.extension ? ' / ' : ''})).append($('<span/>', {class: 'detail word hover-focus -hp-number', text: '휴대폰:' + e.hpNumber, 'data-value': e.hpNumber}));
        if (e.emailInfo)
            header.append($('<span/>', {class: 'detail word', text: e.extension || (e.hpNumber && messenger.isPosition === 'N') ? ' / ' : ''})).append($('<span/>', {class: 'detail word', text: '이메일:' + e.emailInfo}));

        $('<div/>', {class: 'item user -messenger-user', 'data-id': e.id, 'data-name': e.idName})
            .append(
                $('<div/>', {class: 'picture-container'})
                    .append($('<img/>', {
                        class: 'picture -picture -status-icon ' + (e.isLoginChatt === 'L' ? 'active' : ''),
                        'data-id': e.id,
                        src: e.profilePhoto
                            ? apiServerUrl + '/api/memo/profile-resource?path=' + encodeURIComponent(e.profilePhoto) + '&token=' + encodeURIComponent(accessToken)
                            : defaultProfilePicture
                    }))
                    .append($('<span/>', {
                        'data-id': e.id,
                        class: 'material-icons',
                        text: userStatuses[e.id] && statusCodes[userStatuses[e.id].status] ? statusCodes[userStatuses[e.id].status].icon || 'person' : 'person'
                    }))
            )
            .append($('<div/>', {class: 'content'}).append(header))
            .appendTo(container);
    }

    messengerInvitation.ui.organizationPanel.empty();
    response.data.map(function (e) {
        attachFolder(messengerInvitation.ui.organizationPanel, e);
    });

    $('.material-icons').on('click',function(){
        var list = $(this).next(".content").find('.list');
        var folderHeader = $(this).text().contains('1') ? $(this).find('.list') : $(this).next(".content").find('.-messenger-folder-header');
        list.slideToggle(0, '', function () {
            if (list.css('display') === 'none')
                folderHeader.css({'color': '#dbdbdb'})
            else
                folderHeader.css({'color': ''})
        });
    });
};

MessengerInvitation.prototype.init = function (arg) {
    const messengerInvitation = this;

    messengerInvitation.roomId = arg.roomId;
    messengerInvitation.members = arg.members;
    messengerInvitation.ui = {
        organizationPanel: $('#organization')
    };

    const document = $(window.document);

    messengerInvitation._loadOrganization(arg.organizationData, arg.userStatuses, arg.statusCodes);
    document.find('.detail').remove();

    document.find('.-messenger-user').click(function (event) {
        event.stopPropagation();

        if (event.ctrlKey) {
            $(this).toggleClass('active');

            if ($(this).hasClass('active')) {
                window.lastActiveElement = this;
            }

            return false;
        }
        if (event.shiftKey && window.lastActiveElement) {
            document.find('.-messenger-user').removeClass('active');
            document.find('.-messenger-bookmark').removeClass('active');
            document.find('.-messenger-folder').removeClass('active');
            $(window.lastActiveElement).addClass('active');

            const _this = this;

            if (_this === window.lastActiveElement)
                return false;

            let meetActiveElement = false;
            let meetMe = false;
            document.find('.-messenger-user').each(function () {
                if (this === window.lastActiveElement) {
                    $(this).addClass('active');
                    meetActiveElement = true;
                } else if (this === _this) {
                    $(this).addClass('active');
                    meetMe = true;
                } else {
                    if (meetActiveElement ^ meetMe)
                        $(this).addClass('active');
                }
            });
        } else {
            document.find('.-messenger-user').removeClass('active');
            document.find('.-messenger-bookmark').removeClass('active');
            document.find('.-messenger-folder').removeClass('active');
            $(this).addClass('active');
            window.lastActiveElement = this;
        }

        return false;
    });
}
