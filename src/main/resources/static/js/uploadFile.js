$(document).ready(function () {

    $('#uploadFileBtn').change(function () {
        $('#uploadFileError').removeClass('alert alert-danger').text('');
        $('#fileUploadSubmit').removeAttr("disabled");
        if (this.files[0].size > 524288) {
            $('#uploadFileError').html('File size must be 512KB or below').addClass('alert alert-danger');
            $('#fileUploadSubmit').attr("disabled", "disabled");
        }
    });

    $('#fileUploadSubmit').click(function (e) {
        if ($('#uploadFileBtn')[0].files[0].size > 524288) {
            e.preventDefault();
        }
    });
});