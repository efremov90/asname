package org.asname.model.security;

public enum SecurityContextStatusCodeType {
    S00000("Успешная авторизация."),
    S00001("Неверный логин или пароль."),
    S99999("Неизвестная ошибка.");

    private final String Description;

    SecurityContextStatusCodeType(String description) {
        Description = description;
    }
}
