<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="org.opensrp.common.util.CheckboxHelperUtil"%>
<%@page import="java.util.List"%>
<%@page import="org.opensrp.core.entity.Permission"%>
  <title><spring:message code="lbl.addUnion"/></title>
<jsp:include page="/WEB-INF/views/header.jsp" />

    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
  
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="org.opensrp.core.entity.LocationTag"%>
<%@page import="org.json.JSONObject" %>
<%@page import="org.json.JSONArray" %>
<%@ page import="org.opensrp.common.util.LocationTags" %>


<c:url var="listUrl" value="/location/union-list.html" />
<c:url var="saveUrl" value="/location/add-new.html" />
<c:url var="saveRestUrl" value="/rest/api/v1/location/save" />
<div class="page-content-wrapper">
		<div class="page-content">
			
			
			<ul class="page-breadcrumb breadcrumb">
				<li>
					<a href="<c:url value="/"/>">Home</a>
					<i class="fa fa-circle"></i>
				</li>
				<li>
					<a href="<c:url value="/location/union-list.html"/>">
					Back</a>
					
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
								<i class="fa fa-edit"></i><spring:message code="lbl.addUnion"/>
							</div>
							
							
						</div>
						 <div id="loading" style="display: none;position: absolute; z-index: 1000;margin-left:45%">
                    	<img width="50px" height="50px" src="<c:url value="/resources/images/ajax-loading.gif"/>"></div>
						
						<div class="portlet-body">
							<form:form  class="validator-form"  id="locationInfo">
								<div class="form-group">
					              <div class="row">
					                <div class="col-lg-6 form-group"> 
						                <label class="control-label" for="name"> <spring:message code="lbl.unionName"/> <span class="required">* </span>	</label>
										<input id="name" class="form-control" name="name" required="required"/>
					                 </div>
					                  <div class="col-lg-6 form-group">
					                   <label class="control-label" for="code"><spring:message code="lbl.unionCode"/> <span class="required">* </span></label>
										<input maxlength="3" id="code" class="form-control" name="code" required="required"/>
					                  </div>
					                 
					              </div>
					            </div> 
								  <input id="locationTag" value="${locationTag}" hidden/>
								  <input id="parentLocation" name="parentLocation" hidden/>
								 
								<div class="form-group">
					              <div class="row">
					                <div class="col-lg-6 form-group"> 
						                <label class="control-label" for="name"> <spring:message code="lbl.description"/><span class="required">* </span>	</label>
										 <input id="description" class="form-control" name="description" required="required" />
					                 </div>
					                   <div id="division" class="col-lg-6 form-group">
					                    <label><spring:message code="lbl.division"/><span style="color: red;">*</span></label>
				                        <select id="division-option" class="form-control" name="divisionId">
                                    	</select>
					                  </div>
					                 
					              </div>
					            </div>
					            

			                    <div class="form-group">
								     <div class="row">
			                            <div id="district" class="col-lg-6 form-group">
			                                <div class="ui-widget">
			                                    <label class="control-label"><spring:message code="lbl.district"/><span style="color: red;">*</span></label>
			                                    <select id="district-option" class="form-control" name="districtId">
			                                    </select>
			                                </div>
			                         </div>
			                        
			                     
			                          <div id="upazila" class="col-lg-6 form-group">
			                                <div class="ui-widget">
			                                    <label class="control-label"><spring:message code="lbl.upazila"/><span style="color: red;">*</span></label>
			                                    <select id="upazila-option" class="form-control" name="upazilaId">
			                                    </select>
			                                </div>
			                          </div>
			                  		</div>
								</div>  
								<div class="form-group">
									<div class="row">
										
			                            <div id="pourasabha" class="col-lg-6 form-group">
			                                <div class="ui-widget">
			                                    <label><spring:message code="lbl.pourasabha"/><span style="color: red;">*</span></label>
			                                    <select id="pourasabha-option" class="form-control" name="pourasabhaId">
			                                    </select>
			                                </div>
			                            </div>
					                        
				                  	 </div>
								</div>   
									<%-- <div id="division" class="form-group" style="display: none;">
				                        <div class="row">
				                            <div class="col-lg-6 form-group">
				                                <div class="ui-widget">
				                                    <label><spring:message code="lbl.division"/><span style="color: red;">*</span></label>
				                                    <select id="division-option" class="form-control" name="divisionId">
				                                    </select>
				                                </div>
				                            </div>
				                        </div>
				                    </div> --%>
				
				                    
				
				                    
				
				                   
				                  
				
				                   
								
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
 <script>
    $(document).ready(function () {
        getLocationHierarchy("/opensrp-dashboard/location-by-tag-id?id=<%=LocationTags.DIVISION.getId()%>&title=", "division-option");
    });

    $("#division-option").change(function () {
        getLocationHierarchy("/opensrp-dashboard/child-locations?id="+$("#division-option").val()+"&title=", "district-option")
    });

    $("#district-option").change(function () {
        getLocationHierarchy("/opensrp-dashboard/child-locations?id="+$("#district-option").val()+"&title=", "upazila-option")
    });

    $("#upazila-option").change(function () {
        getLocationHierarchy("/opensrp-dashboard/child-locations?id="+$("#upazila-option").val()+"&title=", "pourasabha-option")
    });

    $('#locationInfo').submit(function(event) {
        event.preventDefault();
        var visitLocation = false, loginLocation = false;
        if ($('#visitLocation').is(":checked")) {
            loginLocation = true;
        }
        if ($('#loginLocation').is(":checked")) {
            visitLocation = true;
        }
        let url = "/opensrp-dashboard/rest/api/v1/location/save";
        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");
        let formData = {
            'name': $('#name').val(),
            'code': $('#code').val(),
            'description': $('#description').val(),
            'parent_location_id': $('#pourasabha-option').val(),
            'location_tag_id': $('#locationTag').val(),
            'division_id': $('#division-option').val(),
            'district_id': $('#district-option').val(),
            'upazila_id': $('#upazila-option').val(),
            'pourasabha_id': $('#pourasabha-option').val(),
            'union_id': $('#union-option').val(),
            'created_at': new Date(),
            'is_login_location': visitLocation,
            'is_visit_location': loginLocation
        };

        console.log(formData);

        $.ajax({
            contentType : "application/json",
            type: "POST",
            url: url,
            data: JSON.stringify(formData),
            dataType : 'json',

            timeout : 100000,
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
                $("#loading").show();
            },
            success : function(data) {
                $("#loading").hide();
                $('#locationErrorMessage').html("");
                if(data == ""){
                    window.location.replace("/opensrp-dashboard/location/union-list.html");
                } else {
                    $('#locationErrorMessage').html(data);
                }
                console.log(data);
            },
            error : function(data) {
                $('#locationErrorMessage').html("");
                $("#loading").hide();
                if (data.status == 0) {
                    $('#locationErrorMessage').html("Check your internet connection and try again...");
                } else {
                    $('#locationErrorMessage').html("Something bad happened, please contact with admin...");
                }
                console.log("IN ERROR");
                console.log(data);
            },
            complete : function(data) {
                $("#loading").hide();
                console.log("IN COMPLETE");
                console.log(data.status);
            }
        });
    });

    function getLocationHierarchy(url, id) {
        $("#"+id).html("");
        $.ajax({
            type : "GET",
            contentType : "application/json",
            url : url,

            dataType : 'html',
            timeout : 100000,
            beforeSend: function() {},
            success : function(data) {
                $("#"+id).html(data);
            },
            error : function(e) {
                console.log("ERROR: ", e);
                display(e);
            },
            done : function(e) {

                console.log("DONE");
                //enableSearchButton(true);
            }
        });

    }

</script>

