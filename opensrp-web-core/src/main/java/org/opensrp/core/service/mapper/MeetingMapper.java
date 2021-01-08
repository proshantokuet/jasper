package org.opensrp.core.service.mapper;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import org.opensrp.common.util.DateUtil;
import org.opensrp.core.entity.Meeting;
import org.opensrp.core.entity.User;
import org.opensrp.core.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class MeetingMapper {
	
	@Autowired
	private MeetingService meetingService;
	
	@Value("#{opensrp['dhis2.id.meeting']}")
	private String DHIS2ID;
	
	public Meeting map(MultipartHttpServletRequest request, Meeting meeting) throws ParseException {
		Integer totalParticipants = Integer.parseInt(request.getParameter("totalParticipants"));
		Integer meetingOrWorkPlaneType = Integer.parseInt(request.getParameter("meetingOrWorkPlaneType"));
		Date meetingDate = DateUtil.parseDate(request.getParameter("meetingDate"));
		int meetingPlaceOptions = Integer.parseInt(request.getParameter("meetingPlaceOptions"));
		String meetingPlaceOther = request.getParameter("meetingPlaceOther");
		String description = request.getParameter("description");
		meeting.setMeetingDate(meetingDate);
		
		meeting.setDhisId(DHIS2ID);
		meeting.setMeetingPlaceOptions(meetingPlaceOptions);
		meeting.setMeetingPlaceOther(meetingPlaceOther);
		meeting.setStatus(true);
		meeting.setName("");
		meeting.setTotalParticipants(totalParticipants);
		meeting.setType(request.getParameter("type"));
		meeting.setTimestamp(System.currentTimeMillis());
		meeting.setMeetingOrWorkPlaneType(meetingOrWorkPlaneType);
		meeting.setDescription(description);
		if (meeting.getId() == 0) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = (User) auth.getPrincipal();
			meeting.setCreator(user.getId());
			meeting.setUuid(UUID.randomUUID().toString());
			
		}
		return meeting;
	}
	
}
