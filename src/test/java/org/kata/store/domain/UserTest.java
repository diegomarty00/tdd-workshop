package org.kata.store.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/*
 * TODO: Tests unitarios de User.
 *
 * Ideas de tests:
 *   - shouldCreateUserWithIdNameAndSurname
 *   - shouldRejectEmptyName
 *   - shouldRejectNullSurname
 *
 * Pista: assertThrows(...) te devuelve la excepción, así puedes comprobar también su mensaje.
 */
class UserTest {

    @Test
    void shouldCreateUserWithIdNameAndSurname() {
        // Arrange
        String id = "user-1";
        String firstName = "Ana";
        String lastName = "García";

        // Act
        User user = new User(id, firstName, lastName);

        // Assert
        assertEquals(id, user.getId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
    }

    @Test
    void shouldRejectEmptyName() {
        // Arrange
        String emptyName = "";

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new User("user-1", emptyName, "García"));

        // Assert
        assertEquals("First name is required", exception.getMessage());
    }

    @Test
    void shouldRejectNullSurname() {
        // Arrange
        String nullSurname = null;

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new User("user-1", "Ana", nullSurname));

        // Assert
        assertEquals("Last name is required", exception.getMessage());
    }
}
