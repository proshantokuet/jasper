package org.opensrp.common.util;

public enum Roles {
    ADMIN(25, "Admin"),
    FO(29, "FO"),
    DPM(26, "DPM"),
    UPM(27, "UPM"),
    APM(28, "APM");

    private Integer id;
    private String name;

    Roles(Integer id, String name) {
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
