package org.opensrp.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Service;

@Service
@Entity
@Table(name = "dhis2_program_configuration", schema = "core")
public class Dhis2ProgramConfiguration implements Serializable {
	
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dhis2_program_configuration_id_seq")
	@SequenceGenerator(name = "dhis2_program_configuration_id_seq", sequenceName = "dhis2_program_configuration_id_seq", allocationSize = 1)
	private int id;
	
	@Column(name = "program_id")
	private String programId;
	
	@Column(name = "form_name")
	private String formName;
	
	@Column(name = "track_entity_type_id")
	private String trackEntityTypeId;
	
	@Column(name = "status")
	private Boolean status = false;
	
	@Column(name = "program_stage_id")
	private String programStageId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", updatable = false)
	@CreationTimestamp
	private Date created = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE", insertable = true, updatable = true)
	@UpdateTimestamp
	private Date updated = new Date();
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFormName() {
		return formName;
	}
	
	public void setFormName(String formName) {
		this.formName = formName;
	}
	
	public String getProgramId() {
		return programId;
	}
	
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	
	public String getTrackEntityTypeId() {
		return trackEntityTypeId;
	}
	
	public void setTrackEntityTypeId(String trackEntityTypeId) {
		this.trackEntityTypeId = trackEntityTypeId;
	}
	
	public String getProgramStageId() {
		return programStageId;
	}
	
	public void setProgramStageId(String programStageId) {
		this.programStageId = programStageId;
	}
	
	public Boolean getStatus() {
		return status;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
}
