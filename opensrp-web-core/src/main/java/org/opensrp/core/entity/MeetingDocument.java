/**
 * @author proshanto (proshanto123@gmail.com)
 */
package org.opensrp.core.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.stereotype.Service;

@Service
@Entity
@Table(name = "meeting_doc", schema = "core")
public class MeetingDocument implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meeting_doc_id_seq")
	@SequenceGenerator(name = "meeting_doc_id_seq", sequenceName = "meeting_doc_id_seq", allocationSize = 1)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "meeting_id", referencedColumnName = "id")
	private Meeting meeting;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "original_name")
	private String originalName;
	
	private String type;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Meeting getMeeting() {
		return meeting;
	}
	
	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getOriginalName() {
		return originalName;
	}
	
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "MeetingDocument [id=" + id + ", meeting=" + meeting + ", fileName=" + fileName + ", originalName="
		        + originalName + ", type=" + type + "]";
	}
	
}
