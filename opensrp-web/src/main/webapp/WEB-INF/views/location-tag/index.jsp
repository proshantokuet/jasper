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
<title><spring:message code="lbl.loactionTagList"/></title>

	
<jsp:include page="/WEB-INF/views/header.jsp" />
<link type="text/css" href="<c:url value="/resources/css/jquery.toast.css"/>" rel="stylesheet">
<jsp:include page="/WEB-INF/views/dataTablecss.jsp" />
<div class="page-content-wrapper">
		<div class="page-content">
			
			
			<ul class="page-breadcrumb breadcrumb">
				<li>
					<%-- <a href="<c:url value="/user.html"/>">Home</a> --%>
					
				
				
				
				<% if(AuthenticationManagerUtil.isPermitted("ADD_LOCATION_TAG")){ %>
									
					<a class="btn btn-default" id="add" href="<c:url value="/location/tag/add.html?lang=${locale}"/>">
					Add a location tag</a> 
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
								<i class="fa fa-list"></i>Location tag list <span class="success" style="color:green">${message}</span>
							</div>
							
							
						</div>
						
						<div class="portlet-body">
							<table class="table table-striped table-bordered " id="sample_1">
								<thead>
								<tr>
									<th><spring:message code="lbl.name"/></th>
									<th><spring:message code="lbl.description"/></th>
									<th><spring:message code="lbl.createdDate"/></th>
									<th><spring:message code="lbl.action"/></th>
								</tr>
							</thead>
							
							<tbody>
								<c:forEach var="locationTag" items="${locationTags}" varStatus="loop">
									<tr>
										<td>${locationTag.getName()}</td>
										<td>${locationTag.getDescription()}</td>
										<td>${locationTag.getCreated()}</td>
										<td>
										<% if(AuthenticationManagerUtil.isPermitted("EDIT_LOCATION_TAG")){ %>
											<a href="<c:url value="/location/tag/${locationTag.id}/edit.html?lang=${locale}"/>"><spring:message code="lbl.edit"/></a>
										<%} %>
										</td>

									</tr>
								</c:forEach>
							</tbody>

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

<jsp:include page="/WEB-INF/views/dataTablejs.jsp" />

<script src="<c:url value='/resources/assets/admin/js/table-advanced.js'/>"></script>
<script>
jQuery(document).ready(function() {       
	 Metronic.init(); // init metronic core components
		Layout.init(); // init current layout
   TableAdvanced.init();
});
</script>
<jsp:include page="/WEB-INF/views/footer.jsp" />


