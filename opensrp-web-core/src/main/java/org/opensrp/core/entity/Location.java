/**
 * @author proshanto (proshanto123@gmail.com)
 */

package org.opensrp.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Service;

@Service
@Entity
@Table(name = "location", schema = "core")
public class Location implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_id_seq")
	@SequenceGenerator(name = "location_id_seq", sequenceName = "location_id_seq", allocationSize = 1)
	private int id;
	
	@NotEmpty(message = "location name can't be empty")
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "code")
	private String code;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", updatable = false)
	@CreationTimestamp
	private Date created = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE", insertable = true, updatable = true)
	@UpdateTimestamp
	private Date updated = new Date();
	
	@ManyToOne()
	@JoinColumn(name = "creator", referencedColumnName = "id")
	private User creator;
	
	@ManyToOne()
	@JoinColumn(name = "parent_location_id", referencedColumnName = "id")
	private Location parentLocation;

	@ManyToOne()
	@JoinColumn(name = "division_id", referencedColumnName = "id")
	private Location division;

	@ManyToOne()
	@JoinColumn(name = "district_id", referencedColumnName = "id")
	private Location district;

	@ManyToOne()
	@JoinColumn(name = "upazila_id", referencedColumnName = "id")
	private Location upazila;

	@ManyToOne()
	@JoinColumn(name = "pourasabha_id", referencedColumnName = "id")
	private Location pourasabha;

	@ManyToOne()
	@JoinColumn(name = "union_id", referencedColumnName = "id")
	private Location union;

	@ManyToOne()
	@JoinColumn(name = "para_id", referencedColumnName = "id")
	private Location para;

	@ManyToOne()
	@JoinColumn(name = "location_tag_id", referencedColumnName = "id")
	private LocationTag locationTag;
	
	@Column(name = "is_login_location", columnDefinition = "boolean default false", nullable = false)
	private boolean loginLocation;
	
	@Column(name = "is_visit_location", columnDefinition = "boolean default false", nullable = false)
	private boolean visitLocation;
	
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
	
	public User getCreator() {
		return creator;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public Location getParentLocation() {
		return parentLocation;
	}
	
	public void setParentLocation(Location parentLocation) {
		this.parentLocation = parentLocation;
	}
	
	public LocationTag getLocationTag() {
		return locationTag;
	}
	
	public void setLocationTag(LocationTag locationTag) {
		this.locationTag = locationTag;
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public boolean isLoginLocation() {
		return loginLocation;
	}
	
	public void setLoginLocation(boolean loginLocation) {
		this.loginLocation = loginLocation;
	}
	
	public boolean isVisitLocation() {
		return visitLocation;
	}
	
	public void setVisitLocation(boolean visitLocation) {
		this.visitLocation = visitLocation;
	}

	public Location getDivision() {
		return division;
	}

	public void setDivision(Location division) {
		this.division = division;
	}

	public Location getDistrict() {
		return district;
	}

	public void setDistrict(Location district) {
		this.district = district;
	}

	public Location getUpazila() {
		return upazila;
	}

	public void setUpazila(Location upazila) {
		this.upazila = upazila;
	}

	public Location getPourasabha() {
		return pourasabha;
	}

	public void setPourasabha(Location pourasabha) {
		this.pourasabha = pourasabha;
	}

	public Location getUnion() {
		return union;
	}

	public void setUnion(Location union) {
		this.union = union;
	}

	public Location getPara() {
		return para;
	}

	public void setPara(Location para) {
		this.para = para;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", name=" + name + ", description=" + description + ", uuid=" + uuid + ", code="
		        + code + ", created=" + created + ", updated=" + updated + ", creator=" + creator + ", parentLocation="
		        + parentLocation + ", locationTag=" + locationTag + ", loginLocation=" + loginLocation + "]";
	}

}
