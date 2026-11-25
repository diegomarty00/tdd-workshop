package org.kata.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.store.domain.Order;
import org.kata.store.domain.OrderStatus;
import org.kata.store.domain.User;
import org.kata.store.repository.OrderRepository;
import org.kata.store.repository.UserRepository;
import org.kata.store.repository.postgres.PostgresUserRepository;
import org.kata.store.service.OrderService;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers(disabledWithoutDocker = true)
class PostgresRepositoriesIT {

    private static final String ORDER_ID = "order-1";
    private static final String USER_ID = "user-1";
    private static final String FIRST_NAME = "Ana";
    private static final String LAST_NAME = "García";
    private static final double PRICE = 50.0;

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16");

    private UserRepository userRepository;
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() throws SQLException {
        DataSource dataSource = dataSource();
        userRepository = new PostgresUserRepository(dataSource);
        cleanTables(dataSource);
    }

    @Test
    void shouldSaveAndFindUser() {
        // Arrange
        User user = new User(USER_ID, FIRST_NAME, LAST_NAME);

        // Act
        userRepository.save(user);
        User found = userRepository.findById(USER_ID);

        // Assert
        assertEquals(USER_ID, found.getId());
        assertEquals(FIRST_NAME, found.getFirstName());
        assertEquals(LAST_NAME, found.getLastName());
    }

    @Test
    void shouldTellIfUserExists() {
        // Arrange
        userRepository.save(new User(USER_ID, FIRST_NAME, LAST_NAME));

        // Act
        boolean existing = userRepository.exists(USER_ID);
        boolean unknown = userRepository.exists("unknown-user");

        // Assert
        assertTrue(existing);
        assertFalse(unknown);
    }

    @Test
    void shouldSaveAndFindOrder() {
        // Arrange
        Order order = new Order(ORDER_ID, USER_ID, PRICE);

        // Act
        orderRepository.save(order);
        Order found = orderRepository.findById(ORDER_ID);

        // Assert
        assertEquals(ORDER_ID, found.getId());
        assertEquals(USER_ID, found.getUserId());
        assertEquals(PRICE, found.getPrice());
        assertEquals(OrderStatus.DRAFT, found.getStatus());
    }

    private static DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUser(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());
        return dataSource;
    }

    private static void cleanTables(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE users");
        }
    }
}