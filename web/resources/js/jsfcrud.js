function handleSubmit(args, dialog) {
    var jqDialog = jQuery('#' + dialog);
    if (args.validationFailed) {
        jqDialog.effect('shake', {times: 3}, 100);
    } else {
        PF(dialog).hide();
    }
}

$.fn.customFileInput = function () {

    return this.each(function () {
        var t = $(this),
                input = t.find('input'),
                fakeTrigger = t.find('.bt'),
                fakeInput = t.find('.fileName');

        input.change(function () {
            // get only file name, with out path
            var fileName = input.val().split('\\').pop();
            fakeInput.html(fileName);
        });

        input.click(function (e) {
            e.stopPropagation();
        })

        t.click(function (e) {
            e.preventDefault();
            input.click();
        });

    });
};

$(function () {
    $('.customFileInput').customFileInput();
});