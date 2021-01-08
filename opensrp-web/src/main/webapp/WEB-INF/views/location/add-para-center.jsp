<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="org.opensrp.common.util.CheckboxHelperUtil"%>
<%@page import="java.util.List"%>
<%@page import="org.opensrp.core.entity.Permission"%>
 <title><spring:message code="lbl.addParaCenter"/></title>
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

<style>
	.ui-datepicker-calendar {
   display:none;
}
	</style>
<c:url var="listUrl" value="/location/para-center-list.html" />
<c:url var="saveUrl" value="/rest/api/v1/location/para-center/save" />

<c:url var="saveRestUrl" value="/rest/api/v1/location/save" />
<div class="page-content-wrapper">
		<div class="page-content">
			
			
			<ul class="page-breadcrumb breadcrumb">
				<li>
					<a href="<c:url value="/"/>">Home</a>
					<i class="fa fa-circle"></i>
				</li>
				<li>
					<a href="<c:url value="/location/para-center-list.html"/>">
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
								<i class="fa fa-edit"></i> <spring:message code="lbl.addParaCenter"/>
							</div>
							
							
						</div>
						 <div id="loading" style="display: none;position: absolute; z-index: 1000;margin-left:45%">
                    	<img width="50px" height="50px" src="<c:url value="/resources/images/ajax-loading.gif"/>"></div>
						
						<div class="portlet-body">
							<form:form  class="validator-form"  id="paraCenterInfo">
								<div class="form-group">
					              <div class="row">
					                <div class="col-lg-6 form-group"> 
						                <label class="control-label" for="name"> <spring:message code="lbl.paraCenterName"/> <span class="required">* </span>	</label>
										<input id="name" class="form-control" name="name" required="required"/>
					                 </div>
					                  <div class="col-lg-6 form-group">
					                   <label class="control-label" for="code"><spring:message code="lbl.paraCenterCode"/> <span class="required">* </span></label>
										<input maxlength="1" id="code" class="form-control" name="code" required="required"/>
					                  </div>
					                 
					              </div>
					            </div>
					            
					            <div class="form-group">
					              <div class="row">
					                <div class="col-lg-6 form-group"> 
						                <label class="control-label" for="name"> Established(year) <span class="required">* </span>	</label>
										<input readonly="readonly" id="established" class="form-control" name="established" required="required"/>
					                 </div>
					                  <div class="col-lg-6 form-group">
					                   <label class="control-label" for="code">Para Kormi Name <span class="required">* </span></label>
										 <input id="paraKormiName" class="form-control" name="paraKormiName" required="required"/>
					                  </div>
					                 
					              </div>
					            </div> 
					            
								  <input id="locationTag" value="${locationTag}" hidden/>
								  <input id="parentLocation" name="parentLocation" hidden/>
								 
								<div class="form-group">
					              <div class="row">
					                <div class="col-lg-6 form-group"> 
						                <label class="control-label" for="dhis2Id"> DHIS2 ID <span class="required">* </span>	</label>
										 <input id="dhis2Id" class="form-control" name="dhis2Id" required="required" />
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
			                            
			                            <div id="union" class="col-lg-6 form-group">
			                                <div class="ui-widget">
			                                    <label><spring:message code="lbl.union"/><span style="color: red;">*</span></label>
			                                   <select id="union-option" class="form-control" name="unionId">
                                    		   </select>
			                                </div>
			                            </div>
					                        
				                  	 </div>
								</div>
								
								<div class="form-group">
									<div class="row">
										
			                            <div id="para" class="col-lg-6 form-group">
			                                <div class="ui-widget">
			                                    <label><spring:message code="lbl.village"/><span style="color: red;">*</span></label>
			                                    <select id="para-option" class="form-control" name="paraId">
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

    $("#pourasabha-option").change(function () {
        getLocationHierarchy("/opensrp-dashboard/child-locations?id="+$("#pourasabha-option").val()+"&title=", "union-option")
    });

    $("#union-option").change(function () {
        getLocationHierarchy("/opensrp-dashboard/child-locations?id="+$("#union-option").val()+"&title=", "para-option")
    });

    $('#paraCenterInfo').submit(function(event) {
        event.preventDefault();
        let url = '${saveUrl}';
        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");
        let formData = {
            'name': $('#name').val(),
            'code': $('#code').val(),
            'established': $('#established').val(),
            'para_kormi_name': $('#paraKormiName').val(),
            'division_id': $('#division-option').val(),
            'district_id': $('#district-option').val(),
            'upazila_id': $('#upazila-option').val(),
            'pourasabha_id': $('#pourasabha-option').val(),
            'union_id': $('#union-option').val(),
            'para_id': $('#para-option').val(),
            'date_created': new Date(),
			'dhis2_id': $('#dhis2Id').val()
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
                    window.location.replace("/opensrp-dashboard/location/para-center-list.html");
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
    
    $(function() {
        $('#established').datepicker({
            changeYear: true,
            showButtonPanel: true,
            dateFormat: 'yy',
            maxDate:new Date(),
            onClose: function(dateText, inst) { 
                var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                $(this).datepicker('setDate', new Date(year, 1));
            }
        });
    $(".date-picker-year").focus(function () {
            $(".ui-datepicker-month").hide();
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

