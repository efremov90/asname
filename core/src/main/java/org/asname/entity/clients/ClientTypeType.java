package org.asname.entity.clients;

public enum ClientTypeType {
    DOPOFFICE(0, "ВСП"),
    SELFSERVICE(1, "УС");

    private final int Code;
    private final String Description;

    ClientTypeType(int code, String description) {
        Code = code;
        Description = description;
    }

    public int getCode() {
        return Code;
    }

    public String getDescription() {
        return Description;
    }
}
