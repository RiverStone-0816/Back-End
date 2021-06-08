function TabController(tabLabelFlowContainerIndicator, tabLabelContainerIndicator, tabArrowIndicator, tabContentContainerIndicator) {
    this.tabLabelContainer = $(tabLabelContainerIndicator);
    this.tabContentContainer = $(tabContentContainerIndicator);
    this.tabArrow = $(tabArrowIndicator);
    this.tabLabelFlowContainer = $(tabLabelFlowContainerIndicator);
    this.tabLabels = [];
    this.tabContents = {};

    return this;
}

TabController.prototype.getTabLabelTotalWidth = function () {
    return this.tabLabelContainer.outerWidth();
};

TabController.prototype.decideTabArrowVisible = function () {
    const totalWidth = this.getTabLabelTotalWidth();
    if (totalWidth > this.tabLabelFlowContainer.outerWidth()) {
        this.tabArrow.show();
    } else {
        this.tabArrow.hide();
    }
};

TabController.prototype.moveToRightTabLabels = function (left) {
    const containerWidth = this.tabLabelFlowContainer.outerWidth();
    const labelsWidth = this.getTabLabelTotalWidth();

    if (!isNaN(left)) {
        this.tabLabelContainer.css('left', 0);
        return;
    }

    const currentLeft = parseFloat(this.tabLabelContainer.css('left'));
    this.tabLabelContainer.css('left', labelsWidth > currentLeft + containerWidth
        ? Math.max(containerWidth - labelsWidth, currentLeft - containerWidth)
        : currentLeft - containerWidth);
};

TabController.prototype.moveToLeftTabLabels = function () {
    const containerWidth = this.tabLabelFlowContainer.outerWidth();
    const currentLeft = parseFloat(this.tabLabelContainer.css('left'));
    this.tabLabelContainer.css('left', Math.min(currentLeft + containerWidth, 0));
};

TabController.prototype.attachTab = function (linker) {
    const _this = this;
    const title = $(linker).attr('title') || $(linker).text();
    const href = $(linker).attr('href');

    const validTabLabels = this.tabLabelContainer.find('.tab-label').filter(function () {
        return $(this).attr('data-href') === href;
    });

    const tabContentId = validTabLabels.length > 0 ? validTabLabels.attr('data-target-id') : ('tab-' + guid());
    let tabLabel;

    if (validTabLabels.length > 0) {
        tabLabel = validTabLabels
    } else {
        tabLabel = $('<li/>', {
            'class': 'tab-label', 'data-href': href, 'data-target-id': tabContentId, text: title,
            click: function () {
                _this.tabLabels.map(function (label) {
                    label.removeClass('active');
                });
                $(this).addClass('active');

                values(_this.tabContents).map(function (content) {
                    content.removeClass('active');
                });
                _this.tabContents[$(this).attr('data-target-id')].addClass('active');
                _this.decideTabArrowVisible();
            }
        });

        if ($(linker).attr('data-closable') !== 'false')
            tabLabel.append($('<button/>', {
                'class': 'tab-close',
                click: function () {
                    _this.removeTab($(this).closest('.tab-label'));
                    _this.decideTabArrowVisible();
                    try {
                        tab.close();
                    } catch (e) {
                        console.error(e);
                    }
                }
            }));

        _this.tabLabels.push(tabLabel);
    }

    if (validTabLabels.length === 0) {
        this.tabLabelContainer.prepend(tabLabel);
        const tabContent = $('<iframe/>', {id: tabContentId, name: tabContentId, 'class': 'tab-content active'});
        this.tabContentContainer.append(tabContent);
        _this.tabContents[tabContentId] = tabContent;
        this.decideTabArrowVisible();
    }

    const tab = window.open(href, tabContentId, "width=0 height=0 menubar=no status=no");

    tabLabel.click();
    this.moveToRightTabLabels(0);
};

TabController.prototype.removeTab = function (tabLabel) {
    const _this = this;
    const contentId = tabLabel.attr('data-target-id');
    const changingActiveLabel = tabLabel.hasClass('active');

    function getTabLabelIndex() {
        for (let i = 0; i < _this.tabLabels.length; i++)
            if (contentId === _this.tabLabels[i].attr('data-target-id'))
                return i;

        throw 'invalid tabLabel';
    }

    const labelIndex = getTabLabelIndex();
    _this.tabLabels.splice(labelIndex, 1);
    tabLabel.remove();

    _this.tabContents[contentId].remove();
    delete _this.tabContents[contentId];

    if (changingActiveLabel)
        try {
            const lastLabel = (_this.tabLabels[labelIndex] || _this.tabLabels[labelIndex - 1]);
            if (lastLabel)
                lastLabel.click();
        } catch (e) {
            console.error(e);
        }

    _this.decideTabArrowVisible();
};
