package org.kata.store;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kata.store.domain.OrderStatus;
import org.kata.store.repository.OrderRepository;
import org.kata.store.repository.UserRepository;
import org.kata.store.service.OrderService;
import org.kata.store.service.UserService;

/*
 * TODO: Prueba de integración (repositorio en memoria obligatorio, TestContainers opcional).
 *
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
        userService.register("user-1", "Ana", "García");
        orderService.createOrder("order-1", "user-1", 50.0);

        // Act
        orderService.changeStatus("order-1", OrderStatus.PROCESSED);
        orderService.changeStatus("order-1", OrderStatus.DISPATCHED);
        orderService.changeStatus("order-1", OrderStatus.DELIVERED);

        // Assert
        assertEquals(OrderStatus.DELIVERED, orderRepository.findById("order-1").getStatus());
    }

}
