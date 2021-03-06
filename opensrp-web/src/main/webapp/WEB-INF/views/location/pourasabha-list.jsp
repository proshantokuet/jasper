<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="security"
		   uri="http://www.springframework.org/security/tags"%>
<%@page import="org.opensrp.web.util.AuthenticationManagerUtil"%>
<%@page import="java.util.List"%>
<%@page import="org.opensrp.core.service.UserService"%>
 
<title><spring:message code="lbl.districtList"/></title>
<jsp:include page="/WEB-INF/views/header.jsp" />
<jsp:include page="/WEB-INF/views/dataTablecss.jsp" />
<c:url var="list_url" value="/rest/api/v1/location/pourasabha-list" />

<div class="page-content-wrapper">
		<div class="page-content">
			
			
			<ul class="page-breadcrumb breadcrumb">
				<li>
					<%-- <a href="<c:url value="/user.html"/>">Home</a> --%>
					
				
				
				
				<% if(AuthenticationManagerUtil.isPermitted("ADD_POURASAVA")){ %>
									
					<a class="btn btn-default" id="add" href="<c:url value="/location/add-pourasabha.html?lang=${locale}"/>"><spring:message code="lbl.addNew"/> pourasabha</a>
					<%} %>
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
								<i class="fa fa-list"></i> <spring:message code="lbl.pourasabhaList"/>
							</div>
							
							
						</div>
						
						<div class="portlet-body">
							<table class="table table-striped table-bordered " id="locationListWithPagination">
								 <thead>
                        <tr>
                            <th><spring:message code="lbl.pourasabhaName"></spring:message></th>
                            <th><spring:message code="lbl.code"></spring:message></th>
                            <th><spring:message code="lbl.upazilaName"></spring:message></th>
                            <th><spring:message code="lbl.districtName"></spring:message></th>
                            <th><spring:message code="lbl.divisionName"></spring:message></th>
                        </tr>
                        </thead>
							</table>
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
<jsp:include page="/WEB-INF/views/dataTablejs.jsp" />

<script src="<c:url value='/resources/assets/admin/js/table-advanced.js'/>"></script>

<script>
    let locations;
    $(document).ready(function() {
        locations = $('#locationListWithPagination').DataTable({
            bFilter: true,
            serverSide: true,
            processing: true,
            columnDefs: [
                { targets: [1, 2, 3], orderable: false },
                { width: "25%", targets: 0 },
                { width: "25%", targets: 1 },
                { width: "25%", targets: 2 },
                { width: "25%", targets: 3 }
            ],
            ajax: {
                url: "${list_url}",
                dataSrc: function(json){
                    if(json.data){
                        return json.data;
                    }
                    else {
                        return [];
                    }
                },
                complete: function() {
                },
                type: 'GET'
            },
            bInfo: true,
            destroy: true,
            language: {
                searchPlaceholder: "Pourasabha Name"
            }
        });
    });
</script>
