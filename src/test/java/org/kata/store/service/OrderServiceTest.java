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
import org.kata.store.domain.Order;
import org.kata.store.domain.OrderStatus;
import org.kata.store.repository.OrderRepository;
import org.kata.store.repository.UserRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/*
 * TODO: Tests unitarios de OrderService con Mockito.
 *
 * UserRepository y OrderRepository son dependencias: se sustituyen por dobles de prueba.
 *
 * Ideas de tests:
 *   - shouldCreateOrderInDraftForExistingUser      -> stub del usuario existente + verify del save
 *   - shouldRejectOrderForUnknownUser              -> stub: existe = false + assertThrows
 *   - shouldNotSaveOrderWhenUserDoesNotExist       -> verify(orderRepository, never()).save(any())
 *   - shouldRejectOrderWithInvalidPrice
 *   - shouldAdvanceOrderToNextStatus
 *   - shouldRejectInvalidStatusTransition
 *
 * Recuerda la diferencia:
 *   - Stub: la dependencia devuelve un valor fijo y compruebas el resultado (estado).
 *   - Mock: compruebas con verify que el servicio llamó a la dependencia (comportamiento).
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final String ORDER_ID = "order-1";
    private static final String USER_ID = "user-1";
    private static final double VALID_PRICE = 50.0;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(userRepository, orderRepository);
    }

    @Test
    void shouldRejectOrderForUnknownUser() {
        // Arrange
        when(userRepository.exists(USER_ID)).thenReturn(false);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.createOrder(ORDER_ID, USER_ID, VALID_PRICE));

        // Assert
        assertEquals("User user-1 does not exist", exception.getMessage());
    }

    @Test
    void shouldCreateOrderInDraftForExistingUser() {
        // Arrange
        when(userRepository.exists(USER_ID)).thenReturn(true);

        // Act
        Order order = orderService.createOrder(ORDER_ID, USER_ID, VALID_PRICE);

        // Assert
        assertEquals(OrderStatus.DRAFT, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void shouldNotSaveOrderWhenUserDoesNotExist() {
        // Arrange
        when(userRepository.exists(USER_ID)).thenReturn(false);

        // Act
        assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(ORDER_ID, USER_ID, VALID_PRICE));

        // Assert
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldAdvanceOrderToNextStatus() {
        // Arrange
        Order order = new Order(ORDER_ID, USER_ID, VALID_PRICE);
        when(orderRepository.findById(ORDER_ID)).thenReturn(order);

        // Act
        orderService.changeStatus(ORDER_ID, OrderStatus.PROCESSED);

        // Assert
        assertEquals(OrderStatus.PROCESSED, order.getStatus());
        verify(orderRepository).save(order);
    }
}
