package org.opensrp.common.dto;

public class MeetingCommonDTO {
	
	private int id;
	
	private String name;
	
	private String type;
	
	private String description;
	
	private String files;
	
	private String meetingDate;
	
	private String meetingPlaceOther;
	
	private int participants;
	
	private String place;
	
	private String meetingOrWorkPlane;
	
	private int totalDoc;
	
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getFiles() {
		return files;
	}
	
	public void setFiles(String files) {
		this.files = files;
	}
	
	public String getMeetingDate() {
		return meetingDate;
	}
	
	public void setMeetingDate(String meetingDate) {
		this.meetingDate = meetingDate;
	}
	
	public String getMeetingPlaceOther() {
		return meetingPlaceOther;
	}
	
	public void setMeetingPlaceOther(String meetingPlaceOther) {
		this.meetingPlaceOther = meetingPlaceOther;
	}
	
	public int getParticipants() {
		return participants;
	}
	
	public void setParticipants(int participants) {
		this.participants = participants;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	public String getMeetingOrWorkPlane() {
		return meetingOrWorkPlane;
	}
	
	public void setMeetingOrWorkPlane(String meetingOrWorkPlane) {
		this.meetingOrWorkPlane = meetingOrWorkPlane;
	}
	
	public int getTotalDoc() {
		return totalDoc;
	}
	
	public void setTotalDoc(int totalDoc) {
		this.totalDoc = totalDoc;
	}
	
}
