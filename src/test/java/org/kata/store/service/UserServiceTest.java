package org.kata.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kata.store.domain.User;
import org.kata.store.repository.UserRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/*
 * El UserRepository es una dependencia: aquí se sustituye por un mock, nunca por la implementación real.
 *
 * Ideas de tests:
 *   - shouldSaveNewUser                      -> verify(userRepository).save(...)
 *   - shouldRejectUserWithDuplicatedId       -> stub: when(...).thenReturn(true) + assertThrows
 *   - shouldNotSaveUserWithDuplicatedId      -> verify(userRepository, never()).save(any())
 *
 * Pista: puedes crear los mocks con mock(UserRepository.class) o con
 * @ExtendWith(MockitoExtension.class) y @Mock.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String USER_ID = "user-1";
    private static final String FIRST_NAME = "Ana";
    private static final String LAST_NAME = "García";

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void shouldSaveNewUser() {
        // Act
        User user = userService.register(USER_ID, FIRST_NAME, LAST_NAME);

        // Assert
        verify(userRepository).save(user);
    }

    @Test
    void shouldRejectUserWithDuplicatedId() {
        // Arrange
        when(userRepository.exists(USER_ID)).thenReturn(true);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(USER_ID, FIRST_NAME, LAST_NAME));

        // Assert
        assertEquals("User with id " + USER_ID + " already exists", exception.getMessage());
    }

    @Test
    void shouldNotSaveUserWithDuplicatedId() {
        // Arrange
        when(userRepository.exists(USER_ID)).thenReturn(true);

        // Act
        assertThrows(IllegalArgumentException.class,
                () -> userService.register(USER_ID, FIRST_NAME, LAST_NAME));

        // Assert
        verify(userRepository, never()).save(any());
    }
}
