package org.asname.model.tasks;

public enum TaskStatusType {

    CREATED("Создано"),
    STARTED("Начато"),
    ERROR("Ошибка"),
    FINISH("Завершено");

    private final String Description;

    TaskStatusType(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }
}
