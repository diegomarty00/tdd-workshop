package org.kata.store.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/*
 * TODO: Tests unitarios de Order y de su flujo de estados.
 *
 * Empieza aquí: es la parte sin dependencias, la más fácil para arrancar el ciclo Red -> Green -> Refactor.
 *
 * Ideas de tests (uno por comportamiento, de uno en uno):
 *   - shouldStartInDraftStatus
 *   - shouldMoveFromDraftToProcessed
 *   - shouldNotSkipFromDraftToDispatched
 *   - shouldNotChangeStatusWhenDelivered
 *   - shouldRejectPriceLowerOrEqualThanZero  (prueba con @ParameterizedTest y @ValueSource)
 *
 * Recuerda:
 *   - Patrón AAA: // Arrange, // Act, // Assert.
 *   - Haz un commit en rojo, otro en verde y otro tras refactorizar.
 */
class OrderTest {

    @Test
    void shouldStartInDraftStatus() {
        // Arrange
        Order order = new Order("order-1", "user-1", 50.0);

        // Act
        OrderStatus status = order.getStatus();

        // Assert
        assertEquals(OrderStatus.DRAFT, status);
    }

    @Test
    void shouldMoveFromDraftToProcessed() {
        // Arrange
        Order order = new Order("order-1", "user-1", 50.0);

        // Act
        order.changeStatusTo(OrderStatus.PROCESSED);

        // Assert
        assertEquals(OrderStatus.PROCESSED, order.getStatus());
    }

    @Test
    void shouldNotSkipFromDraftToDispatched() {
        // Arrange
        Order order = new Order("order-1", "user-1", 50.0);

        // Act
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> order.changeStatusTo(OrderStatus.DISPATCHED));

        // Assert
        assertEquals("Cannot change status from DRAFT to DISPATCHED", exception.getMessage());
    }
}
