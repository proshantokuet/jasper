<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@ page import="org.opensrp.core.entity.ParaCenter" %>
<%@ page import="org.opensrp.core.entity.Location" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
    List<Location>  dataList = (List<Location>)session.getAttribute("locationData");
%>
<%   for (Location l : dataList) {%>
<option value="<%=l.getId()%>"><%=l.getName()%></option>
<%}%>
