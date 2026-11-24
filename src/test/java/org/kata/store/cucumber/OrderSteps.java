package org.kata.store.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.kata.store.domain.OrderStatus;
import org.kata.store.repository.OrderRepository;
import org.kata.store.repository.UserRepository;
import org.kata.store.repository.memory.InMemoryOrderRepository;
import org.kata.store.repository.memory.InMemoryUserRepository;
import org.kata.store.service.OrderService;
import org.kata.store.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderSteps {

    private final OrderRepository orderRepository = new InMemoryOrderRepository();
    private final UserService userService;
    private final OrderService orderService;

    public OrderSteps() {
        UserRepository userRepository = new InMemoryUserRepository();
        userService = new UserService(userRepository);
        orderService = new OrderService(userRepository, orderRepository);
    }

    @Given("un usuario con id {string} llamado {string} {string}")
    public void unUsuario(String id, String firstName, String lastName) {
        userService.register(id, firstName, lastName);
    }

    @When("el usuario {string} crea el pedido {string} de {double} euros")
    public void crearPedido(String userId, String orderId, double price) {
        orderService.createOrder(orderId, userId, price);
    }

    @Then("el pedido {string} está en estado {string}")
    public void elPedidoEstaEnEstado(String orderId, String expectedStatus) {
        assertEquals(OrderStatus.valueOf(expectedStatus), orderRepository.findById(orderId).getStatus());
    }
}