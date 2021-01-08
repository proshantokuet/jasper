package org.opensrp.core.entity;

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
@Table(name = "dhis_configuration", schema = "core")
public class DhisConfiguration {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dhis_configuration_id_seq")
	@SequenceGenerator(name = "dhis_configuration_id_seq", sequenceName = "dhis_configuration_id_seq", allocationSize = 1)
	private int id;
	
	@Column(name = "form_name")
	private String formName;
	
	@Column(name = "field_name")
	private String fieldName;
	
	@Column(name = "status")
	private Boolean status = false;
	
	@Column(name = "dynamic")
	private Boolean dynamic = false;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "dhis_id")
	private String dhisId;
	
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
	
	public String getFieldName() {
		return fieldName;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public Boolean getStatus() {
		return status;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public String getDhisId() {
		return dhisId;
	}
	
	public void setDhisId(String dhisId) {
		this.dhisId = dhisId;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Boolean getDynamic() {
		return dynamic;
	}
	
	public void setDynamic(Boolean dynamic) {
		this.dynamic = dynamic;
	}
	
}
