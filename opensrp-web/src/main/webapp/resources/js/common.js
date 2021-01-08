$(document).ready(function () {	
	
	 $("#picture").change(function(){	 	
	     readURL(this);
	 });
	$( "#dateOfBirth" ).datepicker({
		  dateFormat: 'dd/mm/yy',
	      changeMonth: true,
	      changeYear: true,
	      yearRange:'-110:+0',
	      defaultDate: '1/1/2000'
	 });
	
	
	$( "#medicalRegistrationExpiryDate" ).datepicker({
		  dateFormat: 'dd/mm/yy',
	      changeMonth: true,
	      changeYear: true ,	      
	      minDate: new Date()
	     
	 });
	
	$(document.body).on('click','.Delete', function(){
		var parentId = ($(this).parent().attr('id'));
		$("#"+parentId).remove();
	});

		
	/**
	 * user account type & email validation
	 * */
	$(document.body).on('click','#user', function(){
		var role = $("#role_id").val();
		var email = $("#email").val()
		var password = $("#password").val()
		var repeatpassword = $("#retypePassword").val()
		var emailTest = checkemail(email)
		if($("#name").val() == "" || $("#name").val() ==null){
			$(".name_error").css("display","block")
			return false
		}else{
			$(".name_error").css("display","none")
		}
		if(emailTest == false){
			$(".email_error").css("display","block")
			return false
		}else{
			$(".email_error").css("display","none")
		}		
		if(password.length < 8){
			$(".shortPassword").css("display","block")
			return false;
		}else{
			$(".shortPassword").css("display","none")
		}
		if(password != repeatpassword){
			$(".repeatPassword").css("display","block")
			return false
		}else{
			$(".repeatPassword").css("display","none")
		}
		if(role == ''){
			$(".accountType").css("display","block")
			return false
		}		
	});
	
	
	$(document.body).on('click','#login', function(){		
		var email = $("#email").val()
		var emailTest = checkemail(email)
		if(emailTest == false){
			$(".email_error").css("display","block")
			return false
		}else{
			$(".email_error").css("display","none")
		}	
	});
	
	$(document.body).on('keyup','.validate', function(){		
		var id = $(this).attr("id")	
		var value = $("#"+id).val();	
		var success			
		var filter=/^[a-zA-Z, .'-]+$/;
		if (filter.test(value) || value == ""){
			$("#validmessage"+id).css("display","none")
			success=true
		}else{			
			//$("#"+id).val(value.substring(0, value.length - 1));
			$("#"+id).val(value.replace(/[^a-zA-Z, .'-]/g,''));
			$("#validmessage"+id).css("display","block");
			success=false
		}
		return (success)
	});
	
	
	//Encounter mobile number of name validity check
	$(document.body).on('keyup','.encounterValidate', function(){		
		var id = $(this).attr("id")	
		var value = $("#"+id).val();	
		var success			
		var filter=/^[0-9+a-zA-Z, .'-]+$/;
		if (filter.test(value) || value == ""){
			$("#validmessagename").css("display","none")
			success=true
		}else{			
			$("#"+id).val(value.substring(0, value.length - 1));
			$("#validmessagename").css("display","block")
			success=false
		}
		return (success)
	});
		
	$(document.body).on('keyup','.mobiles', function(){		
		var id = $(this).attr("id")	
		var value = $("#"+id).val();	
		var success			
		var filter=/^[0-9+]+$/;
		if (filter.test(value)){
			$("#validmessagemobileNumber"+id).css("display","none")
			success=true
		}else{			
			$("#"+id).val(value.substring(0, value.length - 1));
			$("#validmessagemobileNumber"+id).css("display","block")
			success=false
		}
		return (success)
		
	});
	/**
	 * password field empty validation 
	 * when user trying to change password
	 * */
	$(document.body).on('click','#pass', function(){
		var password = $("#password").val();
		if(password.length < 8){
			
			$(".shortPassword").css("display","block")
			return false;
		}else{
			$(".shortPassword").css("display","none")
		}
	});
	
	/**
	 * start and enddate  field empty validation 
	 * when user trying to search audit trail
	 * */
	$(document.body).on('click','#auditTrail', function(){
		var start = $("#start").val();
		var end = $("#end").val();
		if(start =="" || end ==""){
			alert("Please select start and end date");
			return false;
		}/*else if(start>end){
			alert("End date should be later or equal to Start date");
			return false;
		}*/else{
			return true;
		}		
		
	});
	
	
	
	$(document.body).on('click','#role', function(){
		var name = $("#name").val();		
		if(name ==""){
			alert("Role name can't be empty.");
			return false;
		}else{
			return true;
		}		
		
	});
	
	
	/**
	 * inplement select all checkbox functionality for  
	 *  role add or edit page
	 * */
	$(document).ready(function() {
		var counter = 0;// For counting that how many checkbox are true
		$('.acl').each(function(){
			if(this.checked)counter++;
		});
		
		$('#selecctall').click(function(event) { // on click
			if (this.checked) { // check select status
				$('.acl').each(function() { // loop through each checkbox
					this.checked = true; // select all checkboxes
				});
			} else if(!this.checked){
				$('.acl').each(function() { // loop through each checkbox
					this.checked = false; // deselect all checkboxes
					// with class "checkbox1"
				});
			}
		});

		$('.acl').click(function(event) { // on click
			if (!this.checked)counter--;
			else if (this.checked)counter++;
			
			if (counter == 9) {
				document.getElementById("selecctall").checked = true;
			}else{
				document.getElementById("selecctall").checked = false;
			}
		});

	});
	

	
	 $(document.body).on('click', '#export' ,function() {		
			
			var doctor = $("#doctor").val();
			if(doctor == "" || doctor == null){
				doctor = "N";
			}
			
			var mobile = $("#mobile").val();
			if(mobile == "" || mobile == null){
				mobile = "N";
			}
			
			var start = $("#start").val();
			if(start == "" || start == null){
				start = "N";
			}
			var end = $("#end").val();
			if(end == "" || end == null){
				end = "N";
			}
			
			
			$.ajax({
			    async:true,		   
			    dataType: "html",
			    url:"/export/"+doctor+"/"+mobile+ "/"+start+"/"+end,
			    success:function (data) {			    	
			    	window.location = "/report/"+data;			    	
					$("#waits").css('display','none');
					$("#parentLocation").html(data);
			    },
			    type:"post"
						
			});			
		return false; 
	    });	
	 
	 
	 $(document.body).on('click', '.role_method' ,function() {		 
			var id = $(this).val();
			var value = id.split(",");
			 
			 if (!$(this).is(':checked')) {				
				 $.ajax({
				    async:true,		   
				    dataType: "html",
				    url:"/role_method/"+value[1]+"/"+value[0]+"/"+1,
				    success:function (data) {
						$("#waits").css('display','none');						
				    },
				    type:"post"
							
				 });	
				 return true; 
				 
			 }else{
				 $.ajax({
					    async:true,		   
					    dataType: "html",
					    url:"/role_method/"+value[1]+"/"+value[0]+"/"+0,
					    success:function (data) {
							$("#waits").css('display','none');
							
					    },
					    type:"post"
								
					 });
				 return true
			 }
					
		return false; 
	    });
	 
	 $(document.body).on('click', '#profile' ,function() {	 
		var mobile = $("#mobile").val();
		var profile = $("#pid").val();
		if(mobile != ""){
			var decision = false;			
			$.ajax({
					async:false,		   
					dataType: "html",
				    url:"/profile/mobile_number_check/"+mobile+"/"+profile,
					success:function (data) {					
							if(data == "0"){							
								decision =  true;
							}else{
								$(".mobileDuplicate").css("display","block")							
								decision = false;
							}							
						 },
						  type:"post"						
				 });		
			return decision; 
		}else{
			
		}
			
	    });
	 
	
});

/**
 * check box validation in role model
 * */

function roleFormvalidation(){	
	var success = false;
	for (i = 0; i < document.role.elements['action[]'].length; i++){
        if (document.role.elements['action[]'][i].checked){
            success = true;
        }
    }
	
	if(success == false){		
		alert("Please select at least one action.")
	}
	return success;
}

function checkemail(email){
	var success	
	var filter=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i
	if (filter.test(email))
		success=true
	else{		
		success=false
	}
	return (success)
}
function checkPassword(password, retypePassword){
	if(password==retypePassword){
		return true;
	}else{
		return false;
	}
}
function checkPasswordLength(password){
	if(password.length<8){
		return false;
	}else{
		return true;
	}
}

function readURL(input) {

    if (input.files && input.files[0]) {
    	$("#image").css("display","block")
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#image').attr('src', e.target.result);
        }
        
        reader.readAsDataURL(input.files[0]);
    }
}


var _validFileExtensions = [".jpg", ".jpeg",".gif", ".png"];    
function ValidateSingleInput(oInput) {
    if (oInput.type == "file") {
        var sFileName = oInput.value;
         if (sFileName.length > 0) {
            var blnValid = false;
            for (var j = 0; j < _validFileExtensions.length; j++) {
                var sCurExtension = _validFileExtensions[j];
                if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                    blnValid = true;
                    break;
                }
            }
             
            if (!blnValid) {
                alert("Sorry, " + sFileName + " is invalid, allowed extensions are: " + _validFileExtensions.join(", "));
                oInput.value = "";
                return false;
            }
        }
    }
    return true;
}

