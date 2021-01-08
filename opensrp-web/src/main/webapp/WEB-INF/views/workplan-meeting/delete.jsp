<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
	<c:when test="${meeting.getMeetingDocuments().size() != 0}">
		<div class="row">
			<div class="col-lg-12 form-group">
				<label class="control-label" for="name"> <strong>Documents:
				</strong>
				</label><br />
				<c:forEach items="${meeting.getMeetingDocuments()}" var="image">
					<div class="col-lg-6 form-group">
						<a style="color: blue; text-decoration: underline" class=""
							target="_new" href="/opt/multimedia/document/${image.fileName }">
							<strong>${image.originalName }</strong>
						</a> &nbsp
						<button id="${image.id }" onclick="del(${image.id })"
							type="button" class="btn-close" aria-label="Close">X</button>

					</div>
				</c:forEach>

			</div>
		</div>
	</c:when>
	<c:otherwise>No documents found</c:otherwise>
</c:choose>
<div class="row">
	<div class="col-lg-12 form-group">
	<h3><strong>${msg }</strong></h3>
	</div>

</div>