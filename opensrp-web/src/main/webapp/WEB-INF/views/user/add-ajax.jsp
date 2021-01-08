<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="org.opensrp.core.entity.Role"%>
<%@ page import="org.opensrp.common.util.Roles" %>

<title><spring:message code="lbl.addUserTitle"/></title>

<jsp:include page="/WEB-INF/views/header.jsp" />

<meta name="_csrf" content="${_csrf.token}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<c:url var="saveUrl" value="/user/add.html" />
<c:url var="saveRestUrl" value="/rest/api/v1/user/add" />
<c:url var="cancelUrl" value="/user.html" />
<c:url var="userList" value="/user.html" />

<link type="text/css" href="<c:url value="/resources/css/jquery-ui.css"/>" rel="stylesheet" http-equiv="Cache-control" content="public">

<div class="page-content-wrapper">
    <div class="page-content">


        <ul class="page-breadcrumb breadcrumb">
            <li>
                <a href="<c:url value="/user.html"/>">Home</a>
                <i class="fa fa-circle"></i>
            </li>
            <li>
                <a href="<c:url value="/user.html"/>">Back</a>
                
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
                            <i class="fa fa-edit"></i>Add new user
                        </div>


                    </div>
                    <div id="loading" style="display: none;position: absolute; z-index: 1000;margin-left:45%">
                        <img width="50px" height="50px" src="<c:url value="/resources/images/ajax-loading.gif"/>">
                    </div>
                    <div class="portlet-body">
                        <form id="userInfo" autocomplete="off">
                            <div class="form-group row">
                                <div class="col-sm-6">
                                    <label class="control-label" for="firstName"> <spring:message code="lbl.firstName"/> <span class="required">* </span>	</label>
                                    <input id="firstName" name="firstName" class="form-control mx-sm-3" required="required" />
                                </div>
								<div class="col-sm-6">
                                    <label class="control-label" for="lastName"> <spring:message code="lbl.lastName"/> 	</label>
                                    <input id="lastName" name="lastName" class="form-control mx-sm-3"  />
                                </div>
                               
                            </div>

                            <div class="form-group row">
                                 <div class="col-sm-6">
                                    <label class="control-label" for="email"> <spring:message code="lbl.email"/> 	</label>
                                    <input id="email"type="email" name="email" class="form-control mx-sm-3"  />
                                </div>

                                <div class="col-sm-6">
                                    <label class="control-label" for="email"> <spring:message code="lbl.mobile"/> <span class="required">* </span>	</label>
                                    <input id="mobile" pattern="^(?:\+88|01)?\d{9}\r?$"  name="mobile" class="form-control mx-sm-3" required="required" />
                                </div>
                            </div>

                            <div class="form-group row">
                                <div class="col-sm-6">
                                    <label class="control-label" for="birthDate"> <spring:message code="lbl.birthDate"/> <span class="required">* </span>	</label>
                                    <input type="text" id="birthDate" name="birthDate" class="form-control mx-sm-3" required="required" />
                                </div>
								<div class="col-sm-6">
                                    <label class="control-label" for="mobile"> <spring:message code="lbl.ethnicCommunity"/> 	</label>
                                    <input id="ethnicCommunity" name="ethnicCommunity" class="form-control mx-sm-3"  />
                                </div>
                               

                            </div>

                            <div class="form-group row">
                                
								 <div class="col-sm-6">
                                    <label class="control-label" for="username"> <spring:message code="lbl.username"/> <span class="required">* </span>	</label>
                                    <input id="username" name="username" class="form-control mx-sm-3" required="required" />
                                </div>
                                <div class="col-sm-4">
                                    <label class="control-label" for="password"> <spring:message code="lbl.password"/> <span class="required">* </span>	</label>
                                    <input type="text" id="password" name="password" class="form-control mx-sm-3" required="required" />
                                   
                                </div>
                                <div class="col-sm-2" style="padding-top: 26px">
                                 <input type="checkbox" class="form-control mx-sm-3" checked onclick="toggleVisibilityOfPassword()">Show Password
                                </div>
                            </div>

                            <div class="form-group row">
                                <div class="col-sm-6">
                                    <label class="control-label" for="gender"> <spring:message code="lbl.gender"/> <span class="required">* </span>	</label>
                                    <select
                                            id="gender-option"
                                            class="form-control js-example-basic-multiple"
                                            name="genderId"
                                            required="required">
                                        <option selected value="Male">Male</option>
                                        <option value="Female">Female</option>
                                        <option value="Other">Other</option>
                                    </select>
                                </div>

                                <div class="col-sm-6">
                                    <label class="control-label" for="role-option"> <spring:message code="lbl.role"/> <span class="required">* </span>	</label>
                                    <%--                                    <input id="role" name="role" class="form-control mx-sm-3" required="required" />--%>
                                    <select
                                            id="role-option"
                                            class="form-control"
                                            name="roleId" required="required">
                                        <option value="">Please select</option>
                                        <c:forEach items="${roles}" var="role">
                                            <option value="${role.id}">${role.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group row">
                                <div class="col-sm-6">
                                    <label class="control-label" for="brId"> <spring:message code="lbl.brId"/> 	</label>
                                    <input id="brId" name="brId" class="form-control mx-sm-3"/>
                                </div>

                                <div class="col-sm-6" id="district-div">
                                    <label class="control-label" ><spring:message code="lbl.district"/> <span class="required">* </span>
                                        <%--                                        <span style="color: red;">*</span>--%>
                                    </label>
                                    <select
                                            id="district-option"
                                            class="form-control js-example-basic-multiple"
                                            name="districtId" multiple="multiple">
                                        <c:forEach items="${districts}" var="district">
                                            <option value="${district.id}">${district.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group row">
                                <div class="col-sm-6">
                                    <label class="control-label" for="nId"> <spring:message code="lbl.nationalId"/>	</label>
                                    <input id="nId" name="nId" class="form-control mx-sm-3"/>
                                </div>

                                <div class="col-sm-6" id="upazila-div">
                                    <label class="control-label"><spring:message code="lbl.upazila"/> <span class="required">* </span>
                                        <%--                                        <span style="color: red;">*</span>--%>
                                    </label>
                                    <select
                                            id="upazila-option"
                                            class="form-control js-example-basic-multiple"
                                            name="upazilaId"
                                            multiple="multiple">
                                    </select>
                                </div>
                            </div>

                            <div class="form-group row">
                                <div class="col-sm-6">
                                    <label class="label-width" for="joiningDate"> Joining Date	</label>
                                    <input type="text" id="joiningDate" name="joiningDate" class="form-control mx-sm-3" />
                                </div>

                                <div class="col-sm-6" id="pourasabha-div">
                                    <label class="label-width"><spring:message code="lbl.pourasabha"/> <span style="color: red;">*</span>
                                        <%--                                        <span style="color: red;">*</span>--%>
                                    </label>
                                    <select
                                            id="pourasabha-option"
                                            class="form-control js-example-basic-multiple"
                                            name="pourasabhaId"
                                            multiple="multiple">
                                    </select>
                                </div>
                            </div>

                            <div class="form-group row">
                                <div class="col-sm-6">
                                    <label class="label-width" for="permanentAddress"> Permanent Address </label>
                                    
                                	<textarea id="permanentAddress"   name="permanentAddress" style="margin: 0px -11px 0px 0px; height: 60px; width: 100%;" class="form-control" placeholder=""></textarea>
                                	
                                
                                </div>

                                <div class="col-sm-6" id="union-div">
                                    <label class="label-width"><spring:message code="lbl.union"/> <span style="color: red;">*</span>
                                        <%--                                        <span style="color: red;">*</span>--%>
                                    </label>
                                    <select
                                            id="union-option"
                                            class="form-control js-example-basic-multiple"
                                            name="unionId"
                                            multiple="multiple">
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row">
                                <div class="col-sm-6">
                                    <label class="label-width" for="academicQualification"> Academic Qualification 	</label>
                                    <select
                                            id="academicQualification"
                                            class="form-control"
                                            name="academicQualification">
                                        <option value="">Please select</option>
                                        <c:forEach items="${configs}" var="config">
                                            <option value="${config.name}">${config.name}</option>
                                        </c:forEach>
                                    </select>
                                   
                                </div>

                                <div class="col-sm-6" id="cluster-div">
                                    <label class="label-width"><spring:message code="lbl.cluster"/> <span style="color: red;">*</span>
                                        <%--                                        <span style="color: red;">*</span>--%>
                                    </label>
                                    <select
                                            id="cluster-option"
                                            class="form-control js-example-basic-multiple"
                                            name="clusterId">
                                    </select>
                                </div>
                            </div>


                            <div class="col-lg-12">
								<div class="form-group text-right" id="errorText" style="display: none;">
								
								</div>
								<div id="errorMessage" style="display: none;">
			                         <div id="errormessageContent" style="font-size: 15;font-weight: bold;" class=" text-right"> </div>
			                         
		                    	</div>
							</div>
							
							<div class="form-group row"></div>

                            <hr class="dotted">
                            <div class="form-group text-right">
                                <button type="submit" id="submit-form"  class="btn btn-primary" name="signup" value="Validate">Submit</button>
                                <a class="btn btn-info" href="${cancelUrl}">Cancel</a>
                            </div>
                            
					
                        </form>




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




<script type="text/javascript">


    // function toggleVisibilityOfPassword() {
    //     let password = document.getElementById("password");
    //     let retypePassword = document.getElementById("retypePassword");
    //     if (password.type === "password") {
    //         password.type = "text";
    //         retypePassword.type = "text";
    //     } else {
    //         password.type = "password";
    //         retypePassword.type = "password";
    //     }
    // }
    //
    // function getBranches() {
    //     let branches = $('#branches').val();
    //     let selectedBranches = "";
    //     if ($('#role').val() == 29 || $('#role').val() == 28) return branches;
    //     branches.forEach(function(branch){
    //         selectedBranches += parseInt(branch) + ",";
    //     });
    //     if (selectedBranches.length > 0) selectedBranches = selectedBranches.slice(0, -1);
    //     return selectedBranches;
    // }

    $('#userInfo').submit(function(event) {
        event.preventDefault();
        let url = "${saveRestUrl}";
        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");
        let formData = {
            'first_name': $('input[name=firstName]').val(),
            'last_name': $('input[name=lastName]').val(),
            'birth_date': $('input[name=birthDate]').val(),
            'ethnic_community': $('input[name=ethnicCommunity]').val(),
            'gender': $('#gender-option').val(),
            'br_id': $('input[name=brId]').val(),
            'national_id': $('input[name=nId]').val(),
            'joining_date': $('input[name=joiningDate]').val(),
            'permanent_address': $('#permanentAddress').val(),
            'academic_qualification': $('#academicQualification').val(),
            'email': $('input[name=email]').val(),
            'mobile': $('input[name=mobile]').val(),
            'username': $('input[name=username]').val(),
            'password': $('input[name=password]').val(),
            'roles': $('#role-option').val(),
            'districts': getValues($('#district-option').val()),
            'upazilas': getValues($('#upazila-option').val()),
            'pourasabhas': getValues($('#pourasabha-option').val()),
            'unions': getValues($('#union-option').val()),
            'clusters': $('#cluster-option').val()
        };
       
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
                $("#errorMessage").show();            	  
                $("#errormessageContent").html("Please wait...") 
            },
            success : function(data) {
            	 $("#loading").hide();
            	let response = JSON.parse(data);
               
                $("#errorMessage").show();            	  
                $("#errormessageContent").html(response.msg)  
                if(response.status == 'SUCCESS'){
                	setTimeout(function(){
                		 window.location.replace("${userList}");
                     }, 1000);

                }
                
               
                
            },
            error : function(e) {
                console.log(e);
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    });

    // function Validate() {
    //     let password = document.getElementById("password").value;
    //     let confirmPassword = document.getElementById("retypePassword").value;
    //     if (password != confirmPassword) {
    //         $("#usernameUniqueErrorMessage").html("Your password is not similar with confirm password. Please enter same password in both");
    //         return false;
    //     }else{
    //         $("#passwordNotMatchedMessage").html("");
    //         return true;
    //     }
    // }

</script>

<script>
    $(document).ready(function() {
        $('.js-example-basic-multiple').select2({dropdownAutoWidth : true});
    });

    $('#role').change(function (event) {
    });

    $("#district-option").change(function () {
        console.log($("#district-option").val());
        getLocationHierarchy("/opensrp-dashboard/location-by-parent?ids="+$("#district-option").val()+"&title=", "upazila-option")
    });

    $("#upazila-option").change(function () {
        getLocationHierarchy("/opensrp-dashboard/location-by-parent?ids="+$("#upazila-option").val()+"&title=", "pourasabha-option")
    });

    $("#pourasabha-option").change(function () {
        getLocationHierarchy("/opensrp-dashboard/location-by-parent?ids="+$("#pourasabha-option").val()+"&title=", "union-option")
    });

    $("#union-option").change(function () {
        getLocationHierarchy("/opensrp-dashboard/cluster-by-union?ids="+$("#union-option").val()+"&title=", "cluster-option")
    });

    function getValues(values) {
        let selectedValues = [];
        console.log("values::");
        console.log(values);
        if( values !=null && typeof values != 'undefined'){
        	 values.forEach(function(value){
                 selectedValues.push(value);
             });
        }
       
        return selectedValues;
    }

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
                console.log(data);
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
    
    
    $('#role-option').change(function () {
		let roleId = $('#role-option').val();
		
		if ('<%=Roles.ADMIN.getId()%>' == roleId) {
			$('#district-option').prop('required', false);
			$('#upazila-option').prop('required', false);
			$('#pourasabha-option').prop('required', false);
			$('#union-option').prop('required', false);
			$('#cluster-option').prop('required', false);
			
			$('#district-option').val("");
			$('#district-option').trigger('change');

			$('#district-div').hide();
			$('#upazila-div').hide();
			$('#pourasabha-div').hide();
			$('#union-div').hide();
			$('#cluster-div').hide();
		} else if ('<%=Roles.DPM.getId()%>' == roleId) {
			$('#district-option').prop('required', true);
			$('#upazila-option').prop('required', false);
			$('#pourasabha-option').prop('required', false);
			$('#union-option').prop('required', false);
			$('#cluster-option').prop('required', false);

			$('#district-div').show();
			$('#upazila-div').hide();
			$('#pourasabha-div').hide();
			$('#union-div').hide();
			$('#cluster-div').hide();
		} else if ('<%=Roles.UPM.getId()%>' == roleId) {
			$('#district-option').prop('required', true);
			$('#upazila-option').prop('required', true);
			$('#pourasabha-option').prop('required', false);
			$('#union-option').prop('required', false);
			$('#cluster-option').prop('required', false);

			$('#district-div').show();
			$('#upazila-div').show();
			$('#pourasabha-div').hide();
			$('#union-div').hide();
			$('#cluster-div').hide();
		} else if ('<%=Roles.APM.getId()%>' == roleId) {
			$('#district-option').prop('required', true);
			$('#upazila-option').prop('required', true);
			$('#pourasabha-option').prop('required', true);
			$('#union-option').prop('required', false);
			$('#cluster-option').prop('required', false);

			$('#district-div').show();
			$('#upazila-div').show();
			$('#pourasabha-div').show();
			$('#union-div').show();
			$('#cluster-div').hide();
		} else {
			$('#district-option').prop('required', true);
			$('#upazila-option').prop('required', true);
			$('#pourasabha-option').prop('required', true);
			$('#union-option').prop('required', true);
			$('#cluster-option').prop('required', true);

			$('#district-div').show();
			$('#upazila-div').show();
			$('#pourasabha-div').show();
			$('#union-div').show();
			$('#cluster-div').show();
		}
	});
    
    
    
    $( function() {
	    $( "#birthDate" ).datepicker({
	    	dateFormat: 'yy-mm-dd',
	    	changeYear: true,
	    	maxDate:new Date(),
	    	
	    }	
	    );
	 } );
    
    $( function() {
	    $( "#joiningDate" ).datepicker({
	    	dateFormat: 'yy-mm-dd',
	    	changeYear: true,
	    	maxDate:new Date(),
	    	
	    }	
	    );
	 } );
    
    function toggleVisibilityOfPassword() {
        var password = document.getElementById("password");
        //var retypePassword = document.getElementById("retypePassword");
        if (password.type === "password") {
            password.type = "text";
            //retypePassword.type = "text";
        } else {
           password.type = "password";
           // retypePassword.type = "password";
        }
    }
</script>

