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

<link type="text/css" href="<c:url value="/resources/css/jquery-ui.css"/>" rel="stylesheet" http-equiv="Cache-control" content="public">
<c:url var="postUrl" value="/rest/api/v1/meeting/save-or-update" />
<c:url var="listUrl" value="/workplan-meeting/list.html" />

<c:url var="uploadUrl" value="/rest/api/v1/meeting/upload" />

<div class="page-content-wrapper">
		<div class="page-content">
			
			
			<ul class="page-breadcrumb breadcrumb">
				<li>
					<a href="<c:url value="/"/>">Home</a>
					<i class="fa fa-circle"></i>
				</li>
				<li>
					<a href="<c:url value="/workplan-meeting/list.html"/>">Back</a>
					
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
								<i class="fa fa-edit"></i>Add work plan meeting
							</div>
							
							
						</div>
						
						<div class="portlet-body">
							<form:form  id="meeting" class="validator-form" autocomplete="off" enctpye="multipart/form-data">
								<div class="form-group">
					              <div class="row">
					                <div class="col-lg-6 form-group"> 
						                <label class="control-label" for="name"> Work Plan Type <span class="required">* </span>	</label>
										<select required="required" name="meetingOrWorkPlaneType" id="meetingOrWorkPlaneType" class="form-control">
											<option value="">Please Select</option>
					                        <c:forEach items="${meetingTypes}" var="meetingType">					                        	
					                        	<c:choose>
					                        	 <c:when test="${meetingType.id ==meetingTypeId}">
					                        	 <option value="${meetingType.id}" selected="selected">${meetingType.name}</option>
					                        	 </c:when>
					                        	 <c:otherwise>
					                        	 <option value="${meetingType.id}">${meetingType.name}</option>
					                        	 </c:otherwise>
					                        	</c:choose>
					                        </c:forEach>
										  </select>
					                 </div>
					                  <div class="col-lg-6 form-group">
					                   <label class="control-label" for="code"> Work Plan Made on <span class="required">* </span></label>
										<input name="meetingDate" required="required" id="meetingDate" type="text" class="form-control">  
										
					                  </div>
					                 
					              </div>
					              
					              <div class="row">
					                <div class="col-lg-6 form-group"> 
						                <label class="control-label" for="name"> Meeting Place </label>
										<select onClick="showHideOtherPlace()" name="meetingPlaceOptions" id="meetingPlaceOptions" class="form-control">
											<option value="0">Please Select</option>
					                        <c:forEach items="${meetingPlaces}" var="meetingPlace">					                        	
					                        	
					                        	 <option value="${meetingPlace.id}" >${meetingPlace.name}</option>
					                        	
					                        </c:forEach>
										  </select>
					                 </div>
					                  <div class="col-lg-6 form-group" style="display:none"  id="op">
					                   <label class="control-label" for="code"> Other place name </label>
										<input name="meetingPlaceOther" id="meetingPlaceOther" type="text" class="form-control">  
										
					                  </div>
					                 
					              </div>
					              
					              
					              <div class="row">
					               
					                  <div class="col-lg-6 form-group">
					                   <label class="control-label" for="code"> Discussion & Action Points </label>
										<textarea   id="description" name="description" style="margin: 0px -11px 0px 0px; height: 110px; width: 100%;" class="input-md form-control"></textarea>
				                 	
					                  </div>
					                  
					                   <div class="col-lg-6 form-group"> 
						                <label class="control-label" for="name"> Total Participants <span class="required">* </span>	</label>
										<input name="totalParticipants"  min="1" required="required" id="totalParticipants" type="number" class="form-control">  
										
					                 </div>
					                 
					              </div>
					              
					              <div class="row">
					                <div class="col-lg-6 form-group"> 
						                <label class="control-label" for="name"> Add files 	</label>
										<input type="file"  name="picture" multiple  id="picture" class="form-control"/>
										
										<span class="profile_image" style="display:none; color:red;">['gif','png','jpg','jpeg','pdf','doc','docx','xls','xlsx']</span>   		
					                
					                  <div id="fileList">
					                	
					                	<label class="control-label" for="code"> Uploaded file list: </label>
					                   
					                  </div>
					                
					                 </div>
					                  <input name="filename"  id="filename" type="hidden">  
					                   <input name="type"  id="type" value="WP" type="hidden">
					                    <input name="id"  id="id" value="0" type="hidden">
										
					                 
					           
					           
					           
					            </div> 
								
								<div class="form-group hide" id="successMessage">
						              <div class="row">
						                 <div id="successMessageContent" style="color:green;text-align: right;font-weight: bold;" class="col-lg-12 form-group"> 
						                	
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

<jsp:include page="/WEB-INF/views/meeting.jsp" />

