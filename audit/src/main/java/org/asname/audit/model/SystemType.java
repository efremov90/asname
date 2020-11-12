package org.asname.audit.model;

public enum SystemType {

    ASNAME(-1, "AS Name"),
    ASNAME1(-2, "AS Name 1"),
    ASNAME2(-3, "AS Name 2");

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

    public static SystemType getSystemTypeById(int id) {
        for (SystemType value : SystemType.values()) {
            if (value.getId() == id)
            return value;
        }
        return null;
    }
}
