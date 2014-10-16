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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.Validator;

public class CustomCSRFWhitelistActionFilter extends AbstractCustomCSRFWhitelistActionFilter {
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomCSRFWhitelistActionFilter.class);
	
	private List<KeyValuePair> parameter = new ArrayList<KeyValuePair>();

	@Override
    public void init(final FilterConfig filterConfig) throws PortletException {
		LOG.debug("Filter init");
	    String[] whitelistKeyValues = getWhitelistKeyValues(filterConfig);
	    LOG.debug("Configured values: " + ArrayUtils.toString(whitelistKeyValues));
	    if(whitelistKeyValues != null){
	    	initParameterList(whitelistKeyValues);
	    }
	    LOG.debug("Filter init completed");
    }
	
	private String[] getWhitelistKeyValues(final FilterConfig filterConfig){
		final String whitelistString = filterConfig.getInitParameter("WhitelistParams");
		String[] whitelistKeyValues = null;
		if(Validator.isNotNull(whitelistString)){
			if(whitelistString.contains(",")){
				whitelistKeyValues = whitelistString.split(",");
			} else {
				whitelistKeyValues = new String[] { whitelistString };
			}
		}
		return whitelistKeyValues;
	}
	
	private void initParameterList(final String[] whitelistKeyValues){
		for (final String keyValueString : whitelistKeyValues) {
	        if(keyValueString.contains("#")){
	        	final String[] keyValue = keyValueString.split("#");
	        	if(keyValue.length == 2 && (!keyValue[0].isEmpty() && !keyValue[1].isEmpty())){
	        		parameter.add(new KeyValuePair(keyValue[0], keyValue[1]));
	        	}
	        }
        }
	}

	public void doFilter(final ActionRequest request, final ActionResponse response, final FilterChain chain) throws IOException,
	        PortletException {
		if (isCSRFTokenValid(request) || isActionWhitelisted(request)) {
			LOG.debug("Pre ActionRequest");
			chain.doFilter(request, response);
			LOG.debug("Post ActionRequest");
		} else {
			// ActionRequest wird ignoriert
			LOG.warn("Ignoring ActionRequest - CSRFToken is not valid");
		}

	}

	private boolean isActionWhitelisted(final ActionRequest request) {
		boolean whitelisted = false;
		if(!parameter.isEmpty()){
			for (KeyValuePair keyValue : parameter) {
	            final String requestValue = request.getParameter(keyValue.getKey());
	            if(keyValue.getValue().equals(requestValue)){
	            	whitelisted = true;
	            	break;
	            }
            }
		}
		return whitelisted;
	}

}
