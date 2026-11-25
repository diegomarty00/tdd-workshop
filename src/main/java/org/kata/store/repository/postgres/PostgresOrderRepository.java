package org.kata.store.repository.postgres;

import org.kata.store.domain.Order;
import org.kata.store.domain.OrderStatus;
import org.kata.store.repository.OrderRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresOrderRepository implements OrderRepository {

    private final DataSource dataSource;

    public PostgresOrderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        createTableIfMissing();
    }

    @Override
    public void save(Order order) {
        String sql = "INSERT INTO orders (id, user_id, price, status) VALUES (?, ?, ?, ?) "
            + "ON CONFLICT (id) DO UPDATE SET "
            + "user_id = EXCLUDED.user_id, price = EXCLUDED.price, status = EXCLUDED.status";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, order.getId());
            statement.setString(2, order.getUserId());
            statement.setDouble(3, order.getPrice());
            statement.setString(4, order.getStatus().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save order " + order.getId(), e);
        }
    }

    @Override
    public Order findById(String id) {
        String sql = "SELECT id, user_id, price, status FROM orders WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet rows = statement.executeQuery()) {
                if (!rows.next()) {
                    return null;
                }
                return new Order(
                        rows.getString("id"),
                        rows.getString("user_id"),
                        rows.getDouble("price"),
                        OrderStatus.valueOf(rows.getString("status")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not find order " + id, e);
        }
    }

    private void createTableIfMissing() {
        String sql = "CREATE TABLE IF NOT EXISTS orders ("
                + "id VARCHAR(50) PRIMARY KEY, "
                + "user_id VARCHAR(50) NOT NULL, "
                + "price NUMERIC(12, 2) NOT NULL, "
                + "status VARCHAR(20) NOT NULL)";
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not create orders table", e);
        }
    }
}