package org.kata.store;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.store.domain.OrderStatus;
import org.kata.store.repository.*;
import org.kata.store.repository.memory.*;
import org.kata.store.service.*;

/*
 * A diferencia de los tests unitarios, aquí NO se usan mocks: se conectan las piezas reales
 * (servicios + repositorios) y se prueba un flujo completo. Por ejemplo:
 *
 *   dar de alta un usuario -> crear un pedido -> avanzarlo hasta DELIVERED -> comprobar su estado final
 *
 * Opciones:
 *   - Repositorios en memoria (lo mínimo).
 *   - Extra: repositorios con PostgreSQL probados con Testcontainers
 *     (@Testcontainers, @Container y PostgreSQLContainer; las dependencias ya están en el pom.xml).
 *
 * Las clases que terminan en IT las ejecuta maven-failsafe-plugin con `mvn verify`, no con `mvn test`.
 */
class OrderRegistryIT {

    private static final String ORDER_ID = "order-1";
    private static final String USER_ID = "user-1";
    private static final String FIRST_NAME = "Ana";
    private static final String LAST_NAME = "García";

    private UserService userService;
    private OrderService orderService;
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new InMemoryUserRepository();
        orderRepository = new InMemoryOrderRepository();
        userService = new UserService(userRepository);
        orderService = new OrderService(userRepository, orderRepository);
    }

    @Test
    void shouldTakeOrderFromDraftToDelivered() {
        // Arrange
        userService.register(USER_ID, FIRST_NAME, LAST_NAME);
        orderService.createOrder(ORDER_ID, USER_ID, 50.0);

        // Act
        orderService.changeStatus(ORDER_ID, OrderStatus.PROCESSED);
        orderService.changeStatus(ORDER_ID, OrderStatus.DISPATCHED);
        orderService.changeStatus(ORDER_ID, OrderStatus.DELIVERED);

        // Assert
        assertEquals(OrderStatus.DELIVERED, orderRepository.findById(ORDER_ID).getStatus());
    }

    @Test
    void shouldRejectSecondUserWithSameId() {
        // Arrange
        userService.register(USER_ID, FIRST_NAME, LAST_NAME);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(USER_ID, "Luis", "Pérez"));

        // Assert
        assertEquals("User with id " + USER_ID + " already exists", exception.getMessage());
    }

    @Test
    void shouldNotCreateOrderForUnregisteredUser() {
        // Act
        assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(ORDER_ID, USER_ID, 50.0));

        // Assert
        assertNull(orderRepository.findById(ORDER_ID));
    }
}
