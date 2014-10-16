<%@include file="init.jsp"%>

<%@ page import="com.liferay.portal.kernel.util.HttpUtil" %>
<%@ page import="com.liferay.util.Encryptor" %>
<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="com.liferay.portal.security.auth.AuthTokenUtil" %>

<h2>ActionTest - Portlet</h2>

<div class="alert alert-info">
	Löschen Sie alle Cookies und versuchen Sie mit einem der beiden Buttons einen ActionRequest auszulösen.
</div>

<liferay-portlet:actionURL var="actionUrl">
	<portlet:param name="action" value="secureAction"></portlet:param>
	<portlet:param name="sessionValue" value="${sessionTimestamp}"></portlet:param>
</liferay-portlet:actionURL>

<liferay-portlet:actionURL var="actionIgnoreUrl">
	<portlet:param name="action" value="whitelistAction"></portlet:param>
	<portlet:param name="sessionValue" value="${sessionTimestamp}"></portlet:param>
</liferay-portlet:actionURL>

<c:set var="authParam" value="<%=HttpUtil.encodeURL(AuthTokenUtil.getToken(request)) %>"/>

<form:form action="${actionUrl}&p_auth=${authParam}" method="POST">
	<p>Die Action, die mit dem <i>Sicher</i> Button verknüpft ist, kann nur aufgerufen werden, wenn das CSRF-Token gültig ist.</p>
	<input type="submit" name="submit" value="Sicher" />
</form:form>

<form:form action="${actionIgnoreUrl}&p_auth=${authParam}" method="POST">
	<p>Die Action, die mit dem <i>Whitelisted Action</i> Button verknüpft ist, kann immer aufgerufen werden.</p>
	<input type="submit" name="submit" value="Whitelisted Action" />
</form:form>