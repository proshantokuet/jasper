<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="org.opensrp.web.util.AuthenticationManagerUtil"%>
<%@ page import="org.opensrp.core.entity.User" %>

<!DOCTYPE html>
<!-- 
Template Name: Metronic - Responsive Admin Dashboard Template build with Twitter Bootstrap 3.3.2
Version: 3.7.0
Author: KeenThemes
Website: http://www.keenthemes.com/
Contact: support@keenthemes.com
Follow: www.twitter.com/keenthemes
Like: www.facebook.com/keenthemes
Purchase: http://themeforest.net/item/metronic-responsive-admin-dashboard-template/4021469?ref=keenthemes
License: You must have a valid license purchased only from themeforest(the above link) in order to legally use the theme for your project.
-->
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8"/>

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta content="" name="description"/>
<meta content="" name="author"/>
<meta name="_csrf" content="${_csrf.token}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<link rel="shortcut icon" href="<c:url value="/resources/assets/img/favicon.ico"/>" />
<title>${title}</title>	
<jsp:include page="/WEB-INF/views/css.jsp" />
<c:url var="sendPrescriptionMessage" value="/rest/api/v1/message/prescription" />
<c:url var="sendBookingMessage" value="/rest/api/v1/message/booking" />

<jsp:include page="/WEB-INF/views/js.jsp" />

   <%
   
   User user = (User) AuthenticationManagerUtil.getLoggedInUser();
   
   %>
  
<body>
<c:url var="home" value="/" />

<body class="page-header-fixed page-sidebar-closed-hide-logo ">
<!-- BEGIN HEADER -->
<div class="page-header navbar navbar-fixed-top">
	<!-- BEGIN HEADER INNER -->
	<div class="page-header-inner">
		<!-- BEGIN LOGO -->
		<div class="page-logo">
			<%-- <a href="<c:url value="/" />">
			<img src="<c:url value="/resources/assets/img/106x43-logo.png"/>" alt="logo" class="logo-default"/>
			</a> --%>
			<div class="menu-toggler sidebar-toggler">
				<!-- DOC: Remove the above "hide" to enable the sidebar toggler button on header -->
			</div>
		</div>
		<!-- END LOGO -->
		<!-- BEGIN RESPONSIVE MENU TOGGLER -->
		<a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
		</a>
		
		<!-- END PAGE ACTIONS -->
		<!-- BEGIN PAGE TOP -->
		<div class="page-top">
		
			<span id="confirmedNotification" class="success" style="position: absolute; top: 5px; right: 651px;color:#578ebe;font-weight:bold; font-size: 14px"></span>
			<span id="mobileMsssage" class="successs" style="position: absolute; top: 24px; right: 651px;color:#578ebe;font-weight:bold; font-size: 14px"></span>
					
			<!-- BEGIN TOP NAVIGATION MENU -->
			 <div class="top-menu">
			
			
				<ul class="nav navbar-nav pull-right">
				
					
					
					<li class="dropdown dropdown-user dropdown-dark">
						<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
						<span class="username username-hide-on-mobile">
						Login as <%=AuthenticationManagerUtil.getLoggedInUser().getUsername() %> </span>
						<!-- DOC: Do not remove below empty space(&nbsp;) as its purposely used -->
						
						</a>
						<ul class="dropdown-menu dropdown-menu-default">
							<!-- <li>
								<a href="extra_profile.html">
								<i class="icon-user"></i> My Profile </a>
							</li>
							
							<li class="divider">
							</li>
							<li>
								<a href="extra_lock.html">
								<i class="icon-lock"></i> Lock Screen </a>
							</li> -->
							<li>
								<a  href="<c:url value="/logout/"/>">
								<i class="icon-key"></i> Log Out </a>
							</li>
							<li>
								<a  href="/opensrp-dashboard/user/<%=user.getId()%>/change-password.html?lang=${locale}">
								Change Password  </a>
							</li>
						</ul>
					</li>
					
					
				</ul>
			</div>
			<!-- END TOP NAVIGATION MENU -->
		</div>
		<!-- END PAGE TOP -->
	</div>
	<!-- END HEADER INNER -->
