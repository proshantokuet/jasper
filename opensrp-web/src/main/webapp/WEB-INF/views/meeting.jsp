<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url var="postUrl" value="/rest/api/v1/meeting/save-or-update" />
<c:url var="listUrlMeeting" value="/other-meeting-minutes/list.html" />
<c:url var="listUrlWorkPlan" value="/workplan-meeting/list.html" />
<c:url var="delete" value="/workplan-meeting/delete" />
<script>
 
 $("#meeting").submit(function (event) {
		$("#loading").show();
		var url = '${postUrl}';
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		var form = document.getElementById("meeting");
		
		if(!validateImage()){
			return false;
		}
		var listUrl = "";
		var type= $("#type").val();
		if(type=='WP'){
			listUrl="${listUrlWorkPlan}";
		}else{
			listUrl="${listUrlMeeting}";
		}
		
	   	
	   
	    var formData = new FormData(form);
	 
		event.preventDefault();
		console.log(formData);
		
		$.ajax({
			cache: false,
	        contentType: false,
	        processData: false,        	
	      	url:  url,     	
	     	data: formData,
	      	dataType : 'json',
	      	type:"POST",
	      	beforeSend: function(xhr) {
	      		$(".profile_image").css("display","none") ;
	          xhr.setRequestHeader(header, token);
			},
			success : function(data) { 
				let response = JSON.parse(data);
				if (response.status == "CREATED") {
					$('#loading').hide();
					$("#successMessage").removeClass( "hide" );           	  
		            $("#successMessageContent").html(response.msg);
					setTimeout(function(){            		
						window.location.replace(listUrl);
		                }, 1000);
					
				} else {
					$('#errorMessage').html(response.msg);
					$('#errorMessage').show();
					$('#loading').hide();
				}
			},
			error : function(e) {
				$('#loading').hide();
				$('#errorMessage').html(data);
				$('#errorMessage').show();
			},
			complete : function(e) {
				$("#loading").hide();
				console.log("DONE");
			}
		});
	});
	
	function showHideOtherPlace(){	
		var roleName= $("#meetingPlaceOptions option:selected").text();
		if(roleName == 'Other'){
			$("#op").show(); 
			
		}else{
			$("#op").hide(); 			
			$("#meetingPlaceOther").val('');
		}
		
	}
	
	function validateImage(){
		var totalfiles = document.getElementById('picture').files.length;
		var ext="";
		for (var index = 0; index < totalfiles; index++) {			
			ext = document.getElementById('picture').files[index].name.split('.').pop().toLowerCase();
			console.log(ext);
			if($.inArray(ext, ['gif','png','jpg','jpeg','pdf','doc','docx','xls','xlsx']) == -1) {
		    	$(".profile_image").html("File type is not valid, Please select file type ['gif','png','jpg','jpeg''pdf','doc','docx','xls','xlsx']");
	     		$(".profile_image").css("display","block") ;
		    	document.getElementById("picture").value = "";
		        return false;
		    }
		}
		return true;
	}
	$( function() {
	    $( "#meetingDate" ).datepicker({
	    	dateFormat: 'yy-mm-dd',
	    	changeYear: true,
	    	maxDate:new Date(),
	    	
	    });
	});
	
	function del(id){
		$.ajax({
			type:"GET",
	      	url:  "${delete}"+"/"+id,
	      	dataType : 'html',	      
	      	timeout : 300000,
	      	beforeSend: function(xhr) {	      	  
	          //xhr.setRequestHeader(header, token);
			},
			success : function(data) { 
				alert(data);
				$("#fileList").html(data);
			},
			error : function(e) {
				$('#loading').hide();
				$('#errorMessage').html(data);
				$('#errorMessage').show();
			},
			complete : function(e) {
				$("#loading").hide();
				console.log("DONE");
			}
		});
	}
</script> 