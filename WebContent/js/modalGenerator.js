$(document).ready(function() {
	$("body").append(						
			"			<div class='modal fade' id='modal-container-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"+
			"				<div class='modal-dialog'>"+
			"					<div class='modal-content'>"+
			"						<div class='modal-header'>"+
			"							<button type='button' class='close' data-dismiss='modal' aria-hidden='true' onclick='close()'>×</button>"+
			"							<h4 class='modal-title' id='alertTitle'></h4>"+
			"						</div>"+
			"						<div class='modal-body'>"+
			"							<div id='alertbody'></div>"+
			"						</div>"+
			"						<div class='modal-footer'>"+
			"							<button type='button' class='btn btn-default' data-dismiss='modal' onclick='close()'>關閉</button>"+
			"						</div>"+
			"					</div>"+
			"				</div>"+
			"			</div>");
});




function modalGenerator(title, body){
	$('#alertTitle').empty(title);
	$('#alertbody').empty(body);
	$('#alertTitle').append(title);
	$('#alertbody').append(body);
	$('#modal-container-1').modal('show');
}

function close(){
	window.location.href = "Homepage.html";
}
