(function ($) {
    "use strict";

    $(window).on('load', function () {
        $('body').bindHelpers();
    });

    $(document).on("click", 'a[href="#"]', function (e) {
        e.preventDefault();
    });

    $(document).on('click', '.tab-indicator', function (event) {
        event.preventDefault();
        event.stopPropagation();
        parent.tabController.attachTab(this);
    });
})(jQuery);