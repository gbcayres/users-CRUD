package com.gb.usersCRUD;

import com.gb.usersCRUD.dto.UserDTO;
import com.gb.usersCRUD.model.User;
import com.gb.usersCRUD.repository.UserRepository;
import com.gb.usersCRUD.service.UserService;
import com.gb.usersCRUD.validation.UserNotFoundException;
import com.gb.usersCRUD.validation.UserValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> idArgumentCaptor;

    @Nested
    class CreateUser {
        @Test
        @DisplayName("should create user successfully")
        public void shouldCreateUserSuccessfully() throws SQLException {
            // arrange
            UUID generatedId = UUID.randomUUID();
            when(userRepository.save(userArgumentCaptor.capture())).thenReturn(generatedId);

            UserDTO newUser = new UserDTO("Test", "test@gmail.com", "Test@1234");

            // act
            UUID savedUserId = userService.createUser(newUser);

            // assert
            assertNotNull(savedUserId);
            assertEquals(generatedId, savedUserId);

            User capturedUser = userArgumentCaptor.getValue();

            assertEquals(capturedUser.getName(), newUser.name());
            assertEquals(capturedUser.getEmail(), newUser.email());
            assertTrue(capturedUser.getPassword().startsWith("$2a$"));
        }

        @Test
        @DisplayName("should throw exception when user fields are invalid")
        public void shouldThrowExceptionWhenUserFieldsAreInvalid() throws SQLException {
            // arrange
            UserDTO invalidUser = new UserDTO("", "testgmail.com", null);

            // act & assert
            assertThrows(UserValidationException.class, () -> userService.createUser(invalidUser));
        }

        @Test
        @DisplayName("should throw exception when email already exists")
        public void shouldThrowExceptionWhenEmailAlreadyExists() throws SQLException {
            // arrange
            when(userRepository.emailInUse("test@gmail.com")).thenReturn(true);

            UserDTO newUser = new UserDTO("Test", "test@gmail.com", "Test@1234");

            // act & assert
            assertThrows(UserValidationException.class, () -> userService.createUser(newUser));
        }

        @Test
        @DisplayName("should throw exception when error ")
        public void shouldThrowExceptionWhenDTOisNull() throws SQLException {
            // arrange
            when(userRepository.save(any())).thenThrow(new RuntimeException());

            UserDTO newUser = new UserDTO("Test", "test@gmail.com", "Test@1234");

            // act & assert
            assertThrows(RuntimeException.class, () -> userService.createUser(newUser));
        }
    }

    @Nested
    class UpdateUser {
        @Test
        @DisplayName("should update user successfully")
        public void shouldUpdateUserSuccessfully() {
            // arrange
            UUID userId = UUID.randomUUID();

            UserDTO updateUserDTO = new UserDTO("newName", "newEmail@gmail.com", "new@P4ss");

            User existingUser = new User(
                    userId,
                    "oldName",
                    "oldName@gmail.com",
                    "0ldP4$$word",
                    Instant.now(),
                    Instant.now()
            );

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // act
            userService.updateUser(userId, updateUserDTO);

            // assert
            verify(userRepository, times(1)).findById(eq(userId));
            verify(userRepository, times(1)).update(eq(userId), userArgumentCaptor.capture());

            User capturedUser = userArgumentCaptor.getValue();

            assertEquals(updateUserDTO.name(), capturedUser.getName());
            assertEquals(updateUserDTO.email(), capturedUser.getEmail());
            assertTrue(capturedUser.getPassword().startsWith("$2a$"));
        }

        @Test
        @DisplayName("should throw exception when user not found")
        public void shouldThrowExceptionWhenUserNotFound() {
            // arrange
            UUID userId = UUID.randomUUID();

            UserDTO updateUserDTO = new UserDTO("newName", "newEmail@gmail.com", "new@P4ss");

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // act & assert
            assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, updateUserDTO));
        }

        @Test
        @DisplayName("should throw exception if validation fails")
        public void shouldThrowExceptionIfValidationFails() {
            // arrange
            UUID userId = UUID.randomUUID();

            UserDTO invalidUpdateUserDTO = new UserDTO("1nv@lid", "invalidmail.com", "");

            User existingUser = new User(
                    userId,
                    "oldName",
                    "oldName@gmail.com",
                    "0ldP4$$word",
                    Instant.now(),
                    Instant.now()
            );

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // act & assert
            assertThrows(UserValidationException.class, () -> userService.updateUser(userId, invalidUpdateUserDTO));
        }

        @Test
        @DisplayName("should not update null updateUserDTO fields")
        public void shouldNotUpdateNullDTOFields() {
            // arrange
            UUID userId = UUID.randomUUID();

            UserDTO updateUserDTO = new UserDTO(null, null, null);

            User existingUser = new User(
                    userId,
                    "oldName",
                    "oldName@gmail.com",
                    "0ldP4$$word",
                    Instant.now(),
                    Instant.now()
            );

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // act
            userService.updateUser(userId, updateUserDTO);

            // assert
            verify(userRepository, times(1)).update(eq(userId), userArgumentCaptor.capture());

            User capturedUser = userArgumentCaptor.getValue();

            assertEquals(existingUser.getName(), capturedUser.getName());
            assertEquals(existingUser.getEmail(), capturedUser.getEmail());
            assertEquals(existingUser.getPassword(), capturedUser.getPassword());
        }
    }

    @Nested
    class RemoveUser {
        // should remove user successfully
        @Test
        @DisplayName("should remove user successfully")
        public void shouldRemoveUserSuccessfully() {
            // arrange
            UUID userId = UUID.randomUUID();

            when(userRepository.idExists(userId)).thenReturn(true);

            // act
            userService.removeUser(userId);

            // assert
            verify(userRepository, times(1)).delete(eq(userId));
        }

        @Test
        @DisplayName("should throw exception when user not found")
        public void shouldThrowExceptionWhenUserNotFound() {
            // arrange
            UUID userId = UUID.randomUUID();

            when(userRepository.idExists(userId)).thenReturn(false);

            // act & assert
            assertThrows(UserNotFoundException.class, () -> userService.removeUser(userId));
        }
    }

    @Nested
    class GetUserById {
        @Test
        @DisplayName("should get user by id successfully")
        public void shouldRemoveUserSuccessfully() {
            // arrange
            UUID userId = UUID.randomUUID();

            User existingUser = new User(
                    userId,
                    "oldName",
                    "oldName@gmail.com",
                    "0ldP4$$word",
                    Instant.now(),
                    Instant.now()
            );

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // act
            User user = userService.getUserById(userId);

            // assert
            verify(userRepository, times(1)).findById(eq(userId));
            assertEquals(userId, user.getId());
        }


        @Test
        @DisplayName("should throw exception when user not found")
        public void shouldThrowExceptionWhenUserNotFound() {
            // arrange
            UUID userId = UUID.randomUUID();

            when(userRepository.idExists(userId)).thenReturn(false);

            // act & assert
            assertThrows(UserNotFoundException.class, () -> userService.removeUser(userId));
        }
    }

    @Nested
    class listUsers {

        @Test
        @DisplayName("should get all users successfully")
        public void shouldGetAllUsersSuccessfully() {
            // arrange
            UUID userId = UUID.randomUUID();

            User user = new User(
                    userId,
                    "oldName",
                    "oldName@gmail.com",
                    "0ldP4$$word",
                    Instant.now(),
                    Instant.now()
            );

            List<User> existingUsers = new ArrayList<>(Arrays.asList(user));

            when(userRepository.findAll()).thenReturn(existingUsers);

            // act
            List<User> users = userService.listUsers();

            // assert
            verify(userRepository, times(1)).findAll();
            assertEquals(1, users.size());
            assertEquals(user, users.get(0));
        }
    }
}