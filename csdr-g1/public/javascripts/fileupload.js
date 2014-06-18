/** *************** **/
/** * File Upload * **/
/** *************** **/
var donationImageCount = 0;
function initFileUpload(actionUrl) {
	$("#donateForm").fileupload({
	    url: actionUrl,
        sequentialUploads: true,
        autoUpload: true,
        dataType: 'json',
    	maxFileSize: 5000000,
    	acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
    	previewCanvas: false,
    	added: function (e, data) {
    		registerCancelButtonEventHandler();
        },
        done: function (e, data) {
        	var file = data.result.file;
        	addFileInput("" + file.filename, "" + file.randomFilename);
        }
    });
}

function registerCancelButtonEventHandler() {
	$("li.template-upload .btn-danger").click(function(event) {
		event.preventDefault();
		$(this).parents("li").fadeOut('fast');
		return false; // prevent form submission
	});
}

function addFileInput(filename, randomFilename) {
	$("#donateForm").append('<input type="hidden" name="file_' + donationImageCount + '-filename" value="' + filename + '">');
	$("#donateForm").append('<input type="hidden" name="file_' + donationImageCount + '-tmpname" value="' + randomFilename + '">');
    donationImageCount++;
	$("input[name='numberOfFiles']").val(donationImageCount);
}