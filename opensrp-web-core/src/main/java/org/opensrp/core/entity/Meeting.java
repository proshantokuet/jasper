/**
 * @author proshanto (proshanto123@gmail.com)
 */
package org.opensrp.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Service;

@Service
@Entity
@Table(name = "meeting", schema = "core")
public class Meeting implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meeting_id_seq")
	@SequenceGenerator(name = "meeting_id_seq", sequenceName = "meeting_id_seq", allocationSize = 1)
	private int id;
	
	private String name;
	
	private long timestamp;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "dhis_id")
	private String dhisId;
	
	private String type;
	
	@Column(name = "meeting_or_work_plane_type")
	private int meetingOrWorkPlaneType;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "meeting_date")
	@Temporal(TemporalType.DATE)
	private Date meetingDate;
	
	@Column(name = "meeting_place_options")
	private int meetingPlaceOptions;
	
	@Column(name = "meeting_place_other")
	private String meetingPlaceOther;
	
	@Column(name = "total_participants")
	private int totalParticipants;
	
	@Column(name = "files")
	private String files;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", updatable = false)
	@CreationTimestamp
	private Date created = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE", insertable = true, updatable = true)
	@UpdateTimestamp
	private Date updated = new Date();
	
	@Column(name = "creator")
	private int creator;
	
	@OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<MeetingDocument> meetingDocuments = new HashSet<MeetingDocument>();
	
	private boolean status;
	
	public int getId() {
		return id;
		
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated() {
		this.created = new Date();
	}
	
	public Date getUpdated() {
		return updated;
	}
	
	public void setUpdated() {
		this.updated = new Date();
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
	
	public int getCreator() {
		return creator;
	}
	
	public void setCreator(int creator) {
		this.creator = creator;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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
	
	public String getDhisId() {
		return dhisId;
	}
	
	public void setDhisId(String dhisId) {
		this.dhisId = dhisId;
	}
	
	public Set<MeetingDocument> getMeetingDocuments() {
		return meetingDocuments;
	}
	
	public void setMeetingDocuments(Set<MeetingDocument> meetingDocuments) {
		this.meetingDocuments = meetingDocuments;
	}
	
	@Override
	public String toString() {
		return "Meeting [id=" + id + ", name=" + name + ", timestamp=" + timestamp + ", uuid=" + uuid + ", dhisId=" + dhisId
		        + ", type=" + type + ", meetingOrWorkPlaneType=" + meetingOrWorkPlaneType + ", description=" + description
		        + ", meetingDate=" + meetingDate + ", meetingPlaceOptions=" + meetingPlaceOptions + ", meetingPlaceOther="
		        + meetingPlaceOther + ", totalParticipants=" + totalParticipants + ", files=" + files + ", created="
		        + created + ", updated=" + updated + ", creator=" + creator + ", status=" + status + "]";
	}
	
}
