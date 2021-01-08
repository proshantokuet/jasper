package org.opensrp.core.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.common.dto.MeetingCommonDTO;
import org.opensrp.common.util.ImageUpload;
import org.opensrp.core.entity.Meeting;
import org.opensrp.core.entity.MeetingDocument;
import org.opensrp.core.service.mapper.MeetingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class MeetingService extends CommonService {
	
	private static final Logger logger = Logger.getLogger(MeetingService.class);
	
	@Autowired
	private MeetingMapper meetingMapper;
	
	@Autowired
	private ImageUpload imageUpload;
	
	private static final String UPLOADED_FOLDER = "/opt/multimedia/document/";
	
	@Transactional
	public Meeting saveOrUpdate(MultipartHttpServletRequest request) throws Exception {
		
		Integer id = Integer.parseInt(request.getParameter("id"));
		
		Meeting meeting = findById(id, "id", Meeting.class);
		if (meeting == null) {
			meeting = new Meeting();
		}
		
		meeting = meetingMapper.map(request, meeting);
		
		List<MultipartFile> pictures = request.getFiles("picture");
		
		Set<MeetingDocument> meetingDocuments = new HashSet<MeetingDocument>();
		for (MultipartFile multipartFile : pictures) {
			String originalName = multipartFile.getOriginalFilename();
			if (!StringUtils.isBlank(originalName)) {
				MeetingDocument meetingDocument = new MeetingDocument();
				
				String fileName = imageUpload.upload(multipartFile, UPLOADED_FOLDER, false, 500);
				
				meetingDocument.setFileName(fileName);
				meetingDocument.setOriginalName(originalName);
				meetingDocument.setMeeting(meeting);
				meetingDocuments.add(meetingDocument);
			}
		}
		
		meeting.setMeetingDocuments(meetingDocuments);
		return saveOrUpdate(meeting);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<MeetingCommonDTO> getMeetingList(String type) {
		
		Session session = getSessionFactory();
		List<MeetingCommonDTO> dtos = new ArrayList<>();
		
		String hql = "select m.id,m.description,m.files,m.meeting_date meetingDate,m.meeting_place_other meetingPlaceOther,m.name,"
		        + " m.total_participants participants,m.type,mt.name place,mt1.name meetingOrWorkPlane"
		        + " from core.meeting as m left join core.meeting_type mt on "
		        + " m.meeting_place_options = mt.id join core.meeting_type mt1 "
		        + " on m.meeting_or_work_plane_type = mt1.id where m.type=:type order by m.meeting_date desc";
		Query query = session.createSQLQuery(hql).addScalar("id", StandardBasicTypes.INTEGER)
		        .addScalar("description", StandardBasicTypes.STRING).addScalar("files", StandardBasicTypes.STRING)
		        .addScalar("meetingDate", StandardBasicTypes.STRING)
		        .addScalar("meetingPlaceOther", StandardBasicTypes.STRING).addScalar("name", StandardBasicTypes.STRING)
		        .addScalar("participants", StandardBasicTypes.INTEGER).addScalar("type", StandardBasicTypes.STRING)
		        .addScalar("place", StandardBasicTypes.STRING).addScalar("meetingOrWorkPlane", StandardBasicTypes.STRING)
		        .setString("type", type).setResultTransformer(new AliasToBeanResultTransformer(MeetingCommonDTO.class));
		dtos = query.list();
		
		return dtos;
	}
	
	@Transactional
	public MeetingCommonDTO getMeetingById(int id) {
		
		Session session = getSessionFactory();
		MeetingCommonDTO dtos = new MeetingCommonDTO();
		
		String hql = "select m.id,m.description,m.files,m.meeting_date meetingDate,m.meeting_place_other meetingPlaceOther,m.name,"
		        + " m.total_participants participants,m.type,mt.name place,mt1.name meetingOrWorkPlane"
		        + " from core.meeting as m left join core.meeting_type mt on "
		        + " m.meeting_place_options = mt.id join core.meeting_type mt1 "
		        + " on m.meeting_or_work_plane_type = mt1.id where m.id=:id order by m.meeting_date desc";
		Query query = session.createSQLQuery(hql).addScalar("id", StandardBasicTypes.INTEGER)
		        .addScalar("description", StandardBasicTypes.STRING).addScalar("files", StandardBasicTypes.STRING)
		        .addScalar("meetingDate", StandardBasicTypes.STRING)
		        .addScalar("meetingPlaceOther", StandardBasicTypes.STRING).addScalar("name", StandardBasicTypes.STRING)
		        .addScalar("participants", StandardBasicTypes.INTEGER).addScalar("type", StandardBasicTypes.STRING)
		        .addScalar("place", StandardBasicTypes.STRING).addScalar("meetingOrWorkPlane", StandardBasicTypes.STRING)
		        .setInteger("id", id).setResultTransformer(new AliasToBeanResultTransformer(MeetingCommonDTO.class));
		dtos = (MeetingCommonDTO) query.uniqueResult();
		
		return dtos;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> getMeetingList(JSONObject json) {
		Session session = getSessionFactory();
		List<String> list = new ArrayList<String>();
		String hql = "select * from core.get_meeting_lists('" + json + "')";
		
		Query query = session.createSQLQuery(hql);
		list = query.list();
		return list;
	}
	
	@Transactional
	public int getMeetingListCount(JSONObject json) {
		Session session = getSessionFactory();
		BigInteger total = null;
		String hql = "select * from core.get_meeting_lists_count('" + json + "')";
		Query query = session.createSQLQuery(hql);
		total = (BigInteger) query.uniqueResult();
		
		return total.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<MeetingCommonDTO> getMeetingListForExport(JSONObject json) {
		Session session = getSessionFactory();
		List<MeetingCommonDTO> list = new ArrayList<MeetingCommonDTO>();
		String hql = "select description,meeting_date meetingDate,meeting_work_plane_type meetingOrWorkPlane, "
		        + " meeting_place place,meeting_place_other meetingPlaceOther,total_participants participants "
		        + " ,total_doc totalDoc from core.get_meeting_list_for_export('" + json + "')";
		
		Query query = session.createSQLQuery(hql).addScalar("description", StandardBasicTypes.STRING)
		        .addScalar("meetingDate", StandardBasicTypes.STRING)
		        .addScalar("meetingOrWorkPlane", StandardBasicTypes.STRING).addScalar("place", StandardBasicTypes.STRING)
		        .addScalar("meetingPlaceOther", StandardBasicTypes.STRING)
		        .addScalar("participants", StandardBasicTypes.INTEGER).addScalar("totalDoc", StandardBasicTypes.INTEGER)
		        .setResultTransformer(new AliasToBeanResultTransformer(MeetingCommonDTO.class));
		;
		list = query.list();
		return list;
	}
	
	public JSONObject getDataTable(Integer draw, Integer totalUser, List<String> users, boolean editPermitted)
	    throws JSONException {
		JSONObject response = new JSONObject();
		response.put("draw", draw + 1);
		response.put("recordsTotal", totalUser);
		response.put("recordsFiltered", totalUser);
		JSONArray array = new JSONArray();
		for (String user : users) {
			JSONObject json = new JSONObject(user);
			
			JSONArray person = new JSONArray();
			person.put(json.opt("meetingorworkplane"));
			person.put(json.opt("meetingdate"));
			person.put((json.opt("place")));
			person.put(json.opt("meetingplaceother"));
			person.put(json.opt("participants"));
			person.put(json.opt("count"));
			String edit = editPermitted ? "<a href='/opensrp-dashboard/workplan-meeting/" + json.opt("id")
			        + "/edit.html?id=" + json.opt("id") + "&lang=en'>Edit</a>" : "";
			edit += " | ";
			edit += editPermitted ? "<a href='/opensrp-dashboard/workplan-meeting/" + json.opt("id") + "/view.html?id="
			        + json.opt("id") + "&lang=en'>View</a>" : "";
			String actions = edit; //buttons
			person.put(actions);
			array.put(person);
		}
		response.put("data", array);
		return response;
	}
	
	public JSONObject getGeneralDataTable(Integer draw, Integer totalUser, List<String> users, boolean editPermitted)
	    throws JSONException {
		JSONObject response = new JSONObject();
		response.put("draw", draw + 1);
		response.put("recordsTotal", totalUser);
		response.put("recordsFiltered", totalUser);
		JSONArray array = new JSONArray();
		for (String user : users) {
			JSONObject json = new JSONObject(user);
			
			JSONArray person = new JSONArray();
			person.put(json.opt("meetingorworkplane"));
			person.put(json.opt("meetingdate"));
			person.put((json.opt("place")));
			person.put(json.opt("meetingplaceother"));
			person.put(json.opt("participants"));
			person.put(json.opt("count"));
			String edit = editPermitted ? "<a href='/opensrp-dashboard/other-meeting-minutes/" + json.opt("id")
			        + "/edit.html?id=" + json.opt("id") + "&lang=en'>Edit</a>" : "";
			edit += " | ";
			edit += editPermitted ? "<a href='/opensrp-dashboard/other-meeting-minutes/" + json.opt("id") + "/view.html?id="
			        + json.opt("id") + "&lang=en'>View</a>" : "";
			String actions = edit; //buttons
			person.put(actions);
			array.put(person);
		}
		response.put("data", array);
		return response;
	}
	
	@Transactional
	public Integer deleteDoc(Long id) {
		Session session = getSessionFactory();
		String hql = "delete from core.meeting_doc where id=:id ";
		Integer res = session.createSQLQuery(hql).setLong("id", id).executeUpdate();
		return res;
	}
}
