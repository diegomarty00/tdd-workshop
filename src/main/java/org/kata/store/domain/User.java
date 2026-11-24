package org.kata.store.domain;

/*
 * TODO: Usuario de la tienda.
 *
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
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
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
}
