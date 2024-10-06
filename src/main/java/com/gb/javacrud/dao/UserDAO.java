package com.gb.javacrud.dao;

import com.gb.javacrud.db.IDatabaseConnector;
import com.gb.javacrud.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    private final IDatabaseConnector connector;

    public UserDAO(IDatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public boolean saveUser(User user) {
        String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        try(Connection connection = connector.connect();
            PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 1) {
                System.out.println("saved user: " + user.toString());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("error inserting user: " + e.getMessage());
        }

        return false;
    }

    public boolean updateUser(int userId, String columnName, String newValue)  {
        String query = "UPDATE users SET " + columnName + " = ? WHERE id = ?";

        try (Connection connection = connector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, newValue);
            stmt.setInt(2, userId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 1) {
                System.out.println("updated user " + userId + " " + columnName + " to " + newValue + ".");
                return true;
            } else {
                System.err.println("user with id " + userId + " not found.");
            }
        } catch (SQLException e) {
            System.err.println("error updating user: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection connection = connector.connect();
            PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 1) {
                System.out.println("deleted user of id " + userId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("error deleting user: " + e.getMessage());
        }

        return false;
    }

    @Override
    public User findById(int userId) {
        String query = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = connector.connect();
            PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                return mapUserFromResultSet(result);
            }
        } catch (SQLException e) {
            System.err.println("error searching user: " + e.getMessage());
        }

        return new User();
    }

    public boolean emailRegistered(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection connection = connector.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int count = result.getInt(1);
                if (count > 0) return true;
            }
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
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int count = result.getInt(1);
                if (count > 0) return true;
            }
        } catch (SQLException e) {
            System.err.println("error finding user by id: " + e.getMessage());
        }

        return false;
    }

    @Override
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
                "********"
        );
    }
}
