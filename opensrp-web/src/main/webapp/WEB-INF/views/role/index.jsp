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
<title>Role list</title>


<jsp:include page="/WEB-INF/views/header.jsp" />
<jsp:include page="/WEB-INF/views/dataTablecss.jsp" />
<link type="text/css" href="<c:url value="/resources/css/jquery.toast.css"/>" rel="stylesheet">

<div class="page-content-wrapper">
	<div class="page-content">


		<ul class="page-breadcrumb breadcrumb">
			<li>
				<%-- <a href="<c:url value="/user.html"/>">Home</a> --%>




				<%-- <% if(AuthenticationManagerUtil.isPermitted("ADD_ROLE")){ %>

				<a class="btn btn-default" id="add" href="<c:url value="/role/add.html?lang=${locale}"/>">
					Add a new role</a>
				<%} %> --%>
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
							<i class="fa fa-list"></i>Role list <span class="success" style="color:green">${message}</span>
						</div>


					</div>

					<div class="portlet-body">
						<table class="table table-striped table-bordered " id="roleList">
							<thead>
							<tr>
								<th><spring:message code="lbl.name"/></th>
								<%--							<th><spring:message code="lbl.permissions"/></th>--%>
								<th><spring:message code="lbl.action"/></th>
							</tr>
							</thead>

							<tbody>
							<c:forEach var="role" items="${roles}" varStatus="loop">
								<tr>
									<td>${role.getName()}</td>
										<%--								<td><c:forEach var="permission"--%>
										<%--											   items="${role.getPermissions()}" varStatus="loop">--%>
										<%--									<b> ${permission.getName()} , </b>--%>
										<%--								</c:forEach></td>--%>
									<td>
										<% if(AuthenticationManagerUtil.isPermitted("EDIT_ROLE")){ %>
										<a href="<c:url value="/role/${role.id}/edit.html?lang=${locale}"/>">
											<span class="glyphicon glyphicon-edit">Edit</span>
										</a>
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
	$(document).ready(function() {
		$('#roleList').DataTable({
			bFilter: true,
			bInfo: true,
			//dom: 'Bfrtip',
			destroy: true,
			buttons: [ 'print' ],
			language: {
				searchPlaceholder: "Role Name "
			}
		});
	});
</script>
