package org.kata.store.repository.memory;

import java.util.HashMap;
import java.util.Map;

import org.kata.store.domain.Order;
import org.kata.store.repository.OrderRepository;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> orders = new HashMap<>();

    @Override
    public void save(Order order) {
        orders.put(order.getId(), order);
    }

    @Override
    public Order findById(String id) {
        return orders.get(id);
    }
}