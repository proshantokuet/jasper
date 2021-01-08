package org.opensrp.web.controller;

import java.util.List;
import java.util.Locale;

import org.opensrp.core.entity.DhisConfiguration;
import org.opensrp.core.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Dhis2ConfigurationController {
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value = "/dhis2-configuartion-list.html", method = RequestMethod.GET)
	public String list(Model model, Locale locale) {
		List<DhisConfiguration> dhisConfigurations = commonService.findAll("DhisConfiguration");
		model.addAttribute("dhisConfigurations", dhisConfigurations);
		model.addAttribute("locale", locale);
		model.addAttribute("title", "Dhis Configuration list");
		return "dhis-configuration/index";
	}
	
	@RequestMapping(value = "/dhis2-configuartion/add.html", method = RequestMethod.GET)
	public String add(Model model, Locale locale) {
		model.addAttribute("locale", locale);
		model.addAttribute("title", "Add new dhis2 configuration");
		return "dhis-configuration/add";
	}
	
	@RequestMapping(value = "/dhis2-configuartion/{id}/edit.html", method = RequestMethod.GET)
	public String edit(@PathVariable("id") int id, Model model, Locale locale) {
		DhisConfiguration dhisConfiguration = commonService.findById(id, "id", DhisConfiguration.class);
		
		model.addAttribute("locale", locale);
		model.addAttribute("dhisConfiguration", dhisConfiguration);
		
		model.addAttribute("title", "Edit dhis2 configuration");
		return "dhis-configuration/edit";
	}
	
}
