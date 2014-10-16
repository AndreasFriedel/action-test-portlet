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
package de.inovex.liferay.tipsandtricks.actiontest;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

@org.springframework.stereotype.Controller
@RequestMapping(value = "VIEW")
public class WhitelistTestController {
	
	private static final Log LOG = LogFactoryUtil.getLog(WhitelistTestController.class);
	
	private static final String VIEW = "test_view";
	
	private static final String UPDATE_VIEW = "update_view";
	
	private static final String SESSION_KEY = "customSessionValue";
	
	@ActionMapping(params = "action=secureAction")
	public void secureAction(final ActionRequest request, final ActionResponse response, @RequestParam final String sessionValue){
		doAction(request, response, sessionValue);
	}
	
	@ActionMapping(params = "action=whitelistAction")
	public void whitelistAction(final ActionRequest request, final ActionResponse response, @RequestParam final String sessionValue){
		doAction(request, response, sessionValue);
	}
	
	private void doAction(final ActionRequest request, final ActionResponse response, final String sessionValue){
		LOG.info("Recived: " + sessionValue);
		request.getPortletSession().setAttribute(SESSION_KEY, sessionValue);
		LOG.info("Session updated");
		response.setRenderParameter("update", "success");
	}

	@RequestMapping(params="update=success")
	public String view(final RenderRequest request, final Model model) {
		model.addAttribute("sessionTimestamp", System.currentTimeMillis());
		model.addAttribute("updatedValue", "NewValue:" + request.getPortletSession().getAttribute(SESSION_KEY));
		return UPDATE_VIEW;
	}
	
	@RenderMapping()
	public String viewUpdateSuccessful(final RenderRequest request, final Model model) {
		model.addAttribute("sessionTimestamp", System.currentTimeMillis());
		return VIEW;
	}
}
