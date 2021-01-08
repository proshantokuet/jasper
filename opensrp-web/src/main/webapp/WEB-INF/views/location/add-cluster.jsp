<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="org.opensrp.common.util.CheckboxHelperUtil"%>
<%@page import="java.util.List"%>
<%@page import="org.opensrp.core.entity.Permission"%>
<title><spring:message code="lbl.addCluster"/></title>
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

<%
	String randomCode = String.valueOf(System.currentTimeMillis());
	Integer codeLength = randomCode.length();
	randomCode = randomCode.substring(codeLength-5);
%>
<c:url var="listUrl" value="/location/cluster-list.html" />
<c:url var="saveUrl" value="/rest/api/v1/location/cluster/save" />

<div class="page-content-wrapper">
		<div class="page-content">
			
			
			<ul class="page-breadcrumb breadcrumb">
				<li>
					<a href="<c:url value="/"/>">Home</a>
					<i class="fa fa-circle"></i>
				</li>
				<li>
					<a href="<c:url value="/location/cluster-list.html"/>">
					 <spring:message code="lbl.clusterList"/></a>
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
								<i class="fa fa-edit"></i> <spring:message code="lbl.addCluster"/>
							</div>
							
							
						</div>
						 <div id="loading" style="display: none;position: absolute; z-index: 1000;margin-left:45%">
                    	<img width="50px" height="50px" src="<c:url value="/resources/images/ajax-loading.gif"/>"></div>
						
						<div class="portlet-body">
							<form:form  class="validator-form" id="clusterInfo">
								<div class="form-group">
					              <div class="row">
					                <div class="col-lg-6 form-group"> 
						                <label class="control-label" for="name"> <spring:message code="lbl.clusterName"/> <span class="required">* </span>	</label>
										<input id="name" class="form-control" name="name" required="required"/>
					                 </div>
					                  <div class="col-lg-6 form-group">
					                   <label class="control-label" for="code"><spring:message code="lbl.clusterCode"/> <span class="required">* </span></label>
										<input disabled="disabled" value="<%=randomCode%>" id="code" class="form-control" name="code" required="required"/>
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
			                            
			                            <div id="union" class="col-lg-6 form-group">
			                                <div class="ui-widget">
			                                    <label><spring:message code="lbl.union"/><span style="color: red;">*</span></label>
			                                  <select id="union-option" class="form-control mx-sm-3 js-example-basic-multiple" name="unionId" multiple="multiple" required>
                                    		</select>
			                                </div>
			                            </div>
					                        
				                  	 </div>
								</div>
								
								<div class="form-group">
									<div class="row">
			                            
			                            <div id="para-center" class="col-lg-6 form-group">
			                                <div class="ui-widget">
			                                    <label> <spring:message code="lbl.paraCenter"/><span style="color: red;">*</span></label>
			                                    <select class="form-control mx-sm-3 js-example-basic-multiple"
				                                        id="para-center-option"
				                                        name="paraCenterId"
				                                        multiple="multiple" required>
				                                </select>
			                                </div>
			                            </div>
			                           
					                        
				                  	 </div>
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
<script>
    $(document).ready(function () {
        $('.js-example-basic-multiple').select2({dropdownAutoWidth : true});
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
        getLocationHierarchy("/opensrp-dashboard/para-center-by-union?ids="+$("#union-option").val()+"&title=", "para-center-option")
    });

    $('#clusterInfo').submit(function(event) {
        event.preventDefault();
        let url = '${saveUrl}';
        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");
        let formData = {
            'name': $('#name').val(),
            'code': $('#code').val(),
            'division_id': $('#division-option').val(),
            'district_id': $('#district-option').val(),
            'upazila_id': $('#upazila-option').val(),
            'pourasabha_id': $('#pourasabha-option').val(),
            'union_ids': getValues($('#union-option').val()),
            'para_center_ids': getValues($('#para-center-option').val()),
            'date_created': new Date()
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
                    window.location.replace("/opensrp-dashboard/location/cluster-list.html");
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

    function getValues(values) {
        let selectedValues = [];
        values.forEach(function(value){
            selectedValues.push(value);
        });
        return selectedValues;
    }

</script>

