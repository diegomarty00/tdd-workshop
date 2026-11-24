package org.kata.store.domain;

/*
 * Datos: id, nombre y apellidos.
 *
 * Requisitos (escribe primero el test de cada uno):
 *   - El nombre y los apellidos son obligatorios: ni nulos ni vacíos.
 *   - Si no se cumple, se lanza una excepción con un mensaje claro.
 */
public class User {

    private String id;
    private String firstName;
    private String lastName;

    public User(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = isNullOrEmpty(firstName, "First name is required");
        this.lastName = isNullOrEmpty(lastName, "Last name is required");
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private static String isNullOrEmpty(String text, String message) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return text;
    }
}
