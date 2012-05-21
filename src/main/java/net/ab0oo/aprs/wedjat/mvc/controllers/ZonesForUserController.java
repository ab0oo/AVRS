/*
 * AVRS - http://avrs.sourceforge.net/
 *
 * Copyright (C) 2011 John Gorkos, AB0OO
 *
 * AVRS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * AVRS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AVRS; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package net.ab0oo.aprs.wedjat.mvc.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ab0oo.aprs.wedjat.models.User;
import net.ab0oo.aprs.wedjat.models.Zone;
import net.ab0oo.aprs.wedjat.service.WedjatService;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

/**
 * @author johng
 *
 */
public class ZonesForUserController extends AbstractCommandController {
    private static final String BASE_VIEW_NAME = "userZones";
	private WedjatService wedjatService;
	
	public ZonesForUserController() {
		setCommandClass(User.class);
		setCommandName("user");
	}

	@Override
    @SuppressWarnings("unchecked")
	protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) {
		User user = (User)command;
		
		List<Zone> userZones = wedjatService.getZonesByUserId(user.getUserId());
		Map<String,Object> model = errors.getModel();
		model.put("zones", userZones);
		model.put("user", user);
		return new ModelAndView(getViewName(request), model);
	}
	
    private String getViewName(HttpServletRequest request) {
	    String requestUri = request.getRequestURI();
	    String extension = "." + requestUri.substring(requestUri.length() - 3);
	    if(".html".equals(extension)) { extension=""; }
	    return BASE_VIEW_NAME + extension;
	}

	/**
	 * @param wedjatService the wedjatService to set
	 */
	public final void setWedjatService(WedjatService wedjatService) {
		this.wedjatService = wedjatService;
	}
}
