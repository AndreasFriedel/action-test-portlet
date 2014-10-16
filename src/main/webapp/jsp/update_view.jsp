<%@include file="init.jsp"%>

<h2>ActionTest - Portlet</h2>

<liferay-portlet:renderURL var="renderUrl"/>

<p>Session updated <c:out value="${updatedValue}"/></p>
<p><a href="${renderUrl}">Back</a>