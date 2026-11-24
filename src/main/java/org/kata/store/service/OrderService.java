package org.kata.store.service;

import org.kata.store.domain.Order;
import org.kata.store.domain.OrderStatus;
import org.kata.store.repository.*;

/*
 * TODO: Registro de pedidos.
 *
 * Requisitos (escribe primero el test de cada uno):
 *   - Crear un pedido para un usuario: queda en estado DRAFT.
 *   - No se puede crear un pedido para un usuario que no existe: se lanza una excepción.
 *   - No se puede crear un pedido con precio menor o igual que 0: se lanza una excepción.
 *   - Avanzar un pedido al siguiente estado (DRAFT -> PROCESSED -> DISPATCHED -> DELIVERED).
 *   - No se puede saltar un estado, retroceder ni cambiar un pedido entregado: se lanza una excepción.
 *
 * Pista: recibe UserRepository y OrderRepository por constructor.
 *   - Stub: when(userRepository.existsById(...)).thenReturn(false) para probar el usuario inexistente.
 *   - Mock: verify(orderRepository).save(...) para comprobar que el pedido se guarda.
 */
public class OrderService {

    private UserRepository userRepository;
    private OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(String orderId, String userId, double price) {
        if (!userRepository.exists(userId)) {
            throw new IllegalArgumentException("User " + userId + " does not exist");
        }
        Order order = new Order(orderId, userId, price);
        orderRepository.save(order);
        return order;
    }

    public void changeStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId);
        order.changeStatusTo(newStatus);
        orderRepository.save(order);
    }

}
