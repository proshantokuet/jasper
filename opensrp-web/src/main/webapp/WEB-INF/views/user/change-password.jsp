<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="org.opensrp.common.util.CheckboxHelperUtil"%>
<%@page import="java.util.List"%>
<%@page import="org.opensrp.core.entity.Permission"%>
 <title><spring:message code="lbl.changePassword"/></title>
<jsp:include page="/WEB-INF/views/header.jsp" />
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="org.opensrp.core.entity.Role"%>
<%@ page import="org.opensrp.web.util.AuthenticationManagerUtil" %>
    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
  <link type="text/css"
	href="<c:url value="/resources/css/magicsuggest-min.css"/>"
	rel="stylesheet"
	http-equiv="Cache-control" content="public">  
<%
    String username = (String) session.getAttribute("username");
%>
<%
    String fromRole = (String) session.getAttribute("fromRole");
    String role = AuthenticationManagerUtil.isAM()?"AM":"";
    Integer skId = (Integer) session.getAttribute("idFinal");
    String skUsername = (String) session.getAttribute("usernameFinal");
%>
<c:url var="postUrl" value="/rest/api/v1/topic/save" />
<c:url var="listUrl" value="/topic-list.html" />

<div class="page-content-wrapper">
		<div class="page-content">
			
			
			<%-- <ul class="page-breadcrumb breadcrumb">
				<li>
					<a href="<c:url value="/"/>">Home</a>
					<i class="fa fa-circle"></i>
				</li>
				<li>
					<a href="<c:url value="/topic-list.html"/>">Topic list</a>
					<i class="fa fa-circle"></i>
				</li>
			
			</ul> --%>
			<!-- END PAGE BREADCRUMB -->
			<!-- END PAGE HEADER-->
			<!-- BEGIN PAGE CONTENT-->
			
	
			<div class="row">
				<div class="col-md-12">
					
					<!-- BEGIN EXAMPLE TABLE PORTLET-->
					<div class="portlet box blue-madison">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-edit"></i>Change Password
							</div>
							
							
						</div>
						
						<div class="portlet-body">
							 <div id="changePassword"  class="validator-form">
								<div class="form-group">
					              <div class="row">
					                <div class="col-lg-6 form-group"> 
						                <label class="control-label" for="name"> <spring:message code="lbl.password"/> <span class="required">* </span>	</label>
										<input type="text" class="form-control mx-sm-3" id="password" name="password"  required />
                        				<input type="checkbox" checked onclick="toggleVisibilityOfPassword()">Show Password
					                 </div>
					                  <div class="col-lg-6 form-group">
					                   <label class="control-label" for="code"> <spring:message code="lbl.confirmedPassword"/> </label>
										<input type="text" class="form-control mx-sm-3" id="retypePassword"
                                       required="required" />
				                        <small id="confirmPasswordHelpInline" class="text-muted text-para">
				                            <span class="text-red" id="passwordNotMatchedMessage"></span>
				                        </small> 
					                  </div>
					                 
					              </div>
					            </div> 
								
								
								
								
								 <hr class="dotted"> 
								<div class="form-group text-right">
					                <input
                                type="submit"
                                onclick="submitted()"
                                value="<spring:message code="lbl.resetPassword"/>"
                                 class="btn btn-primary" />
					                <a class="btn btn-info" href="${listUrl}">Cancel</a>
					            </div>
							</div>
							
							
							
							
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

<jsp:include page="/WEB-INF/views/magicsuggest.jsp" />
	<script src="<c:url value='/resources/js/magicsuggest.js' />"></script>
	

 <script type="text/javascript">

    function toggleVisibilityOfPassword() {
        var password = document.getElementById("password");
        var retypePassword = document.getElementById("retypePassword");
        if (password.type === "password") {
            password.type = "text";
            retypePassword.type = "text";
        } else {
            password.type = "password";
            retypePassword.type = "password";
        }
    }

    function submitted() {

        var password = document.getElementById("password").value;
        var confirmPassword = document.getElementById("retypePassword").value;
        if (password != confirmPassword) {
            $("#passwordNotMatchedMessage").html("Your password doesn't match. Please enter same.");
            return;
        }
        if (password.length < 4) {
            $("#passwordNotMatchedMessage").html("Password should be 4 character long...");
            return;
        }
        $("#passwordNotMatchedMessage").html("");

        console.log("first chance");
        let url = "/opensrp-dashboard/rest/api/v1/user/change-password";
        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");
        console.log("last chance <%=username%>");
        let formData = {
            'username': "<%=username%>",
            'password': $('input[name=password]').val()
        };

        let redirectUrl = "/opensrp-dashboard/user.html";
       

        $.ajax({
            contentType : "application/json",
            type: "POST",
            url: url,
            data: JSON.stringify(formData),
            dataType : 'json',

            timeout : 100000,
            beforeSend: function(xhr) {
                $("#loading").show();
                xhr.setRequestHeader(header, token);
            },
            success : function(data) {
                console.log("response data: "+data);
                $("#usernameUniqueErrorMessage").html(data);
                $("#loading").hide();
                if(data == ""){
                   window.location.replace(redirectUrl);
                }
            },
            error : function(e) {
                $("#loading").hide();
                console.log("In Error");
            },
            done : function(e) {
                $("#loading").hide();
                console.log("DONE");
            }
        });
    }

    function Validate() {
        let password = document.getElementById("password").value;
        let confirmPassword = document.getElementById("retypePassword").value;
        if (password != confirmPassword) {
            $("#passwordNotMatchedMessage").html("Your password doesn't match. Please enter same.");
            return false;
        }

        $("#passwordNotMatchedMessage").html("");
        return true;
    }

</script>

