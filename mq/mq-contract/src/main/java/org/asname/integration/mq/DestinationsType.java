package org.asname.integration.mq;

public enum DestinationsType {

    AS1_IN("AS1.IN"),
    AS1_OUT("AS1.OUT"),
    AS1_REQUEST("AS1.REQUEST"),
    AS1_RESPONSE("AS1.RESPONSE");

    private String Description;

    DestinationsType(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }
}
