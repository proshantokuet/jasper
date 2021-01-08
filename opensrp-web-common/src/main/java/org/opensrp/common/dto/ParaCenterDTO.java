package org.opensrp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class ParaCenterDTO {

    private int id;

    private String name;

    private String code;

    @JsonProperty("dhis2_id")
    private String dhis2Id;

    private String established;

    @JsonProperty("para_kormi_name")
    private String paraKormiName;

    @JsonProperty("date_created")
    private String created;

    @JsonProperty("date_updated")
    private String updated;

    @JsonProperty("creator")
    private Integer creatorId;

    @JsonProperty("division_id")
    private Integer divisionId;

    @JsonProperty("district_id")
    private Integer districtId;

    @JsonProperty("upazila_id")
    private Integer upazilaId;

    @JsonProperty("pourasabha_id")
    private Integer pourasabhaId;

    @JsonProperty("union_id")
    private Integer unionId;

    @JsonProperty("para_id")
    private Integer paraId;

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

    public Integer getUnionId() {
        return unionId;
    }

    public void setUnionId(Integer unionId) {
        this.unionId = unionId;
    }

    public Integer getParaId() {
        return paraId;
    }

    public void setParaId(Integer paraId) {
        this.paraId = paraId;
    }

    public String getEstablished() {
        return established;
    }

    public void setEstablished(String established) {
        this.established = established;
    }

    public String getParaKormiName() {
        return paraKormiName;
    }

    public void setParaKormiName(String paraKormiName) {
        this.paraKormiName = paraKormiName;
    }

    public String getDhis2Id() {
        return dhis2Id;
    }

    public void setDhis2Id(String dhis2Id) {
        this.dhis2Id = dhis2Id;
    }

    @Override
    public String toString() {
        return "ParaCenterDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", dhis2Id='" + dhis2Id + '\'' +
                ", established='" + established + '\'' +
                ", paraKormiName='" + paraKormiName + '\'' +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", creatorId=" + creatorId +
                ", divisionId=" + divisionId +
                ", districtId=" + districtId +
                ", upazilaId=" + upazilaId +
                ", pourasabhaId=" + pourasabhaId +
                ", unionId=" + unionId +
                ", paraId=" + paraId +
                '}';
    }
}
