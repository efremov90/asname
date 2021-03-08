package org.asname.integration.kafka.kafkasend;

public enum DestinationsType {

    IN("IN"),
    OUT("OUT");

    private String Description;

    DestinationsType(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }
}
