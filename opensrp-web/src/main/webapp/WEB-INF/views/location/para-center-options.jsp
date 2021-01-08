<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@ page import="org.opensrp.core.entity.ParaCenter" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
    List<ParaCenter>  dataList = (List<ParaCenter>)session.getAttribute("paraCenterData");
%>
<%   for (ParaCenter p : dataList) {%>
<option value="<%=p.getId()%>"><%=p.getName()%></option>
<%}%>
