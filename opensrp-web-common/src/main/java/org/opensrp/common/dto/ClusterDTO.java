package org.opensrp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class ClusterDTO {

    private int id;

    private String name;

    private String code;

    @JsonProperty("date_created")
    private String created;

    @JsonProperty("date_updated")
    private String updated;

    @JsonProperty("creator_id")
    private Integer creatorId;

    @JsonProperty("division_id")
    private Integer divisionId;

    @JsonProperty("district_id")
    private Integer districtId;

    @JsonProperty("upazila_id")
    private Integer upazilaId;

    @JsonProperty("pourasabha_id")
    private Integer pourasabhaId;

    @JsonProperty("union_ids")
    private Set<Integer> unionIds;

    @JsonProperty("para_center_ids")
    private Set<Integer> paraCenterIds;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getUpazilaId() {
        return upazilaId;
    }

    public void setUpazilaId(Integer upazilaId) {
        this.upazilaId = upazilaId;
    }

    public Integer getPourasabhaId() {
        return pourasabhaId;
    }

    public void setPourasabhaId(Integer pourasabhaId) {
        this.pourasabhaId = pourasabhaId;
    }

    public Set<Integer> getUnionIds() {
        return unionIds;
    }

    public void setUnionIds(Set<Integer> unionIds) {
        this.unionIds = unionIds;
    }

    public Set<Integer> getParaCenterIds() {
        return paraCenterIds;
    }

    public void setParaCenterIds(Set<Integer> paraCenterIds) {
        this.paraCenterIds = paraCenterIds;
    }
}
