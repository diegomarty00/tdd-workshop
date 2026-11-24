package org.kata.store.service;

import static org.mockito.Mockito.verify;

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
}
