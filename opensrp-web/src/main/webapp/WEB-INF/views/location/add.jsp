<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="org.opensrp.common.util.CheckboxHelperUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="org.opensrp.core.entity.LocationTag"%>
<%@page import="org.json.JSONObject" %>
<%@page import="org.json.JSONArray" %>
<%
	Map<Integer, String> parentLocations =  (Map<Integer, String>)session.getAttribute("parentLocation");
	Integer selectedParentLocation = (Integer)session.getAttribute("selectedParentLocation");

	Map<Integer, String> tags =  (Map<Integer, String>)session.getAttribute("tags");

	String selectedParentLocationName = (String)session.getAttribute("parentLocationName");

	Integer selectedTag = (Integer)session.getAttribute("selectedTag");

	JSONArray locationTreeData = (JSONArray)session.getAttribute("locatationTreeData");

%>
<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link type="text/css" href="<c:url value="/resources/css/jqx.base.css"/>" rel="stylesheet">

	<title><spring:message code="lbl.addLocation"/></title>
	<jsp:include page="/WEB-INF/views/css.jsp" />
</head>

<c:url var="saveUrl" value="/location/add-new.html" />

<body class="fixed-nav sticky-footer bg-dark" id="page-top">
<jsp:include page="/WEB-INF/views/navbar.jsp" />
<div class="content-wrapper">
	<div class="container-fluid">
		<div class="form-group">
			<jsp:include page="/WEB-INF/views/location/location-tag-link.jsp" />

		</div>
		<div class="card mb-3">
			<div class="card-header">
				</i><spring:message code="lbl.addLocation"/>
			</div>
			<div class="card-body">
				<span style="color: red;"> ${uniqueErrorMessage}</span>
				<form:form method="POST" action="${saveUrl}" modelAttribute="location">
					<div class="form-group">
						<div class="row">
							<div class="col-5">
								<label for="exampleInputName"><spring:message code="lbl.name"/>  </label>
								<form:input path="name" class="form-control"
											required="required" value="${name}" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							<div class="col-5">
								<label for="exampleInputName"><spring:message code="lbl.code"/></label>
								<form:input path="code" class="form-control"
											required="required"/>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							<div class="col-5">
								<label for="exampleInputName"><spring:message code="lbl.description"/></label>
								<form:input path="description" class="form-control"
											required="required" />
							</div>
						</div>
					</div>

					<div class="form-group">
						<div class="row">
							<div class="col-5">
								<label for="exampleInputName"> <spring:message code="lbl.tag"/></label>
								<select class="custom-select custom-select-lg mb-3" id="locationTag" name="locationTag" required="required">
									<option value="" selected><spring:message code="lbl.pleaseSelect"/></option>
									<%
										for (Map.Entry<Integer, String> entry : tags.entrySet()) {
											if(selectedTag==entry.getKey()){
									%>
									<option value="<%=entry.getKey()%>" selected><%=entry.getValue() %></option>
									<%
											} else{
									%>
									<option value="<%=entry.getKey()%>"><%=entry.getValue() %></option>
									<%
											}
										}
									%>
								</select>
							</div>
						</div>
					</div>

					<div id="division" class="form-group" style="display: none;">
						<div class="row">
							<div class="col-5">
								<div class="ui-widget">
									<label><spring:message code="lbl.division"/> </label>
									<select id="division-option" class="form-control">
									</select>
								</div>
							</div>
						</div>
					</div>

					<div id="district" class="form-group" style="display: none;">
						<div class="row">
							<div class="col-5">
								<div class="ui-widget">
									<label><spring:message code="lbl.district"/> </label>
									<select id="district-option" class="form-control">
									</select>
								</div>
							</div>
						</div>
					</div>

					<div id="upazila" class="form-group" style="display: none;">
						<div class="row">
							<div class="col-5">
								<div class="ui-widget">
									<label><spring:message code="lbl.upazila"/> </label>
									<select id="upazila-option" class="form-control">
									</select>
								</div>
							</div>
						</div>
					</div>

					<div id="pourasabha" class="form-group" style="display: none;">
						<div class="row">
							<div class="col-5">
								<div class="ui-widget">
									<label><spring:message code="lbl.pourasabha"/> </label>
									<select id="pourasabha-option" class="form-control">
									</select>
								</div>
							</div>
						</div>
					</div>

					<div id="union" class="form-group" style="display: none;">
						<div class="row">
							<div class="col-5">
								<div class="ui-widget">
									<label><spring:message code="lbl.union"/> </label>
									<select id="union-option" class="form-control">
									</select>
								</div>
							</div>
						</div>
					</div>

					<form:hidden path="parentLocation" id="parentLocation"/>

					<div class="form-group">
						<div class="row">
							<div class="col-5">
								<label for="exampleInputName"><spring:message code="lbl.loginLocation"/>  </label>
								<form:checkbox path="loginLocation" class="chk" />

								<label for="exampleInputName"><spring:message code="lbl.visitLocation"/>  </label>
								<form:checkbox path="visitLocation" class="chk" />

							</div>
						</div>
					</div>



					<div class="form-group">
						<div class="row">
							<div class="col-3">
								<input type="submit" value="<spring:message code="lbl.save"/>"
									   class="btn btn-primary btn-block" />
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	<!-- /.container-fluid-->
	<!-- /.content-wrapper-->
