package org.opensrp.core.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Service
@Entity
@Table(name = "para_center", schema = "core")
public class ParaCenter implements Serializable {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "para_center_id_seq")
    @SequenceGenerator(name = "para_center_id_seq", sequenceName = "para_center_id_seq", allocationSize = 1)
    private int id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "dhis2_id")
    private String dhis2Id;

    @Column(name = "established")
    private String established;

    @Column(name = "para_kormi_name")
    private String paraKormiName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE", updatable = false)
    @CreationTimestamp
    private Date created = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE", insertable = true, updatable = true)
    @UpdateTimestamp
    private Date updated = new Date();

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private User creator;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "division_id", referencedColumnName = "id")
    private Location division;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "district_id", referencedColumnName = "id")
    private Location district;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "upazila_id", referencedColumnName = "id")
    private Location upazila;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "pourasabha_id", referencedColumnName = "id")
    private Location pourasabha;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "union_id", referencedColumnName = "id")
    private Location union;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "para_id", referencedColumnName = "id")
    private Location para;

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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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
        return "ParaCenter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", dhis2Id='" + dhis2Id + '\'' +
                ", established='" + established + '\'' +
                ", paraKormiName='" + paraKormiName + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", creator=" + creator +
                ", division=" + division +
                ", district=" + district +
                ", upazila=" + upazila +
                ", pourasabha=" + pourasabha +
                ", union=" + union +
                ", para=" + para +
                '}';
    }
}
