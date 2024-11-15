package com.gb.usersCRUD;

import com.gb.usersCRUD.db.DatabaseConnector;
import com.gb.usersCRUD.model.User;
import com.gb.usersCRUD.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private DatabaseConnector connector;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private UserRepository userRepository;

    private User testUser = testUser = new User(
            UUID.randomUUID(),
            "testUser",
            "test@gmail.com",
            "Test@1234",
            Instant.now(),
            Instant.now()
    );

    @BeforeEach
    public void setUp() throws SQLException {
        when(connector.connect()).thenReturn(connection);
    }

    @Nested
    class SaveUser {
        @Test
        @DisplayName("should return saved user ID when save is successful")
        public void shouldReturnSavedUserWhenSaveIsSuccessful() throws SQLException {
            // arrange
            String query = "INSERT INTO users (name, email, password, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?) RETURNING id";

            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getObject("id", UUID.class)).thenReturn(testUser.getId());

            // act
            UUID savedUserId = userRepository.save(testUser);

            // assert
            assertEquals(savedUserId, testUser.getId()); // assume que você tenha equals implementado no User
            verify(preparedStatement, times(1)).executeQuery(); // agora verifica o método correto
        }

        @Test
        @DisplayName("should throw exception when error occurs")
        public void shouldThrowExceptionWhenErrorOccurs() throws SQLException {
            // arrange
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenThrow(new SQLException("Database Error"));

            // act & assert
            assertThrows(RuntimeException.class, () -> userRepository.save(testUser));
        }
    }

    @Nested
    class UpdateUser {
        @Test
        @DisplayName("should return true when update is successful")
        public void shouldReturnTrueWhenUpdateIsSuccessful() throws SQLException {
            // arrange
            String query = "UPDATE users SET name = ?, email = ?, password = ?, updatedAt = ? WHERE id = ?";

            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            // act
            boolean updated = userRepository.update(testUser.getId(), testUser);

            // assert
            assertTrue(updated);
            verify(preparedStatement, times(1)).executeUpdate();
        }

        @Test
        @DisplayName("should throw exception when error occurs")
        public void shouldThrowExceptionWhenErrorOccurs() throws SQLException {
            // arrange
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database Error"));

            // act & assert
            assertThrows(RuntimeException.class, () -> userRepository.update(testUser.getId(), testUser));
        }
    }

    @Nested
    class DeleteUser {
        @Test
        @DisplayName("should return true when deletion is successful")
        public void shouldReturnTrueWhenDeleteIsSuccessful() throws SQLException {
            // arrange
            String query = "DELETE FROM users WHERE id = ?";

            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            // act
            boolean deleted = userRepository.delete(testUser.getId());

            // assert
            assertTrue(deleted);
            verify(preparedStatement, times(1)).executeUpdate();
        }

        @Test
        @DisplayName("should throw exception when error occurs")
        public void shouldThrowExceptionWhenErrorOccurs() throws SQLException {
            // arrange
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database Error"));

            // act & assert
            assertThrows(RuntimeException.class, () -> userRepository.delete(testUser.getId()));
        }
    }

    @Nested
    class EmailInUse {
        @Test
        @DisplayName("should return true when email is already in use")
        public void shouldReturnTrueWhenEmailIsAlreadyInUse() throws SQLException {
            // arrange
            String query = "SELECT COUNT(*) FROM users WHERE email = ?";

            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("count")).thenReturn(1);

            // act
            boolean inUse = userRepository.emailInUse(testUser.getEmail());

            // assert
            assertTrue(inUse);
            verify(preparedStatement, times(1)).executeQuery();
        }

        @Test
        @DisplayName("should return false when email is not in use")
        public void shouldReturnFalseWhenEmailIsNotInUse() throws SQLException {
            // arrange
            String query = "SELECT COUNT(*) FROM users WHERE email = ?";

            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("count")).thenReturn(0);

            // act
            boolean inUse = userRepository.emailInUse(testUser.getEmail());

            // assert
            assertFalse(inUse);
            verify(preparedStatement, times(1)).executeQuery();
        }

        @Test
        @DisplayName("should throw exception when error occurs")
        public void shouldThrowExceptionWhenErrorOccurs() throws SQLException {
            // arrange
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            when(preparedStatement.executeQuery()).thenThrow(new SQLException("Database Error"));

            // act & assert
            assertThrows(RuntimeException.class, () -> userRepository.emailInUse(testUser.getEmail()));
        }
    }

    @Nested
    class idExists  {
        @Test
        @DisplayName("should return true when id exists")
        public void shouldReturnTrueWhenIdExists() throws SQLException {
            // arrange
            String query = "SELECT COUNT(*) FROM users WHERE id = ?";

            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("count")).thenReturn(1);

            // act
            boolean exists = userRepository.idExists(testUser.getId());

            // assert
            assertTrue(exists);
            verify(preparedStatement, times(1)).executeQuery();
        }

        @Test
        @DisplayName("should return false when id do not exists")
        public void shouldReturnTrueWhenIdNotExists() throws SQLException {
            // arrange
            String query = "SELECT COUNT(*) FROM users WHERE id = ?";

            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("count")).thenReturn(0);

            // act
            boolean exists = userRepository.idExists(testUser.getId());

            // assert
            assertFalse(exists);
            verify(preparedStatement, times(1)).executeQuery();
        }

        @Test
        @DisplayName("should throw exception when error occurs")
        public void shouldThrowExceptionWhenErrorOccurs() throws SQLException {
            // arrange
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenThrow(new SQLException("Database Error"));

            // act & assert
            assertThrows(RuntimeException.class, () -> userRepository.idExists(testUser.getId()));
        }
    }
    
    @Nested
    class FindById {
        
        @Test
        @DisplayName("should return user when found")
        public void shouldReturnUserWhenFound() throws SQLException {
            // arrange
            String query = "SELECT * FROM users WHERE id = ?";
            
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            mockResultSet(resultSet, testUser);
            
            // act
            Optional<User> user = userRepository.findById(testUser.getId());
            
            // assert
            assertTrue(user.isPresent());
            assertEquals(testUser, user.get());
            verify(preparedStatement, times(1)).executeQuery();
        }

        @Test
        @DisplayName("should return empty optional when user is not found")
        public void shouldReturnEmptyOptionalWhenUserIsNotFound() throws SQLException {
            // arrange
            String query = "SELECT * FROM users WHERE id = ?";

            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            // act
            Optional<User> user = userRepository.findById(testUser.getId());

            // assert
            assertTrue(user.isEmpty());
            verify(preparedStatement, times(1)).executeQuery();
        }

        @Test
        @DisplayName("should throw exception when error occurs")
        public void shouldThrowExceptionWhenErrorOccurs() throws SQLException {
            // arrange
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenThrow(new SQLException("Database Error"));

            // act & assert
            assertThrows(RuntimeException.class, () -> userRepository.findById(testUser.getId()));
        }
    }

    @Nested
    class FindAll {

        @Test
        @DisplayName("should return all saved users")
        public void ShouldReturnAllSavedUsers() throws SQLException {
            // arrange
            String query = "SELECT * FROM users ORDER BY id ASC";

            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            mockResultSet(resultSet, testUser);

            // act
            List<User> users = userRepository.findAll();

            // assert
            assertNotNull(users);
            assertEquals(1, users.size());
            assertEquals(testUser, users.get(0));
            verify(preparedStatement, times(1)).executeQuery();
            verify(resultSet, times(2)).next();
        }

        @Test
        @DisplayName("should return empty list when no users found")
        public void ShouldReturnEmptyListWhenNoUsersFound() throws SQLException {
            // arrange
            String query = "SELECT * FROM users ORDER BY id ASC";

            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            // act
            List<User> users = userRepository.findAll();

            // assert
            assertNotNull(users);
            assertTrue(users.isEmpty());
            verify(preparedStatement, times(1)).executeQuery();
            verify(resultSet, times(1)).next();
        }

        @Test
        @DisplayName("should throw exception when error occurs")
        public void shouldThrowExceptionWhenErrorOccurs() throws SQLException {
            // arrange
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenThrow(new SQLException("Database Error"));

            // act & assert
            assertThrows(RuntimeException.class, () -> userRepository.findAll());
        }
    }

    private void mockResultSet(ResultSet resultSet, User user) throws SQLException {
        when(resultSet.getObject("id", UUID.class)).thenReturn(user.getId());
        when(resultSet.getString("name")).thenReturn(user.getName());
        when(resultSet.getString("email")).thenReturn(user.getEmail());
        when(resultSet.getString("password")).thenReturn(user.getPassword());
        when(resultSet.getTimestamp("createdAt")).thenReturn(Timestamp.from(user.getCreatedAt()));
        when(resultSet.getTimestamp("updatedAt")).thenReturn(Timestamp.from(user.getUpdatedAt()));
    }
}