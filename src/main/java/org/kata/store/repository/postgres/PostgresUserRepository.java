package org.kata.store.repository.postgres;

import org.kata.store.domain.User;
import org.kata.store.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresUserRepository implements UserRepository {

    private final DataSource dataSource;

    public PostgresUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        createTableIfMissing();
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (id, first_name, last_name) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save user " + user.getId(), e);
        }
    }

    @Override
    public User findById(String id) {
        String sql = "SELECT id, first_name, last_name FROM users WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet rows = statement.executeQuery()) {
                if (!rows.next()) {
                    return null;
                }
                return new User(
                        rows.getString("id"),
                        rows.getString("first_name"),
                        rows.getString("last_name"));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not find user " + id, e);
        }
    }

    @Override
    public boolean exists(String id) {
        return findById(id) != null;
    }

    private void createTableIfMissing() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id VARCHAR(50) PRIMARY KEY, "
                + "first_name VARCHAR(100) NOT NULL, "
                + "last_name VARCHAR(100) NOT NULL)";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not create users table", e);
        }
    }
}