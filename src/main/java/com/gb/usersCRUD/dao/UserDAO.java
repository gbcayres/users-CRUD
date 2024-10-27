package com.gb.usersCRUD.dao;

import com.gb.usersCRUD.db.DatabaseConnector;
import com.gb.usersCRUD.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final DatabaseConnector connector;

    public UserDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public User saveUser(User user) {
        String query = "INSERT INTO users (name, email, password, createdAt) VALUES (?, ?, ?, ?) RETURNING *";

        try(Connection connection = connector.connect();
            PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setTimestamp(4, Timestamp.from(user.getCreatedAt()));

            return executeAndGetUser(stmt);
        } catch (SQLException e) {
            System.err.println("error inserting user: " + e.getMessage());
        }

       return new User();
    }

    public User updateUser(int userId, User user)  {
        String query = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ? RETURNING *";

        try (Connection connection = connector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, userId);

            return executeAndGetUser(stmt);
        } catch (SQLException e) {
            System.err.println("error updating user: " + e.getMessage());
        }

        return new User();
    }

    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection connection = connector.connect();
            PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);

             int rowsAffected = stmt.executeUpdate();

            return rowsAffected == 1;
        } catch (SQLException e) {
            System.err.println("error deleting user: " + e.getMessage());
        }

        return false;
    }

    private User executeAndGetUser(PreparedStatement stmt) throws SQLException {
        try (ResultSet result = stmt.executeQuery()) {
            if (result.next()) {
                return mapUserFromResultSet(result);
            }
        }

        return new User();
    }

    public boolean emailRegistered(String email) {
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

    public boolean idExists(int id) {
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection connection = connector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);

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

    public User findById(int userId) {
        String query = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = connector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return mapUserFromResultSet(result);
                }
            }
        } catch (SQLException e) {
            System.err.println("error searching user: " + e.getMessage());
        }

        return new User();
    }

    public List<User> selectAllUsers() {
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
                result.getInt("id"),
                result.getString("name"),
                result.getString("email"),
                result.getString("password"),
                result.getTimestamp("createdAt").toInstant()
        );
    }
}
