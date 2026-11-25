package org.kata.store.domain;

/*
 * Datos: id de pedido, id de usuario, estado (OrderStatus) y precio.
 *
 * Requisitos (escribe primero el test de cada uno):
 *   - Un pedido nuevo empieza en estado DRAFT.
 *   - El precio tiene que ser mayor que 0.
 *   - Cambia de estado siguiendo el flujo descrito en OrderStatus.
 *
 * Pista: para el precio, piensa en los casos límite (0, negativo, decimales).
 * Un @ParameterizedTest te ahorra repetir tests casi iguales.
 */
public class Order {

    private String id;
    private String userId;
    private OrderStatus status;
    private double price;

    public Order(String id, String userId, double price) {
        this(id, userId, price, OrderStatus.DRAFT);
    }

    public Order(String id, String userId, double price, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        setPrice(price);
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    private void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        this.price = price;
    }

    public void changeStatusTo(OrderStatus newStatus) {
        if (status.isFinal()) {
            throw new IllegalStateException("Delivered order cannot change status");
        }
        if (status == OrderStatus.DRAFT && newStatus != OrderStatus.PROCESSED) {
            throw new IllegalStateException("Cannot change status from " + status + " to " + newStatus);
        }
        this.status = newStatus;
    }

}
