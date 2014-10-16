/*
Copyright 2014 Andreas Friedel

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package de.inovex.auth.token.ignore;

import javax.portlet.ActionRequest;
import javax.portlet.PortletException;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PortalUtil;

public abstract class AbstractCustomCSRFWhitelistActionFilter implements ActionFilter {

	private static final String P_AUTH = "p_auth";
	private static final String _CSRF = "#CSRF";

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCustomCSRFWhitelistActionFilter.class);

	public void init(FilterConfig filterConfig) throws PortletException {

	}

	public void destroy() {

	}

	protected boolean isCSRFTokenValid(ActionRequest actionRequest) {
		final HttpServletRequest request = PortalUtil.getOriginalServletRequest(PortalUtil
		        .getHttpServletRequest(actionRequest));
		final String csrfToken = ParamUtil.getString(request, P_AUTH);
		LOG.debug("P_Auth parameter value: " + csrfToken);
		return csrfToken.equals(getSessionAuthenticationToken(actionRequest));
	}

	protected String getSessionAuthenticationToken(final ActionRequest actionRrequest) {
		final HttpSession session = PortalUtil.getHttpServletRequest(actionRrequest).getSession(false);
		if (session != null) {
			final String tokenKey = WebKeys.AUTHENTICATION_TOKEN.concat(_CSRF);
			final String tokenValue = (String) session.getAttribute(tokenKey);
			LOG.debug("P_Auth session value: " + tokenValue);
			return tokenValue;
		} else {
			return null;
		}
	}

}
