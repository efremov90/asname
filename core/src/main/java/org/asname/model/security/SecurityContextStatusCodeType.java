package org.asname.model.security;

public enum SecurityContextStatusCodeType {
    S00000("Успешная авторизация."),
    S00001("Неверный логин или пароль."),
    S00002("Сессия истекла."),
    S00003("Отсутствует Authorization заголовок."),
    S99999("Ошибка аутентификации.");

    private final String Description;

    SecurityContextStatusCodeType(String description) {
        Description = description;
    }
}