</div>
<!-- END HEADER -->
<div class="clearfix">
</div>
<!-- BEGIN CONTAINER -->
<div class="page-container">
	<!-- BEGIN SIDEBAR -->
	<div class="page-sidebar-wrapper">
		<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
		<!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
		<div class="page-sidebar navbar-collapse collapse">
			<!-- BEGIN SIDEBAR MENU -->
			<!-- DOC: Apply "page-sidebar-menu-light" class right after "page-sidebar-menu" to enable light sidebar menu style(without borders) -->
			<!-- DOC: Apply "page-sidebar-menu-hover-submenu" class right after "page-sidebar-menu" to enable hoverable(hover vs accordion) sub menu mode -->
			<!-- DOC: Apply "page-sidebar-menu-closed" class right after "page-sidebar-menu" to collapse("page-sidebar-closed" class must be applied to the body element) the sidebar sub menu mode -->
			<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
			<!-- DOC: Set data-keep-expand="true" to keep the submenues expanded -->
			<!-- DOC: Set data-auto-speed="200" to adjust the sub menu slide up/down speed -->
			<ul class="page-sidebar-menu " data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">
				<li class="start ">
					<a href="<c:url value="/" />">
					<i class="fa fa-home"></i>
					<span class="title"> Home</span>
					</a>
				</li>
				
				
				
				
				<% if(AuthenticationManagerUtil.isPermitted("ADD_TAG")){ %>
				<li>
					<a  href="<c:url value="/location/tag/list.html?lang=${locale}"/>">
					<i class="fa fa-cogs"></i>
					<span class="title"> Location Tag</span></a>
					
				</li>
				<%} %>
				<% if(AuthenticationManagerUtil.isPermitted("DHIS2")){ %>
				<li>
					<a  href="<c:url value="/dhis2-configuartion-list.html?lang=${locale}"/>">
					<i class="fa fa-cogs"></i>
					<span class="title"> DHIS2 configuration</span></a>
					
				</li>
				<%} %>
				
				<% if(AuthenticationManagerUtil.isPermitted("LOCATION")){ %>
				<li>
					<a href="javascript:;">
					<i class="fa fa-cogs"></i>
					<span class="title"> Location</span>
					<span class="arrow "></span>
					</a>
					<ul class="sub-menu">
						<li>
							<a href="<c:url value="/location/upload_csv.html"/>">
							Upload Location
							</a>
						</li>
					<% if(AuthenticationManagerUtil.isPermitted("DIVISION")){ %>
						<li>
							<a href="<c:url value="/location/location.html?lang=${locale}"/>">
							<spring:message code="lbl.manageLocation"/>
							</a>
						</li>
						<%} %>
					<% if(AuthenticationManagerUtil.isPermitted("DISTRICT")){ %>
						<li>
							<a  href="<c:url value="/location/district-list.html?lang=${locale}"/>">
							
							<spring:message code="lbl.districtList"/>
							</a>
						</li>
						<%} %>
						<% if(AuthenticationManagerUtil.isPermitted("UPAZILA")){ %>
						<li>
							<a  href="<c:url value="/location/upazila-list.html?lang=${locale}"/>">
							
							<spring:message code="lbl.upazilaList"/>
							</a>
						</li>
						<%} %>
						<% if(AuthenticationManagerUtil.isPermitted("POURASAVA")){ %>
						<li>
							<a  href="<c:url value="/location/pourasabha-list.html?lang=${locale}"/>">
							<spring:message code="lbl.pourasabhaList"/></a>
						</li>
						<%} %>
						<% if(AuthenticationManagerUtil.isPermitted("UNION")){ %>
						<li>
							<a href="<c:url value="/location/union-list.html?lang=${locale}"/>">
							<spring:message code="lbl.unionList"/></a>
						</li>
						<% }%>
						<% if(AuthenticationManagerUtil.isPermitted("PARA")){ %>
						<li>
							<a href="<c:url value="/location/para-list.html?lang=${locale}"/>">
							<spring:message code="lbl.villageList"/></a>
						</li>
						<% }%>
						<% if(AuthenticationManagerUtil.isPermitted("PARA_CENTER")){ %>
						<li>
							<a  href="<c:url value="/location/para-center-list.html?lang=${locale}"/>">
							<spring:message code="lbl.paraCenterList"/></a>
						</li>
						<% }%>
						<% if(AuthenticationManagerUtil.isPermitted("CLUSTER")){ %>
						<li>
							<a  href="<c:url value="/location/cluster-list.html?lang=${locale}"/>">
							<spring:message code="lbl.clusterList"/></a>
						</li>
						<% }%>
						
					</ul>
				</li>
				<% }%>
				
				<% if(AuthenticationManagerUtil.isPermitted("ROLE")){ %>
				<li>
					<a href="<c:url value="/role.html"/>">
						<i class="fa fa-cogs"></i>
						<span class="title"> Role</span>
					</a>
				</li>
				<%} %>
				<% if(AuthenticationManagerUtil.isPermitted("USER")){ %>
				<li>
					<a href="<c:url value="/user.html"/>">
						<i class="fa fa-user"></i>
						<span class="title"> User</span>
					</a>
				</li>
				<%} %>
				
				<% if(AuthenticationManagerUtil.isPermitted("TOPIC")){ %>
				<li>
					<a href="<c:url value="/topic-list.html"/>">
						<i class="fa fa-user"></i>
						<span class="title"> Topic</span>
					</a>
				</li>
				<%} %>
				<% if(AuthenticationManagerUtil.isPermitted("OTHER_MEETING_MINUTES_LIST")){ %>
				<li>
					<a href="<c:url value="/other-meeting-minutes/list.html"/>">
						<i class="fa fa-user"></i>
						<span class="title"> General meeting minutes</span>
					</a>
				</li>
				<% } %>
				<% if(AuthenticationManagerUtil.isPermitted("WORK_MEETING_LIST")){ %>
				<li>
					<a href="<c:url value="/workplan-meeting/list.html"/>">
						<i class="fa fa-user"></i>
						<span class="title"> Workplan meeting</span>
					</a>
				</li>
				
				<% } %>
				
				
				
				<!-- <li>
					<a href="javascript:;">
					<i class="icon-diamond"></i>
					<span class="title">UI Features</span>
					<span class="arrow "></span>
					</a>
					<ul class="sub-menu">
						<li>
							<a href="ui_general.html">
							General Components</a>
						</li>
						<li>
							<a href="ui_buttons.html">
							Buttons</a>
						</li>
						<li>
							<a href="ui_icons.html">
							<span class="badge badge-danger">new</span>Font Icons</a>
						</li>
						<li>
							<a href="ui_colors.html">
							Flat UI Colors</a>
						</li>
						<li>
							<a href="ui_typography.html">
							Typography</a>
						</li>
						<li>
							<a href="ui_tabs_accordions_navs.html">
							Tabs, Accordions & Navs</a>
						</li>
						<li>
							<a href="ui_tree.html">
							<span class="badge badge-danger">new</span>Tree View</a>
						</li>
						<li>
							<a href="ui_page_progress_style_1.html">
							<span class="badge badge-warning">new</span>Page Progress Bar - Style 1</a>
						</li>
						<li>
							<a href="ui_blockui.html">
							Block UI</a>
						</li>
						<li>
							<a href="ui_bootstrap_growl.html">
							<span class="badge badge-roundless badge-warning">new</span>Bootstrap Growl Notifications</a>
						</li>
						<li>
							<a href="ui_notific8.html">
							Notific8 Notifications</a>
						</li>
						<li>
							<a href="ui_toastr.html">
							Toastr Notifications</a>
						</li>
						<li>
							<a href="ui_alert_dialog_api.html">
							<span class="badge badge-danger">new</span>Alerts & Dialogs API</a>
						</li>
						<li>
							<a href="ui_session_timeout.html">
							Session Timeout</a>
						</li>
						<li>
							<a href="ui_idle_timeout.html">
							User Idle Timeout</a>
						</li>
						<li>
							<a href="ui_modals.html">
							Modals</a>
						</li>
						<li>
							<a href="ui_extended_modals.html">
							Extended Modals</a>
						</li>
						<li>
							<a href="ui_tiles.html">
							Tiles</a>
						</li>
						<li>
							<a href="ui_datepaginator.html">
							<span class="badge badge-success">new</span>Date Paginator</a>
						</li>
						<li>
							<a href="ui_nestable.html">
							Nestable List</a>
						</li>
					</ul>
				</li> -->
				
				
				
			</ul>
			<!-- END SIDEBAR MENU -->
		</div>
	</div>
	
