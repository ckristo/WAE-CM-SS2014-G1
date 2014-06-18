/** *************** **/
/** * File Upload * **/
/** *************** **/
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
	$("li.template-upload .btn-danger").unbind();
	$("li.template-upload .btn-danger").click(function(event) {
		event.preventDefault();
		var parent = $(this).parents("li");
		var index = parent.index();
		var currentNumber = $("input[name='numberOfFiles']").val();
		
		// Remove corresponding inputs
		$('input[name^="file_' + index + '"]').remove();
		
		// Adjust indices
		for(var i = (index + 1);i<currentNumber;i++) {
			$('input[name="file_' + i + '-filename"]').attr("name", "file_" + (i - 1) + "-filename");
			$('input[name="file_' + i + '-tmpname"]').attr("name", "file_" + (i - 1) + "-tmpname");
		}
		
		// Update files number
		if(currentNumber > 0) { currentNumber--; }
		$("input[name='numberOfFiles']").val(currentNumber);

		parent.fadeOut('fast');
		parent.remove();
		
		return false; // prevent form submission
	});
}

function addFileInput(filename, randomFilename) {
	var currentNumber = $("input[name='numberOfFiles']").val();
	
	$("#donateForm").append('<input type="hidden" name="file_' + currentNumber + '-filename" value="' + filename + '">');
	$("#donateForm").append('<input type="hidden" name="file_' + currentNumber + '-tmpname" value="' + randomFilename + '">');
    
    currentNumber++;
	$("input[name='numberOfFiles']").val(currentNumber);
}