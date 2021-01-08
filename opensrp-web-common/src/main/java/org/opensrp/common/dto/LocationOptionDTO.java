package org.opensrp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class LocationOptionDTO {

    public LocationOptionDTO(Integer id, String name, String selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }

    Integer id;

    String name;

    String selected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
