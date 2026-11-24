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
 * TODO: Tests unitarios de UserService con Mockito.
 *
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
        User user = userService.register("user-1", "Ana", "García");

        // Assert
        verify(userRepository).save(user);
    }

    @Test
    void shouldRejectUserWithDuplicatedId() {
        // Arrange
        when(userRepository.exists("user-1")).thenReturn(true);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register("user-1", "Ana", "García"));

        // Assert
        assertEquals("User with id user-1 already exists", exception.getMessage());
    }

    @Test
    void shouldNotSaveUserWithDuplicatedId() {
        // Arrange
        when(userRepository.exists("user-1")).thenReturn(true);

        // Act
        assertThrows(IllegalArgumentException.class,
                () -> userService.register("user-1", "Ana", "García"));

        // Assert
        verify(userRepository, never()).save(any());
    }
}
