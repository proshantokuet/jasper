package org.opensrp.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.opensrp.common.dto.MeetingCommonDTO;
import org.opensrp.core.entity.Meeting;
import org.opensrp.core.entity.MeetingDocument;
import org.opensrp.core.entity.MeetingType;
import org.opensrp.core.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("workplan-meeting")
public class WorkPlanMeetingController {
	
	@Autowired
	private MeetingService meetingService;
	
	private static final String UPLOADED_FOLDER = "/opt/multimedia/document/";
	
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public String list(Model model, Locale locale) {
		List<MeetingCommonDTO> meetings = meetingService.getMeetingList("WP");
		
		model.addAttribute("meetings", meetings);
		
		Map<String, Object> fielaValues = new HashMap<String, Object>();
		fielaValues.put("type", "WP");
		fielaValues.put("status", true);
		List<MeetingType> meetingTypes = meetingService.findAllByKeys(fielaValues, MeetingType.class);
		model.addAttribute("meetingTypes", meetingTypes);
		
		model.addAttribute("locale", locale);
		model.addAttribute("title", "Workplan meeting");
		return "workplan-meeting/index";
	}
	
	@RequestMapping(value = "/add.html", method = RequestMethod.GET)
	public String add(Model model, Locale locale) {
		model.addAttribute("locale", locale);
		Map<String, Object> fielaValues = new HashMap<String, Object>();
		fielaValues.put("type", "WP");
		fielaValues.put("status", true);
		List<MeetingType> meetingTypes = meetingService.findAllByKeys(fielaValues, MeetingType.class);
		model.addAttribute("meetingTypes", meetingTypes);
		fielaValues.put("type", "PL");
		List<MeetingType> meetingPlaces = meetingService.findAllByKeys(fielaValues, MeetingType.class);
		model.addAttribute("meetingPlaces", meetingPlaces);
		
		model.addAttribute("meetingTypeId", 0);
		
		model.addAttribute("title", "Add new workplan meeting");
		return "workplan-meeting/add";
	}
	
	@RequestMapping(value = "/{id}/edit.html", method = RequestMethod.GET)
	public String edit(@PathVariable("id") int id, Model model, Locale locale) {
		Meeting meeting = meetingService.findById(id, "id", Meeting.class);
		
		Map<String, Object> fielaValues = new HashMap<String, Object>();
		fielaValues.put("type", "WP");
		fielaValues.put("status", true);
		List<MeetingType> meetingTypes = meetingService.findAllByKeys(fielaValues, MeetingType.class);
		
		model.addAttribute("meetingTypes", meetingTypes);
		fielaValues.put("type", "PL");
		List<MeetingType> meetingPlaces = meetingService.findAllByKeys(fielaValues, MeetingType.class);
		model.addAttribute("meetingPlaces", meetingPlaces);
		model.addAttribute("locale", locale);
		model.addAttribute("meeting", meeting);
		
		model.addAttribute("title", "Edit workplan meeting");
		return "workplan-meeting/edit";
	}
	
	@RequestMapping(value = "/{id}/view.html", method = RequestMethod.GET)
	public String view(@PathVariable("id") int id, Model model, Locale locale) {
		MeetingCommonDTO meeting = meetingService.getMeetingById(id);
		Meeting document = meetingService.findById(id, "id", Meeting.class);
		String others = meeting.getFiles();
		List<String> otherImages = new ArrayList<String>();
		if (others != null) {
			String othersArray[] = others.split(",");
			otherImages = Arrays.asList(othersArray);
		}
		
		model.addAttribute("otherImages", otherImages);
		model.addAttribute("locale", locale);
		model.addAttribute("meeting", meeting);
		model.addAttribute("document", document);
		
		model.addAttribute("title", "View work plan");
		return "workplan-meeting/view";
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteImage(@PathVariable("id") Long id, Model model, Locale locale) {
		
		MeetingDocument meetingDocument = meetingService.findById(id, "id", MeetingDocument.class);
		try {
			File file = new File(UPLOADED_FOLDER + "/" + meetingDocument.getFileName());
			if (file.delete()) {
				meetingService.deleteDoc(id);
			} else {
				model.addAttribute("msg", "File does not deleted , becasue file does not found in the server.");
			}
			Meeting meeting = meetingService.findById(meetingDocument.getMeeting().getId(), "id", Meeting.class);
			model.addAttribute("meeting", meeting);
		}
		catch (Exception e) {
			model.addAttribute("msg", e);
		}
		
		return "workplan-meeting/delete";
	}
	
}
