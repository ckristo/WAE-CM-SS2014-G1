/** *************** **/
/** * File Upload * **/
/** *************** **/
$(document).ready(function () {
    $("#donateForm").fileupload({
	    url: '@routes.Donate.upload()',
        sequentialUploads: true,
        autoUpload: false,
        dataType: 'json',
    	maxFileSize: 5000000,
    	acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
        done: function (e, data) {
	        alert(data);
            /* $.each(data.result.files, function (index, file) {
                $('<p/>').text(file.name).appendTo(document.body);
            }); */
        }
    });
});