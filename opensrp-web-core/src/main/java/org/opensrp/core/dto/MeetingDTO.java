package org.opensrp.core.dto;

import java.util.Date;

public class MeetingDTO {
	
	private int id;
	
	private String name;
	
	private String type;
	
	private Date meetingDate;
	
	private int meetingPlaceOptions;
	
	private String meetingPlaceOther;
	
	private int totalParticipants;
	
	private String files;
	
	private int meetingOrWorkPlaneType;
	
	private String description;
	
	private boolean status;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Date getMeetingDate() {
		return meetingDate;
	}
	
	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}
	
	public int getMeetingPlaceOptions() {
		return meetingPlaceOptions;
	}
	
	public void setMeetingPlaceOptions(int meetingPlaceOptions) {
		this.meetingPlaceOptions = meetingPlaceOptions;
	}
	
	public String getMeetingPlaceOther() {
		return meetingPlaceOther;
	}
	
	public void setMeetingPlaceOther(String meetingPlaceOther) {
		this.meetingPlaceOther = meetingPlaceOther;
	}
	
	public int getTotalParticipants() {
		return totalParticipants;
	}
	
	public void setTotalParticipants(int totalParticipants) {
		this.totalParticipants = totalParticipants;
	}
	
	public String getFiles() {
		return files;
	}
	
	public void setFiles(String files) {
		this.files = files;
	}
	
	public int getMeetingOrWorkPlaneType() {
		return meetingOrWorkPlaneType;
	}
	
	public void setMeetingOrWorkPlaneType(int meetingOrWorkPlaneType) {
		this.meetingOrWorkPlaneType = meetingOrWorkPlaneType;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
