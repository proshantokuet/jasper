package org.opensrp.web.rest.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.common.util.ImageUpload;
import org.opensrp.core.entity.MeetingDocument;
import org.opensrp.core.service.MeetingService;
import org.opensrp.web.util.AuthenticationManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;

@RestController
@RequestMapping("rest/api/v1/meeting")
public class MeetingRestController {
	
	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private ImageUpload imageUpload;
	
	private static final String UPLOADED_FOLDER = "/opt/multimedia/document/";
	
	@RequestMapping(value = "/save-or-update", method = RequestMethod.POST)
	public ResponseEntity<String> saveOrUpdate(MultipartHttpServletRequest request) throws JSONException {
		
		JSONObject response = new JSONObject();
		try {
			
			meetingService.saveOrUpdate(request);
			
			response.put("status", "CREATED");
			response.put("msg", "you have successfully created.");
		}
		catch (Exception e) {
			response.put("status", "ERROR");
			response.put("msg", e.getMessage());
		}
		return new ResponseEntity<>(new Gson().toJson(response.toString()), HttpStatus.OK);
	}
	
	/*@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<String> imageUpload(@RequestParam MultipartFile[] file) throws Exception {
		JSONObject response = new JSONObject();
		try {
			for (MultipartFile multipartFile : file) {
				System.err.println("" + multipartFile.getOriginalFilename());
				String name = imageUpload.upload(multipartFile, UPLOADED_FOLDER, false, 500);
				response.put("fileName", multipartFile.getOriginalFilename());
				response.put("name", name);
				response.put("status", "OK");
			}
			return new ResponseEntity<>(new Gson().toJson(response.toString()), OK);
		}
		catch (Exception e) {
			response.put("status", "");
			return new ResponseEntity<>(new Gson().toJson(response.toString()), OK);
		}
		
	}*/
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<String> imageUpload(MultipartHttpServletRequest request, HttpServletRequest requests)
	    throws Exception {
		JSONObject response = new JSONObject();
		try {
			System.err.println(request.getParameter("totalParticipants"));
			System.err.println("DD:" + requests.getAttribute("totalParticipants"));
			List<MultipartFile> papers = request.getFiles("picture");
			for (MultipartFile multipartFile : papers) {
				System.err.println("" + multipartFile.getOriginalFilename());
				String name = imageUpload.upload(multipartFile, UPLOADED_FOLDER, false, 500);
				response.put("fileName", multipartFile.getOriginalFilename());
			}
			/*for (MultipartFile multipartFile : file) {
				System.err.println("" + multipartFile.getOriginalFilename());
				String name = imageUpload.upload(multipartFile, UPLOADED_FOLDER, false, 500);
				response.put("fileName", multipartFile.getOriginalFilename());
				response.put("name", name);
				response.put("status", "OK");
			}*/
			return new ResponseEntity<>(new Gson().toJson(response.toString()), OK);
		}
		catch (Exception e) {
			response.put("status", "");
			return new ResponseEntity<>(new Gson().toJson(response.toString()), OK);
		}
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteImage(@PathVariable("id") int id) throws Exception {
		JSONObject response = new JSONObject();
		try {
			MeetingDocument meetingDocument = meetingService.findById(id, "id", MeetingDocument.class);
			File file = new File(UPLOADED_FOLDER + "/" + meetingDocument.getFileName());
			if (file.delete()) {
				response.put("status", "Deleted");
				response.put("msg", "You have deleted succefully");
				meetingService.delete(meetingDocument);
			}
			
		}
		catch (Exception e) {
			response.put("status", "ERROR");
			response.put("msg", e.getMessage());
			
		}
		return new ResponseEntity<>(new Gson().toJson(response.toString()), OK);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<String> workPlan(HttpServletRequest request) throws Exception {
		//		String branch = request.getParameter("branch");
		String meetingType = request.getParameter("meeting_type");
		String startDate = request.getParameter("start_date");
		String endDate = request.getParameter("end_date");
		String type = request.getParameter("type");
		Integer draw = Integer.valueOf(request.getParameter("draw"));
		Integer meetingTypeId = StringUtils.isBlank(meetingType) ? 0 : Integer.valueOf(meetingType);
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer length = Integer.valueOf(request.getParameter("length"));
		boolean editPermitted = AuthenticationManagerUtil.isPermitted("EDIT_WORK_MEETING");
		JSONObject json = new JSONObject();
		json.put("meeting_type", meetingTypeId);
		json.put("start_date", startDate);
		json.put("end_date", endDate);
		json.put("offset", start);
		json.put("limit", length);
		json.put("type", type);
		List<String> users = meetingService.getMeetingList(json);
		Integer totalUser = meetingService.getMeetingListCount(json);
		
		JSONObject response = meetingService.getDataTable(draw, totalUser, users, editPermitted);
		return new ResponseEntity<>(response.toString(), OK);
	}
	
	@RequestMapping(value = "/general_list", method = RequestMethod.GET)
	public ResponseEntity<String> generalMeeting(HttpServletRequest request) throws Exception {
		//		String branch = request.getParameter("branch");
		String meetingType = request.getParameter("meeting_type");
		String startDate = request.getParameter("start_date");
		String endDate = request.getParameter("end_date");
		String type = request.getParameter("type");
		Integer draw = Integer.valueOf(request.getParameter("draw"));
		Integer meetingTypeId = StringUtils.isBlank(meetingType) ? 0 : Integer.valueOf(meetingType);
		Integer start = Integer.valueOf(request.getParameter("start"));
		Integer length = Integer.valueOf(request.getParameter("length"));
		boolean editPermitted = AuthenticationManagerUtil.isPermitted("EDIT_WORK_MEETING");
		JSONObject json = new JSONObject();
		json.put("meeting_type", meetingTypeId);
		json.put("start_date", startDate);
		json.put("end_date", endDate);
		json.put("offset", start);
		json.put("limit", length);
		json.put("type", type);
		List<String> users = meetingService.getMeetingList(json);
		Integer totalUser = meetingService.getMeetingListCount(json);
		
		JSONObject response = meetingService.getGeneralDataTable(draw, totalUser, users, editPermitted);
		return new ResponseEntity<>(response.toString(), OK);
	}
	
}
