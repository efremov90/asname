package org.asname.model.reports;

public enum ReportStatusType {

    CREATED("Создан"),
    STARTED("Формируется"),
    ERROR("Ошибка"),
    FINISH("Сформирован");

    private final String Description;

    ReportStatusType(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }
}
