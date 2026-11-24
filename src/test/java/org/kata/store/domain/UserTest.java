package org.kata.store.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
