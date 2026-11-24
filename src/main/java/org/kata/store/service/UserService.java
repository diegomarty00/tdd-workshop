package org.kata.store.service;

import org.kata.store.domain.User;
import org.kata.store.repository.UserRepository;

/*
 * Requisitos (escribe primero el test de cada uno):
 *   - Se puede dar de alta un usuario con id, nombre y apellidos.
 *   - No se puede dar de alta un usuario con un id que ya existe: se lanza una excepción.
 *
 * Pista: recibe el UserRepository por constructor. Así, en el test, puedes pasarle un mock.
 */
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String id, String firstName, String lastName) {
        checkUserIdNotExists(id);
        User user = new User(id, firstName, lastName);
        userRepository.save(user);
        return user;
    }

    private void checkUserIdNotExists(String id) {
        if (userRepository.exists(id)) {
            throw new IllegalArgumentException("User with id " + id + " already exists");
        }
    }
}
