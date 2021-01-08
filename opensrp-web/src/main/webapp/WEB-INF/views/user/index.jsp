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
<title><spring:message code="lbl.userList"/></title>
<%
	Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
%>

<jsp:include page="/WEB-INF/views/header.jsp" />
<jsp:include page="/WEB-INF/views/dataTablecss.jsp" />
<c:url var="list_url" value="/rest/api/v1/user/user-with-catchment-area" />

<link type="text/css" href="<c:url value="/resources/css/jquery.toast.css"/>" rel="stylesheet">

<div class="page-content-wrapper">
	<div class="page-content">


		<ul class="page-breadcrumb breadcrumb">
			<li>
				<%-- <a href="<c:url value="/user.html"/>">Home</a> --%>



				<% if(AuthenticationManagerUtil.isPermitted("ADD_USER")){ %>
				<a class="btn btn-default" id="add" href="<c:url value="/user/add-ajax.html?lang=${locale}"/>">Add
					a new user</a>
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
							<i class="fa fa-list"></i>User list <span class="success" style="color:green">${message}</span>
						</div>


					</div>
					

					<div class="portlet-body">
					
						<div class="row">								
							<div class="col-lg-3 form-group">
							    <label for="designation">Role</label>
								<select name="roleList" class="form-control" id="roleList">
								<option value="0">Please Select</option>
								<c:forEach items="${roles}" var="role">
									<option value="${role.id}">${role.name}</option>
								</c:forEach>
																		
								</select>
							</div>
							<div class="col-lg-3 form-group" style="padding-top: 0px">
								 <label for="cars">Search key</label> 				    
								<input name="search" class="form-control"id="search" placeholder="username, name"/> 
							</div>
							
							<div class="col-lg-3 form-group" style="padding-top: 22px">
							    <button type="submit" onclick="filter()" class="btn btn-primary" value="confirm">Search</button>
						   </div>
						</div>
						<table class="table table-striped table-bordered " id="userList">
							<thead>
							<tr>
								<th><spring:message code="lbl.name"></spring:message></th>
								<th><spring:message code="lbl.role"></spring:message></th>
								<th><spring:message code="lbl.catchmentArea"></spring:message></th>
								<th><spring:message code="lbl.username"></spring:message></th>
								<th><spring:message code="lbl.phoneNumber"></spring:message></th>
								<th><spring:message code="lbl.email"></spring:message></th>
								<th>Joining Date</th>
								<th><spring:message code="lbl.action"></spring:message></th>
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
	let userList;
	$(document).ready(function() {
		// clearRegionSelection();
		$('.js-example-basic-multiple').select2({dropdownAutoWidth : true});
		userList = $('#userList').DataTable({
			bFilter: true,
			serverSide: true,
			processing: true,
			"ordering": false,
			"searching": false,
			ajax: {
				url: "${list_url}",
				data: function(data){
					
					data.role = $('#roleList').val();
					data.key = $('#search').val();
				},
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
				searchPlaceholder: "Username"
			}
		});
	});
	
	
	function filter(){
		
		$('#userList').DataTable({
			bFilter: true,
			serverSide: true,
			processing: true,
			"ordering": false,
			"searching": false,
			ajax: {
				url: "${list_url}",
				data: function(data){					
					data.role = $('#roleList').val();
					data.key = $('#search').val();
				},
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
				searchPlaceholder: "Username"
			}
		});
		
	}
	function drawDataTables() {
		userList.ajax.reload();
	}
	function clearRegionSelection() {
		$("#division").val("");
		$("#district").html("<option value='0?'>Select District</option>");
		$("#upazila").html("<option value='0?'>Select Upazila/City Corporation</option>");
		$("#pourasabha").html("<option value='0?'>Select Pourasabha</option>");
		$("#union").html("<option value='0?'>Select Union/Ward</option>");
		$("#village").html("<option value='0?'>Select Village</option>");
	}
</script>
