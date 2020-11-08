package org.asname.audit.model;

public enum SystemType {
    ASNAME(-1, "ASNAME"),
    ASNAME1(-2, "ASNAME 1"),
    ASNAME2(-3, "ASNAME 2");

    private final int Id;
    private final String Description;

    SystemType(int id, String description) {
        Id = id;
        Description = description;
    }

    public int getId() {
        return Id;
    }

    public String getDescription() {
        return Description;
    }
}
