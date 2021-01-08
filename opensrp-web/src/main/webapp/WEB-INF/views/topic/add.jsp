<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="org.opensrp.common.util.CheckboxHelperUtil"%>
<%@page import="java.util.List"%>
<%@page import="org.opensrp.core.entity.Permission"%>
<jsp:include page="/WEB-INF/views/header.jsp" />

    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
  <link type="text/css"
	href="<c:url value="/resources/css/magicsuggest-min.css"/>"
	rel="stylesheet"
	http-equiv="Cache-control" content="public">  


<c:url var="postUrl" value="/rest/api/v1/topic/save" />
<c:url var="listUrl" value="/topic-list.html" />

<div class="page-content-wrapper">
		<div class="page-content">
			
			
			<ul class="page-breadcrumb breadcrumb">
				<li>
					<a href="<c:url value="/"/>">Home</a>
					<i class="fa fa-circle"></i>
				</li>
				<li>
					<a href="<c:url value="/topic-list.html"/>">Topic list</a>
					<i class="fa fa-circle"></i>
				</li>
			
			</ul>
			<!-- END PAGE BREADCRUMB -->
			<!-- END PAGE HEADER-->
			<!-- BEGIN PAGE CONTENT-->
			
	
			<div class="row">
				<div class="col-md-12">
					
					<!-- BEGIN EXAMPLE TABLE PORTLET-->
					<div class="portlet box blue-madison">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-edit"></i>Add new topic
							</div>
							
							
						</div>
						
						<div class="portlet-body">
							<form:form  id="topic" class="validator-form" autocomplete="off">
								<div class="form-group">
					              <div class="row">
					                <div class="col-lg-3 form-group"> 
						                <label class="control-label" for="name"> Name <span class="required">* </span>	</label>
										<input name="name" class="form-control" 	required="required" />
					                 </div>
					                 <div class="col-lg-3 form-group"> 
						                <label class="control-label" for="name"> Type <span class="required">* </span>	</label>
										<input name="type" class="form-control" id="type" required="required" />
					                 	<span id="t-error" style="color: red"> </span>  
					                 </div>
					                  <div class="col-lg-6 form-group">
					                   <label class="control-label" for="code"> Message <span class="required">* </span></label>
										<input name="message"  id="message" type="text">  
										<span id="m-error" style="color: red"> </span>    
					                  </div>
					                 
					              </div>
					            </div> 
								
								
								<div id="errorMessage" style="display:none;color: red;text-align: right;">
										  
								</div>	
								
								 <hr class="dotted"> 
								<div class="form-group text-right">
					                <button type="submit" id="submit-form"  class="btn btn-primary" name="signup" value="Validate">Submit</button>                
					                <a class="btn btn-info" href="${listUrl}">Cancel</a>
					            </div>
							</form:form>
							
							
							
							
						</div>
					</div>
					
					
					
					
					
				</div>
			</div>
			<!-- END PAGE CONTENT-->
		</div>
	</div>
	<!-- END CONTENT -->
</div>
<script>
jQuery(document).ready(function() {       
	 Metronic.init(); // init metronic core components
		Layout.init(); // init current layout
   //TableAdvanced.init();
});
</script>

<jsp:include page="/WEB-INF/views/footer.jsp" />

<jsp:include page="/WEB-INF/views/magicsuggest.jsp" />
	<script src="<c:url value='/resources/js/magicsuggest.js' />"></script>
	

 <script>
	$("#topic").submit(function (event) {
		$("#loading").show();
		var url = '${postUrl}';
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		var msg = [];
		var types = '';
		
		$('input[name^="type"]').each(function() { 
        	types += $(this).val()+",";
        }); 
		if(types ==''){
			$('#t-error').html("Required");
        	return false;
		}
		types = types.replace(/,\s*$/, "");
		
		
		$('input[name^="message"]').each(function() {           
        	msg.push($(this).val());
        });  
        $('#m-error').html("");
        $('#t-error').html("");
        if(msg.length ==0 ){
        	 $('#m-error').html("Required");
        	return false;
        }
        
        if(msg.length ==0 ){
       	 $('#m-error').html("Required");
       	return false;
       }
        
		var formData = {
				'id':0,
			'name': $('input[name=name]').val(),
			'messages': msg,
			'status': true,
			'type': types
		};

		event.preventDefault();
		console.log(formData);
		
		$.ajax({
			contentType : "application/json",
			type: "POST",
			url: url,
			data: JSON.stringify(formData),
			dataType : 'json',

			timeout : 100000,
			beforeSend: function(xhr) {
				$('#errorMessage').hide();
				$('#errorMessage').html("");
				xhr.setRequestHeader(header, token);
			},
			success : function(data) { 
				let response = JSON.parse(data);
				if (response.status == "CREATED") {
					$('#loading').hide();
					window.location.replace('${listUrl}');
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
</script> 