</div>

<!-- Bootstrap core JavaScript-->
<script src="<c:url value='/resources/js/jquery-1.10.2.js'/>"></script>
<script src="<c:url value='/resources/js/jquery-3.3.1.js' />"></script>
<script src="<c:url value='/resources/vendor/bootstrap/js/bootstrap.bundle.min.js'/>"></script>

<script src="<c:url value='/resources/js/checkbox.js'/>"></script>
<script src="<c:url value='/resources/js/jquery.modal.min.js'/>"></script>

<script>
	$(document).ready(function () {
		console.log("ready to fire");
		var div = $('#division-option').val();
		if (div != undefined && div != null && div != '') $('#division').show();
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

	function showHideOptions(nextDivId, tagOrLocationId, nextOptionId, tagOrLocation, tagId) {
		var selectedLocationTag = $("#locationTag").val();
		var parentLocationId = parseInt(tagOrLocationId.split("?")[0]);

		if (selectedLocationTag == 27) {
			parentLocationId = 0;
			$('#division').hide();
			$('#division-option').html("");
		}
		if (selectedLocationTag == 28) {
			parentLocationId = 9265;
			$('#division').hide();
			$('#division-option').html("");
		}
		$('#parentLocation').val(parentLocationId);

		if (selectedLocationTag > tagId) {
			$("#"+nextDivId).show();
			$('html, body').animate({
				scrollTop: $("#"+nextDivId).offset().top
			}, 500);
			if (tagOrLocationId != '' && tagOrLocationId != null && tagOrLocationId != -1 && tagOrLocationId != undefined && tagOrLocationId != "0?") {
				getLocationHierarchy("/opensrp-dashboard/"+tagOrLocation+"?id="+parentLocationId+"&title=", nextOptionId) ;
				$("#"+nextOptionId).prop('required', true);
			}
		}
	}

	$('#locationTag').change(function (event) {
		var divisionTagId = "28";

		$('#parentLocation').val(9265);
		showHideOptions("division", divisionTagId, "division-option", "location-by-tag-id", 28);

		$('#district').hide();
		$('#upazila').hide();
		$('#pourasabha').hide();
		$('#union').hide();

		$('#district-option').html("");
		$('#upazila-option').html("");
		$('#pourasabha-option').html("");
		$('#union-option').html("");
	});

	$("#division-option").change(function(event) {
		var selectedDivision = $("#division-option").val();
		showHideOptions("district", selectedDivision, "district-option", "child-locations", 29);

		$('#upazila').hide();
		$('#pourasabha').hide();
		$('#union').hide();

		$('#upazila-option').html("");
		$('#pourasabha-option').html("");
		$('#union-option').html("");
	});

	$("#district-option").change(function(event) {
		var selectedDistrict = $("#district-option").val();
		showHideOptions("upazila", selectedDistrict, "upazila-option", "child-locations", 30);

		$('#pourasabha').hide();
		$('#union').hide();

		$('#pourasabha-option').html("");
		$('#union-option').html("");
	});

	$("#upazila-option").change(function(event) {
		var selectedUpazila = $("#upazila-option").val();
		showHideOptions("pourasabha", selectedUpazila, "pourasabha-option", "child-locations", 31);

		$('#union').hide();

		$('#union-option').html("");
	});

	$("#pourasabha-option").change(function(event) {
		var selectedPourasabha = $("#pourasabha-option").val();
		showHideOptions("union", selectedPourasabha, "union-option", "child-locations", 32);
	});

	$("#union-option").change(function(event) {
		var selectedPourasabha = $("#union-option").val();
		var parentLocationId = parseInt(selectedPourasabha.split("?")[0]);
		$('#parentLocation').val(parentLocationId);
	});

</script>
<%--<script>--%>
<%--	$( function() {--%>
<%--		$.widget( "custom.combobox", {--%>
<%--			_create: function() {--%>
<%--				this.wrapper = $( "<div>" )--%>
<%--						.addClass( "custom-combobox" )--%>
<%--						.insertAfter( this.element );--%>

<%--				this.element.hide();--%>
<%--				this._createAutocomplete();--%>

<%--			},--%>

<%--			_createAutocomplete: function() {--%>
<%--				var selected = this.element.children( ":selected" ),--%>
<%--						value = selected.val() ? selected.text() : "";--%>
<%--				value = "<%=selectedParentLocationName%>";--%>
<%--				this.input = $( "<input>" )--%>
<%--						.appendTo( this.wrapper )--%>
<%--						.val( value )--%>
<%--						.attr( "title", "" )--%>
<%--						.attr( "name", "parentLocationName" )--%>
<%--						.attr( "required", true)--%>
<%--						.addClass( "form-control custom-combobox-input ui-widget ui-widget-content  ui-corner-left" )--%>
<%--						.autocomplete({--%>
<%--							delay: 0,--%>
<%--							minLength: 1,--%>
<%--							source: $.proxy( this, "_source" )--%>
<%--						})--%>
<%--						.tooltip({--%>
<%--							classes: {--%>
<%--								"ui-tooltip": "ui-state-highlight"--%>
<%--							}--%>
<%--						});--%>

<%--				this._on( this.input, {--%>
<%--					autocompleteselect: function( event, ui ) {--%>
<%--						ui.item.option.selected = true;--%>
<%--						$("#parentLocation").val(ui.item.option.value);--%>
<%--						this._trigger( "select", event, {--%>
<%--							item: ui.item.option--%>
<%--						});--%>
<%--					},--%>

<%--					autocompletechange: "_removeIfInvalid"--%>
<%--				});--%>
<%--			},--%>



<%--			_source: function( request, response ) {--%>
<%--				console.log("request term: ");--%>
<%--				console.log(request);--%>
<%--				console.log(response);--%>
<%--				console.log(request.term);--%>
<%--				$.ajax({--%>
<%--					type: "GET",--%>
<%--					dataType: 'html',--%>
<%--					url: "/opensrp-dashboard/location/search.html?name="+request.term,--%>
<%--					success: function(res)--%>
<%--					{--%>
<%--						console.log(request.term);--%>
<%--						$("#combobox").html(res);--%>
<%--					}--%>
<%--				});--%>
<%--				var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );--%>
<%--				response( this.element.children( "option" ).map(function() {--%>
<%--					var text = $( this ).text();--%>
<%--					if ( this.value && ( !request.term || matcher.test(text) ) )--%>
<%--						return {--%>
<%--							label: text,--%>
<%--							value: text,--%>
<%--							option: this--%>
<%--						};--%>
<%--				}) );--%>
<%--			},--%>

<%--			_removeIfInvalid: function( event, ui ) {--%>

<%--				// Selected an item, nothing to do--%>
<%--				if ( ui.item ) {--%>
<%--					return;--%>
<%--				}--%>

<%--				// Search for a match (case-insensitive)--%>
<%--				var value = this.input.val(),--%>
<%--						valueLowerCase = value.toLowerCase(),--%>
<%--						valid = false;--%>
<%--				this.element.children( "option" ).each(function() {--%>
<%--					if ( $( this ).text().toLowerCase() === valueLowerCase ) {--%>
<%--						this.selected = valid = true;--%>
<%--						return false;--%>
<%--					}--%>
<%--				});--%>

<%--				// Found a match, nothing to do--%>
<%--				if ( valid ) {--%>
<%--					return;--%>
<%--				}--%>

<%--				// Remove invalid value--%>
<%--				this.input--%>
<%--						.val( "" )--%>
<%--						.attr( "title", value + " didn't match any item" )--%>
<%--						.tooltip( "open" );--%>
<%--				$("#parentLocation").val(0);--%>
<%--				this.element.val( "" );--%>
<%--				this._delay(function() {--%>
<%--					this.input.tooltip( "close" ).attr( "title", "" );--%>
<%--				}, 2500 );--%>
<%--				this.input.autocomplete( "instance" ).term = "";--%>
<%--			},--%>

<%--			_destroy: function() {--%>
<%--				this.wrapper.remove();--%>
<%--				this.element.show();--%>
<%--			}--%>
<%--		});--%>

<%--		$( "#combobox" ).combobox();--%>

<%--		$( "#toggle" ).on( "click", function() {--%>
<%--			$( "#combobox" ).toggle();--%>
<%--		});--%>


<%--	} );--%>
<%--</script>--%>
<script src="<c:url value='/resources/js/jquery-ui.js'/>"></script>

<%-- <script src="<c:url value='/resources/jqwidgets/jqxcore.js'/>"></script>
<script src="<c:url value='/resources/jqwidgets/jqxdata.js'/>"></script>
<script src="<c:url value='/resources/jqwidgets/jqxbuttons.js'/>"></script>
<script src="<c:url value='/resources/jqwidgets/jqxscrollbar.js'/>"></script>
<script src="<c:url value='/resources/jqwidgets/jqxpanel.js'/>"></script>
<script src="<c:url value='/resources/jqwidgets/jqxtree.js'/>"></script>
<script src="<c:url value='/resources/jqwidgets/jqxdropdownbutton.js'/>"></script>

<script type="text/javascript">
        $(document).ready(function () {
            $("#dropDownButton").jqxDropDownButton({ width: 200, height: 25 });
    var dropDownContent1 = '<div style="position: relative; margin-left: 3px; margin-top: 5px;">' + "Please select" + '</div>';
     $("#dropDownButton").jqxDropDownButton('setContent', dropDownContent1);

            $('#jqxTree').on('select', function (event) {
                var args = event.args;
                var item = $('#jqxTree').jqxTree('getItem', args.element);

                var dropDownContent = '<div style="position: relative; margin-left: 3px; margin-top: 5px;">' + item.label + '</div>';
         $("#ContentPanel").html("<div style='margin: 10px;'>" + item.element.id + "</div>");
                $("#dropDownButton").jqxDropDownButton('setContent', dropDownContent);

            });

            var data = <%=locatationTreeData%>

            // prepare the data
            var source =
            {
                datatype: "json",
                datafields: [
                    { name: 'id' },
                    { name: 'parentid' },
                    { name: 'text' },
                    { name: 'value' }
                ],
                id: 'id',
                localdata: data
            };

            // create data adapter.
            var dataAdapter = new $.jqx.dataAdapter(source);
            // perform Data Binding.
            dataAdapter.dataBind();
            // get the tree items. The first parameter is the item's id. The second parameter is the parent item's id. The 'items' parameter represents
            // the sub items collection name. Each jqxTree item has a 'label' property, but in the JSON data, we have a 'text' field. The last parameter
            // specifies the mapping between the 'text' and 'label' fields.
            var records = dataAdapter.getRecordsHierarchy('id', 'parentid', 'items', [{ name: 'text', map: 'label'}]);
            $('#jqxTree').jqxTree({ source: records, width: '600px' });
        });
    </script>--%>

</body>
</html>
