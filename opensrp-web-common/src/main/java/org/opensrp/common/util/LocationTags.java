package org.opensrp.common.util;

public enum LocationTags {
    COUNTRY(9, "Country"),
    DIVISION(10, "Division"),
    DISTRICT(11, "District"),
    UPAZILA_CITY_CORPORATION(12, "City Corporation Upazila"),
    POURASABHA(13, "Pourasabha"),
    UNION_WARD(14, "Union Ward"),
    PARA(15, "Para"),
    BANGLADESH(16, "BANGLADESH");

    private Integer id;
    private String name;

    LocationTags(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
