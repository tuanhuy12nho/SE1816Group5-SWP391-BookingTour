// NProgress
if (typeof NProgress != 'undefined') {
    $(document).ready(function () {
        NProgress.start();
    });

    $(window).load(function () {
        NProgress.done();
    });
}

function showLoading() {
    // $('#loading-wrapper').show();
    NProgress.start();
}

function hideLoading() {
    // $('#loading-wrapper').hide();
    NProgress.done();
}

Number.prototype.currencyFormat = function () {
    return this.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1.")
};

$(document).ready(function () {

    var selectors = "input.label-top, textarea.label-top";

    $(selectors).each(function (index, element) {
        if ($(element).val().length > 0 || element.autofocus || $(this).attr('placeholder') !== undefined || $(element)[0].validity.badInput === true) {
            $(this).addClass('has-value');
        }
        else {
            $(this).removeClass('has-value');
        }
    });

    $(selectors).on("blur change keyup", function () {
        if ($(this).val()) {
            $(this).addClass('has-value');
        } else {
            $(this).removeClass('has-value');
        }
    });

    $(window).scroll(function () {
        if ($(this).scrollTop() > 50) {
            $('#back-to-top').fadeIn();
        } else {
            $('#back-to-top').fadeOut();
        }
    });
    var bTopSelector = '#back-to-top';
    $(bTopSelector).click(function () {
        $('#back-to-top').tooltip('hide');
        $('body,html').animate({
            scrollTop: 0
        }, 800);
        return false;
    });

    $(bTopSelector).tooltip('show');
});