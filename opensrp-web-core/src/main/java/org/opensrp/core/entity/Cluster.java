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
@Table(name = "cluster", schema = "core")
public class Cluster implements Serializable {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cluster_id_seq")
    @SequenceGenerator(name = "cluster_id_seq", sequenceName = "cluster_id_seq", allocationSize = 1)
    private int id;

    @NotNull
    @Column(name = "name")
    private String name;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cluster_pourasabha", schema = "core", joinColumns = { @JoinColumn(name = "cluster_id") }, inverseJoinColumns = { @JoinColumn(name = "pourasabha_id") })
    private Set<Location> pourasabhas = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cluster_union", schema = "core", joinColumns = { @JoinColumn(name = "cluster_id") }, inverseJoinColumns = { @JoinColumn(name = "union_id") })
    private Set<Location> unions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cluster_para_center", schema = "core", joinColumns = { @JoinColumn(name = "cluster_id") }, inverseJoinColumns = { @JoinColumn(name = "para_center_id") })
    private Set<ParaCenter> paraCenters = new HashSet<>();

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

    public Set<Location> getPourasabhas() {
        return pourasabhas;
    }

    public void setPourasabhas(Set<Location> pourasabhas) {
        this.pourasabhas = pourasabhas;
    }

    public Set<Location> getUnions() {
        return unions;
    }

    public void setUnions(Set<Location> unions) {
        this.unions = unions;
    }

    public Set<ParaCenter> getParaCenters() {
        return paraCenters;
    }

    public void setParaCenters(Set<ParaCenter> paraCenters) {
        this.paraCenters = paraCenters;
    }
}
