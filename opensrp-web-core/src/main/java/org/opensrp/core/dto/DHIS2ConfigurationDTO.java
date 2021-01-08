package org.opensrp.core.dto;

public class DHIS2ConfigurationDTO {
	
	private int id;
	
	private String formName;
	
	private String fieldName;
	
	private String dhisId;
	
	private boolean status;
	
	private String value;
	
	private Boolean dynamic;
	
	public int getId() {
		return id;
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
	
	public String getDhisId() {
		return dhisId;
	}
	
	public void setDhisId(String dhisId) {
		this.dhisId = dhisId;
	}
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public void setId(int id) {
		this.id = id;
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
