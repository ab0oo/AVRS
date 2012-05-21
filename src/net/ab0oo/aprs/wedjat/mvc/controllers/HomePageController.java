package net.ab0oo.aprs.wedjat.mvc.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ab0oo.aprs.wedjat.models.MonitoredStation;
import net.ab0oo.aprs.wedjat.service.WedjatService;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class HomePageController extends AbstractController {
	private WedjatService wedjatService;

	public HomePageController() {
	}

	@Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<MonitoredStation> monitoredStations = wedjatService.getMonitoredStations();
		System.out.println("There are "+monitoredStations.size()+" stations in the MSL");

		return new ModelAndView("home", "stations", monitoredStations);
	}

	// injected
	public void setWedjatService(WedjatService wedjatService) {
		this.wedjatService = wedjatService;
	}
}
