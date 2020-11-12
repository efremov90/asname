package org.asname.model.requests;

public enum RequestStatusType {

    ERROR("Ошибка"),
    CREATED("Создана"),
    CANCELED("Отменена"),
    CLOSE("Закрыта");

    private final String Description;

    RequestStatusType(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }
}
