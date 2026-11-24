# Extra opcional: escenarios de Cucumber del registro de pedidos.
#
# Quita los "#" del ejemplo y adáptalo a tu API. Cada frase necesita su step en org.kata.store.cucumber.
#

Feature: Registro de pedidos

  Scenario: Un pedido nuevo empieza en borrador
    Given un usuario con id "user-1" llamado "Ana" "García"
    When el usuario "user-1" crea el pedido "order-1" de 50.0 euros
    Then el pedido "order-1" está en estado "DRAFT"

  Scenario Outline: El pedido avanza por sus estados
    Given un usuario con id "user-1" llamado "Ana" "García"
    And un pedido "order-1" del usuario "user-1" en estado "<actual>"
    When avanzo el pedido "order-1" a "<siguiente>"
    Then el pedido "order-1" está en estado "<siguiente>"

    Examples:
      | actual     | siguiente  |
      | DRAFT      | PROCESSED  |
      | PROCESSED  | DISPATCHED |
      | DISPATCHED | DELIVERED  |
