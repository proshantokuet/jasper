package org.opensrp.common.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserDTO {
	
	private Integer id;
	
	@JsonProperty("first_name")
	private String firstName;
	
	@JsonProperty("last_name")
	private String lastName;
	
	private String email;
	
	private String mobile;
	
	private String idetifier;
	
	private String username;
	
	private String password;
	
	private String roles;
	
	private int parentUser;
	
	private int team;
	
	private boolean teamMember;
	
	private boolean status;
	
	private String appVersion;
	
	private Integer parentUserId;
	
	@JsonProperty("ethnic_community")
	private String ethnicCommunity;
	
	@JsonProperty("academic_qualification")
	private String academicQualification;
	
	@JsonProperty("permanent_address")
	private String permanentAddress;
	
	@JsonProperty("br_id")
	private String brId;
	
	@JsonProperty("national_id")
	private String nationalId;
	
	@JsonProperty("joining_date")
	private Date joiningDate;
	
	@JsonProperty("birth_date")
	private Date birthDate;
	
	@JsonProperty("districts")
	private List<Integer> districts;
	
	@JsonProperty("upazilas")
	private List<Integer> upazilas;
	
	@JsonProperty("pourasabhas")
	private List<Integer> pourasabhas;
	
	@JsonProperty("unions")
	private List<Integer> unions;
	
	@JsonProperty("clusters")
	private Integer clusters;
	
	private String gender;
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public boolean isTeamMember() {
		return teamMember;
	}
	
	public void setTeamMember(boolean teamMember) {
		this.teamMember = teamMember;
	}
	
	public int getTeam() {
		return team;
	}
	
	public void setTeam(int team) {
		this.team = team;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName.trim();
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMobile() {
		return mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getIdetifier() {
		return idetifier;
	}
	
	public void setIdetifier(String idetifier) {
		this.idetifier = idetifier;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRoles() {
		return roles;
	}
	
	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public int getParentUser() {
		return parentUser;
	}
	
	public void setParentUser(int parentUser) {
		this.parentUser = parentUser;
	}
	
	public String getFullName() {
		return this.firstName + " " + (this.lastName.equalsIgnoreCase(".") ? "" : this.lastName);
	}
	
	public String getAppVersion() {
		return appVersion;
	}
	
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	public Integer getParentUserId() {
		return parentUserId;
	}
	
	public void setParentUserId(Integer parentUserId) {
		this.parentUserId = parentUserId;
	}
	
	public String getEthnicCommunity() {
		return ethnicCommunity;
	}
	
	public void setEthnicCommunity(String ethnicCommunity) {
		this.ethnicCommunity = ethnicCommunity;
	}
	
	public String getAcademicQualification() {
		return academicQualification;
	}
	
	public void setAcademicQualification(String academicQualification) {
		this.academicQualification = academicQualification;
	}
	
	public String getPermanentAddress() {
		return permanentAddress;
	}
	
	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}
	
	public String getBrId() {
		return brId;
	}
	
	public void setBrId(String brId) {
		this.brId = brId;
	}
	
	public String getNationalId() {
		return nationalId;
	}
	
	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}
	
	public Date getJoiningDate() {
		return joiningDate;
	}
	
	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}
	
	public List<Integer> getDistricts() {
		return districts;
	}
	
	public void setDistricts(List<Integer> districts) {
		this.districts = districts;
	}
	
	public List<Integer> getUpazilas() {
		return upazilas;
	}
	
	public void setUpazilas(List<Integer> upazilas) {
		this.upazilas = upazilas;
	}
	
	public List<Integer> getPourasabhas() {
		return pourasabhas;
	}
	
	public void setPourasabhas(List<Integer> pourasabhas) {
		this.pourasabhas = pourasabhas;
	}
	
	public List<Integer> getUnions() {
		return unions;
	}
	
	public void setUnions(List<Integer> unions) {
		this.unions = unions;
	}
	
	public Integer getClusters() {
		return clusters;
	}
	
	public void setClusters(Integer clusters) {
		this.clusters = clusters;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
}
