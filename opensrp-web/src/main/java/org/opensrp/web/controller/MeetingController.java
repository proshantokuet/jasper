package org.opensrp.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
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
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

@Controller
@RequestMapping("other-meeting-minutes")
public class MeetingController {
	
	private static final String UPLOADED_FOLDER = "/opt/multimedia/document/";
	
	@Autowired
	private MeetingService meetingService;
	
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public String list(Model model, Locale locale) {
		List<MeetingCommonDTO> meetings = meetingService.getMeetingList("OMM");
		
		model.addAttribute("meetings", meetings);
		Map<String, Object> fielaValues = new HashMap<String, Object>();
		fielaValues.put("type", "OMM");
		fielaValues.put("status", true);
		List<MeetingType> meetingTypes = meetingService.findAllByKeys(fielaValues, MeetingType.class);
		model.addAttribute("meetingTypes", meetingTypes);
		model.addAttribute("locale", locale);
		model.addAttribute("title", "General meeting minutes");
		return "other-meeting/index";
	}
	
	@RequestMapping(value = "/add.html", method = RequestMethod.GET)
	public String add(Model model, Locale locale) {
		model.addAttribute("locale", locale);
		Map<String, Object> fielaValues = new HashMap<String, Object>();
		fielaValues.put("type", "OMM");
		fielaValues.put("status", true);
		List<MeetingType> meetingTypes = meetingService.findAllByKeys(fielaValues, MeetingType.class);
		model.addAttribute("meetingTypes", meetingTypes);
		fielaValues.put("type", "PL");
		List<MeetingType> meetingPlaces = meetingService.findAllByKeys(fielaValues, MeetingType.class);
		model.addAttribute("meetingPlaces", meetingPlaces);
		
		model.addAttribute("meetingTypeId", 0);
		
		model.addAttribute("title", "Add new general meeting minutes");
		return "other-meeting/add";
	}
	
	@RequestMapping(value = "/{id}/edit.html", method = RequestMethod.GET)
	public String edit(@PathVariable("id") int id, Model model, Locale locale) {
		Meeting meeting = meetingService.findById(id, "id", Meeting.class);
		
		Map<String, Object> fielaValues = new HashMap<String, Object>();
		fielaValues.put("type", "OMM");
		fielaValues.put("status", true);
		List<MeetingType> meetingTypes = meetingService.findAllByKeys(fielaValues, MeetingType.class);
		
		model.addAttribute("meetingTypes", meetingTypes);
		fielaValues.put("type", "PL");
		List<MeetingType> meetingPlaces = meetingService.findAllByKeys(fielaValues, MeetingType.class);
		model.addAttribute("meetingPlaces", meetingPlaces);
		model.addAttribute("locale", locale);
		model.addAttribute("meeting", meeting);
		
		model.addAttribute("title", "Edit general meeting minutes");
		return "other-meeting/edit";
	}
	
	@RequestMapping(value = "/{id}/view.html", method = RequestMethod.GET)
	public String view(@PathVariable("id") int id, Model model, Locale locale) {
		Meeting document = meetingService.findById(id, "id", Meeting.class);
		MeetingCommonDTO meeting = meetingService.getMeetingById(id);
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
		model.addAttribute("title", "View general meeting minutes");
		return "other-meeting/view";
	}
	
	@RequestMapping(value = "/{id}/{type}/export.html", method = RequestMethod.GET)
	public void exportToCSV(HttpServletResponse response, @PathVariable("id") int id, @PathVariable("type") String type)
	    throws IOException, JSONException {
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());
		
		String typeTitle = "";
		String fileName = "";
		if (type.equalsIgnoreCase("OMM")) {
			typeTitle = "Meeting Type";
			fileName = "general_meeting";
		} else {
			typeTitle = "WorkPlane Type";
			fileName = "work_plane_meeting";
		}
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName + "_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);
		
		//List<MeetingCommonDTO> meetings = meetingService.getMeetingListForExport(json);
		MeetingCommonDTO meeting = meetingService.getMeetingById(id);
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Meeting Date", typeTitle, "Meeting place", "Meeting Place Other", "Total participants",
		        "Discussion & Action Points" };
		String[] nameMapping = { "meetingDate", "meetingOrWorkPlane", "place", "meetingPlaceOther", "participants",
		        "description" };
		
		csvWriter.writeHeader(csvHeader);
		csvWriter.write(meeting, nameMapping);
		/*for (MeetingCommonDTO meeting : meetings) {
			csvWriter.write(meeting, nameMapping);
		}*/
		
		csvWriter.close();
		
	}
	
	@RequestMapping(value = "/{id}/{type}/downlaod.html", method = RequestMethod.GET)
	public void downlaodDocs(HttpServletResponse response, @PathVariable("id") int id, @PathVariable("type") String type)
	    throws IOException, JSONException {
		
		String fileName = "";
		if (type.equalsIgnoreCase("OMM")) {
			
			fileName = "general_meeting";
		} else {
			
			fileName = "work_plane_meeting";
		}
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());
		
		Meeting meeting = meetingService.findById(id, "id", Meeting.class);
		Set<MeetingDocument> meetingDocuments = meeting.getMeetingDocuments();
		
		response.setContentType("Content-type: text/zip");
		response.setHeader("Content-Disposition", "attachment; filename= " + fileName + "_" + currentDateTime + ".zip");
		List<File> files = new ArrayList<>();
		for (MeetingDocument meetingDocument : meetingDocuments) {
			String url = UPLOADED_FOLDER + meetingDocument.getFileName();
			files.add(new File(url));
		}
		
		ServletOutputStream out = response.getOutputStream();
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));
		
		for (File file : files) {
			
			System.out.println("Adding " + file.getName());
			zos.putNextEntry(new ZipEntry(file.getName()));
			
			// Get the file
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				
			}
			catch (FileNotFoundException fnfe) {
				// If the file does not exists, write an error entry instead of
				// file
				// contents
				zos.write(("ERRORld not find file " + file.getName()).getBytes());
				zos.closeEntry();
				System.out.println("Couldfind file " + file.getAbsolutePath());
				continue;
			}
			
			BufferedInputStream fif = new BufferedInputStream(fis);
			
			// Write the contents of the file
			int data = 0;
			while ((data = fif.read()) != -1) {
				zos.write(data);
			}
			fif.close();
			
			zos.closeEntry();
			System.out.println("Finishedng file " + file.getName());
		}
		
		zos.close();
		
	}
	
}
