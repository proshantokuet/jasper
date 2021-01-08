<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@ page import="org.opensrp.core.entity.ParaCenter" %>
<%@ page import="org.opensrp.core.entity.Cluster" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
    List<Cluster>  dataList = (List<Cluster>)session.getAttribute("clusterData");
%>
<%   for (Cluster c : dataList) {%>
<option value="<%=c.getId()%>"><%=c.getName()%></option>
<%}%>
