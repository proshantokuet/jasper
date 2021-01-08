<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="org.opensrp.common.util.CheckboxHelperUtil"%>
<%@page import="java.util.List"%>
<%@page import="org.opensrp.core.entity.Permission"%>
<jsp:include page="/WEB-INF/views/header.jsp" />

    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
  

<style>

#image-gallery .modal-footer{
  display: block;
}

.thumb{
  margin-top: 15px;
  margin-bottom: 15px;
} 

.thumbnail {
    display: block;
    padding: 4px;
    /* margin-bottom: 20px; */
    line-height: 1.42857143;
    background-color: #fff;
    border: 0px solid #ddd;
    border-radius: 0px;
    height: 100px;
    width: 100px;
    -o-transition: border .2s ease-in-out;
    transition: border .2s ease-in-out;
}   
.img-thumbnail {
    padding: .25rem;
    background-color: #fff;
    border: 1px solid #dee2e6;
    border-radius: .25rem;
    max-width: 750px !important; 
    height:75px !important;
}
</style>

<c:url var="listUrl" value="/workplan-meeting/list.html" />

<c:url var="uploadUrl" value="/rest/api/v1/meeting/upload" />
<c:url var="export_url" value="/other-meeting-minutes/${meeting.id}/WP/export.html" />
<c:url var="download_url" value="/other-meeting-minutes/${meeting.id}/WP/downlaod.html" />
<div class="page-content-wrapper">
		<div class="page-content">
			
			
			<ul class="page-breadcrumb breadcrumb">
				<li>
					<a href="<c:url value="/"/>">Home</a>
					<i class="fa fa-circle"></i>
				</li>
				<li>
					<a href="<c:url value="/workplan-meeting/list.html"/>">Back</a>
					
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
								<i class="fa fa-view"></i>Work plan details
							</div>
							
							
						</div>
						
						<div class="portlet-body">
							
								<div class="form-group">
					              <div class="row">
					                <div class="col-lg-12 form-group"> 
						                <label class="control-label" for="name"><strong> Work Plan Type:</strong> </label>
										${meeting.getMeetingOrWorkPlane() }
					                 </div>
					                  <div class="col-lg-12 form-group">
					                   <label class="control-label" for="code"> <strong>Work Plan Made on:</strong> </label>
										${meeting.getMeetingDate() }
					                  </div>
					                 
					              </div>
					              
					              <div class="row">
					                <div class="col-lg-12 form-group"> 
						                <label class="control-label" for="name"><strong> Meeting Place: </strong></label>
										${meeting.getPlace() }
					                 </div>
					                  <div class="col-lg-12 form-group">
					                   <label class="control-label" for="code"> <strong>Other place name: </strong></label>
										${meeting.getMeetingPlaceOther() } 
										
					                  </div>
					                 
					              </div>
					              
					              
					              <div class="row">
					               
					                 
					                  
					                   <div class="col-lg-12 form-group"> 
						                <label class="control-label" for="name"> <strong>Total Participants: </strong>	</label>
										${meeting.getParticipants() } 
					                 </div>
					                  <div class="col-lg-12 form-group">
					                   <label class="control-label" for="code"><strong> Discussion & Action Points:</strong> </label>
										 
										<pre>${meeting.getDescription() }</pre> 
					                  </div>
					                 
					              </div>
					              
					              <c:choose>
						              <c:when test="${document.getMeetingDocuments().size() != 0}">
							              <div class="row">
							              
							                <div class="col-lg-12 form-group"> 
							                 <label class="control-label" for="name"> <strong>Documents: </strong>	</label><br />
								                 <c:forEach items="${document.getMeetingDocuments()}" var="image">				                        
				    									<div class="col-lg-2 form-group">
									                 		<a style="color:blue;text-decoration: underline" class="" target="_new" href="/opt/multimedia/document/${image.fileName }"><strong>${image.originalName }</strong></a>
									                 	</div>
									                 	</c:forEach>
							                
							                 </div>
							             </div> 
						             </c:when>
						             <c:otherwise>No documents found</c:otherwise>
					             </c:choose>
					             
					            <div class="row ">
					              <div class="form-group col-lg-12 ">
					             	<div class="col-lg-6 form-group text-right"> 
					             	<form action="${export_url }">
							    			<button type="submit"  class="btn btn-primary" value="confirm">Export data </button>
							    			
						   			  </form> 
					             	</div>
					             	<div class="col-lg-6 form-grouptext-right text-left"> 
					             	<form action="${download_url }">
							    			
							    			<button type="submit"  class="btn btn-primary" value="confirm">Download attachments </button>
						   			  </form> 
					             	</div>
					             	</div>
					             </div>
							
						</div>
					</div>
					
					
					<div class="modal fade" id="image-gallery" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					            <div class="modal-dialog modal-lg">
					                <div class="modal-content">
					                	<div class="modal-footer">
					                        <button type="button" class="btn btn-secondary float-left" id="show-previous-image"><i class="fa fa-arrow-left"></i>
					                        </button>
					
					                        <button type="button" id="show-next-image" class="btn btn-secondary float-right"><i class="fa fa-arrow-right"></i>
					                        </button>
					                    </div>
					                    <div class="modal-header">
					                        <h4 class="modal-title" id="image-gallery-title"></h4>
					                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span>
					                        </button>
					                    </div>
					                    <div class="modal-body">
					                        <img id="image-gallery-image" class="img-responsive col-md-12" src="">
					                    </div>
					                    <div class="modal-footer">
					                        <button type="button" class="btn btn-secondary float-left" id="show-previous-image"><i class="fa fa-arrow-left"></i>
					                        </button>
					
					                        <button type="button" id="show-next-image" class="btn btn-secondary float-right"><i class="fa fa-arrow-right"></i>
					                        </button>
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
<script>
let modalId = $('#image-gallery');

