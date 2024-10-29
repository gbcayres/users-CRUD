package com.gb.usersCRUD.repository;

import com.gb.usersCRUD.db.DatabaseConnector;
import com.gb.usersCRUD.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {

    private final DatabaseConnector connector;

    public UserRepository(DatabaseConnector connector) {
        this.connector = connector;
    }

    public User save(User user) {
        String query = "INSERT INTO users (name, email, password, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?) RETURNING *";

        try(Connection connection = connector.connect();
            PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setTimestamp(4, Timestamp.from(user.getCreatedAt()));
            stmt.setTimestamp(5, Timestamp.from(user.getCreatedAt()));

            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return mapUserFromResultSet(result);
                }
            }
        } catch (SQLException e) {
            System.err.println("error inserting user: " + e.getMessage());
        }

       return new User();
    }

    public boolean update(UUID userId, User user)  {
        String query = "UPDATE users SET name = ?, email = ?, password = ?, updatedAt = ? WHERE id = ?";

        try (Connection connection = connector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setTimestamp(4, Timestamp.from(user.getUpdatedAt()));
            stmt.setObject(5, userId);

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected == 1;
        } catch (SQLException e) {
            System.err.println("error updating user: " + e.getMessage());
        }

        return false;
    }

    public boolean delete(UUID userId) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection connection = connector.connect();
            PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setObject(1, userId);

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected == 1;
        } catch (SQLException e) {
            System.err.println("error deleting user: " + e.getMessage());
        }

        return false;
    }

    public boolean emailInUse(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection connection = connector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);

            return executeAndCheckRecord(stmt);
        } catch (SQLException e) {
            System.err.println("error finding user by email: " + e.getMessage());
        }

        return false;
    }

    public boolean idExists(UUID id) {
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";

        try (Connection connection = connector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setObject(1, id);

            return executeAndCheckRecord(stmt);
        } catch (SQLException e) {
            System.err.println("error finding user by id: " + e.getMessage());
        }

        return false;
    }

    private boolean executeAndCheckRecord(PreparedStatement stmt) throws SQLException {
        try (ResultSet result = stmt.executeQuery()) {
            return result.next() && result.getInt(1) > 0;
        }
    }

    public Optional<User> findById(UUID userId) {
        String query = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = connector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setObject(1, userId);

            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapUserFromResultSet(result));
                }
            }
        } catch (SQLException e) {
            System.err.println("error searching user: " + e.getMessage());
        }

        return Optional.empty();
    }

    public List<User> findAll() {
        String query = "SELECT * FROM users ORDER BY id ASC";

        List<User> users = new ArrayList<>();

        try(Connection connection = connector.connect();
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet result = stmt.executeQuery()) {

            while (result.next()){
                users.add(mapUserFromResultSet(result));
            }
        } catch (SQLException e) {
            System.err.println("error inserting user: " + e.getMessage());
        }

        return users;
    }

    private User mapUserFromResultSet(ResultSet result) throws SQLException {
        return new User(
            result.getObject("id", UUID.class),
                result.getString("name"),
                result.getString("email"),
                result.getString("password"),
                result.getTimestamp("createdAt").toInstant(),
                result.getTimestamp("updatedAt").toInstant()
        );
    }
}