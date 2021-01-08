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
 
	
	
<jsp:include page="/WEB-INF/views/header.jsp" />

<jsp:include page="/WEB-INF/views/dataTablecss.jsp" />

<div class="page-content-wrapper">
		<div class="page-content">
			
			
			<ul class="page-breadcrumb breadcrumb text-right">
				<li>
									
				<% if(AuthenticationManagerUtil.isPermitted("ADD_WORK_MEETING")){ %>
										
					<a class="btn btn-default" id="add" href="<c:url value="/workplan-meeting/add.html?lang=${locale}"/>">Add new work plan meeting</a>
				<% } %>
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
								<i class="fa fa-list"></i>Work plan meeting list 
							</div>
							
							
						</div>
						
						<div class="portlet-body">
						
						<div class="row">								
							<div class="col-lg-3 form-group">
							    <label for="designation">Work Plan Type</label>
								<select name="roleList" class="form-control" id="typeList">
								<option value="0">Please Select</option>
								<c:forEach items="${meetingTypes}" var="meetingType">
									<option value="${meetingType.id}">${meetingType.name}</option>
								</c:forEach>
																		
								</select>
							</div>
							<div class="col-lg-2 form-group">
								<label for="from">Start date<span
									class="text-danger"> </span> </label> <input readonly="readonly" type="text"
									class="form-control date" id="from"> <span class="text-danger"
									id="startDateValidation"></span>
								</div>
								<div class="col-lg-2 form-group">
									<label for="to">End date<span
										class="text-danger"> </span> </label> <input readonly="readonly" type="text"
										class="form-control date" id="to"> <span class="text-danger"
										id="endDateValidation"></span>
								</div> 
							
							<div class="col-lg-3 form-group" style="padding-top: 22px">
							    <button type="submit" onclick="filter()" class="btn btn-primary" value="confirm">Search</button>
						   </div>
						</div>
							<table class="table table-striped table-bordered " id="meetingList">
								<thead>
									<tr>
										<th>Work Plan Type</th>
										<th>Work Plan Made on</th>
										<th>Meeting Place</th>
										<th>Other meeting Place</th>
										<th>Total Participants</th>	
										<th>No. of attachments</th>						
										<th>Action</th>
									</tr>
									</thead>
		
									<%-- <tbody>
									<c:forEach var="itm" items="${meetings}" varStatus="loop">
										<tr>
											<td>${itm.getMeetingOrWorkPlane()}</td>
											<td>${itm.getMeetingDate()}</td>
											<td>${itm.getPlace()}</td>
											<td>${itm.getMeetingPlaceOther()}</td>
											<td>${itm.getParticipants()}</td>
											
											
											<td>
												<% if(AuthenticationManagerUtil.isPermitted("EDIT_WORK_MEETING")){ %>
												<a href="<c:url value="/workplan-meeting/${ itm.getId()}/edit.html?lang=${locale}">															 
																 </c:url>">
													Edit
												</a>
												<% } %> | 
												<a href="<c:url value="/workplan-meeting/${ itm.getId()}/view.html?lang=${locale}">															 
																 </c:url>">
													Details
												</a>
											</td>
		
										</tr>
									</c:forEach>
									</tbody> --%>

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

<c:url var="list_url" value="/rest/api/v1/meeting/list" />

<script type="text/javascript">
var dateToday = new Date();
var dates = $(".date").datepicker({
	dateFormat: 'yy-mm-dd',
	 maxDate: dateToday,
	onSelect: function(selectedDate) {
	    var option = this.id == "from" ? "minDate" : "maxDate",
	        instance = $(this).data("datepicker"),
	        date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
	    dates.not(this).datepicker("option", option, date);
	} 
	});
var d = new Date();
var startDate =  $.datepicker.formatDate('yy-mm-dd', new Date(d.getFullYear(), d.getMonth(), 1));

$("#from").datepicker('setDate', startDate); 
$("#to").datepicker('setDate', new Date()); 
</script>
<script>
	let userList;
	$(document).ready(function() {
		// clearRegionSelection();
		
		userList = $('#meetingList').DataTable({
			bFilter: true,
			serverSide: true,
			processing: true,
			"ordering": false,
			"searching": false,
			ajax: {
				url: "${list_url}",
				data: function(data){
					
					data.meeting_type = 0;
					data.type = "WP";
					data.start_date=$('#from').val();
					data.end_date=$('#to').val();
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
		
		$('#meetingList').DataTable({
			bFilter: true,
			serverSide: true,
			processing: true,
			"ordering": false,
			"searching": false,
			ajax: {
				url: "${list_url}",
				data: function(data){					
					
					data.meeting_type = $('#typeList').val();;
					data.type = "WP";
					data.start_date=$('#from').val();
					data.end_date=$('#to').val();
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
	
</script>