$(document)
  .ready(function () {

    loadGallery(true, 'a.thumbnail');

    //This function disables buttons when needed
    function disableButtons(counter_max, counter_current) {
      $('#show-previous-image, #show-next-image')
        .show();
      if (counter_max === counter_current) {
        $('#show-next-image')
          .hide();
      } else if (counter_current === 1) {
        $('#show-previous-image')
          .hide();
      }
    }

    /**
     *
     * @param setIDs        Sets IDs when DOM is loaded. If using a PHP counter, set to false.
     * @param setClickAttr  Sets the attribute for the click handler.
     */

    function loadGallery(setIDs, setClickAttr) {
      let current_image,
        selector,
        counter = 0;

      $('#show-next-image, #show-previous-image')
        .click(function () {
          if ($(this)
            .attr('id') === 'show-previous-image') {
            current_image--;
          } else {
            current_image++;
          }

          selector = $('[data-image-id="' + current_image + '"]');
          updateGallery(selector);
        });

      function updateGallery(selector) {
        let $sel = selector;
        current_image = $sel.data('image-id');
        $('#image-gallery-title')
          .text($sel.data('title'));
        $('#image-gallery-image')
          .attr('src', $sel.data('image'));
        disableButtons(counter, $sel.data('image-id'));
      }

      if (setIDs == true) {
        $('[data-image-id]')
          .each(function () {
            counter++;
            $(this)
              .attr('data-image-id', counter);
          });
      }
      $(setClickAttr)
        .on('click', function () {
          updateGallery($(this));
        });
    }
  });

// build key actions
$(document)
  .keydown(function (e) {
    switch (e.which) {
      case 37: // left
        if ((modalId.data('bs.modal') || {})._isShown && $('#show-previous-image').is(":visible")) {
          $('#show-previous-image')
            .click();
        }
        break;

      case 39: // right
        if ((modalId.data('bs.modal') || {})._isShown && $('#show-next-image').is(":visible")) {
          $('#show-next-image')
            .click();
        }
        break;

      default:
        return; // exit this handler for other keys
    }
    e.preventDefault(); // prevent the default action (scroll / move caret)
  });
	/* $(window).bind('beforeunload', function(){
	  return 'Are you sure you want to leave?';
	}); */

</script>

 