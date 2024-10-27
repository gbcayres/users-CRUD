package com.gb.usersCRUD;

import com.gb.usersCRUD.dao.UserDAO;
import com.gb.usersCRUD.model.User;
import com.gb.usersCRUD.validation.Notification;
import com.gb.usersCRUD.validation.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private UserDAO userDAOmock;

    @InjectMocks
    private UserValidator userValidator;

    private Notification notification;

    @BeforeEach
    public void setUp() {
        notification = new Notification();
    }

    @Nested
    class validateId {
        @Test
        @DisplayName("Should not add error when id is valid")
        public void shouldNotAddErrorWhenIdIsValid() {
            int validId = 3;
            when(userDAOmock.idExists(validId)).thenReturn(true);

            userValidator.validateId(validId, notification);

            assertNoErrorsAdded();
        }

        @Test
        @DisplayName("Should add error when id do not exists")
        public void shouldAddErrorWhenIdDoNotExists() {
            int validId = 3;
            when(userDAOmock.idExists(validId)).thenReturn(false);

            userValidator.validateId(validId, notification);

            assertAddedOneErrorCorrectly();
        }

        @Test
        @DisplayName("Should add error when id is invalid")
        public void shouldAddErrorWhenIdIsInvalid() {
            int invalidId = 0;

            userValidator.validateId(invalidId, notification);

            assertAddedOneErrorCorrectly();
        }
    }

    @Nested
    class ValidateUser {
        @Test
        @DisplayName("Should not add error when user is valid")
        public void ShouldNotAddErrorIfUserIsValid() {
            User validUser = new User("Bill", "bill@hotmail.com", "B1ll@Gat3s");

            userValidator.validateUser(validUser, notification);

            assertFalse(notification.hasErrors(), "expected no error when user is valid.");
        }

        @Test
        @DisplayName("Should add error when user is invalid")
        public void ShouldAddErrorWhenUserIsInvalid() {
            User invalidUser = new User("1nvalidN@me", "invalidmail.br", "invalido123");

            userValidator.validateUser(invalidUser, notification);

            assertTrue(notification.hasErrors(), "expected to add errors when user is invalid.");
        }
    }

    @Nested
    class ValidateName {
        @Test
        @DisplayName("Should not add error when name is valid")
        public void shouldNotAddErrorIfNameIsValid() {
            String validName = "Monique";

            userValidator.validateName(validName,  notification);

            assertNoErrorsAdded();
        }

        @Test
        @DisplayName("Should add error when name is null")
        public void shouldAddErrorWhenNameIsNull() {
            userValidator.validateName(null,  notification);

            assertAddedOneErrorCorrectly();
        }

        @Test
        @DisplayName("Should add error when name is empty")
        public void shouldAddErrorWhenNameIsEmpty() {
            String emptyName = " ";

            userValidator.validateName(emptyName,  notification);

            assertAddedOneErrorCorrectly();
        }

        @Test
        @DisplayName("Should add error when name contains numbers")
        public void shouldAddErrorWhenNameContainsNumbers() {
            String digitName = "J0hn";

            userValidator.validateName(digitName, notification);

            assertAddedOneErrorCorrectly();
        }

        @Test
        @DisplayName("Should add error when name contains special character")
        public void shouldAddErrorWhenNameContainsSpecialCharacter() {
            String specialName = "J@hn";

            userValidator.validateName(specialName, notification);

            assertAddedOneErrorCorrectly();

        }
    }

    @Nested
    class ValidateEmail {
        @Test
        @DisplayName("Should not add error when email is valid")
        public void shouldNotAddErrorWhenEmailIsValid() {
            String validEmail = "validEmail@gmail.com";

            userValidator.validateEmail(validEmail, notification);

            assertNoErrorsAdded();
        }


        @Test
        @DisplayName("Should add error when email is null")
        public void shouldAddErrorWhenEmailIsNull() {
            userValidator.validateEmail(null, notification);

            assertAddedOneErrorCorrectly();
        }

        @Test
        @DisplayName("Should add error when email is empty.")
        public void shouldAddErrorWhenEmailIsEmpty() {
            String emptyEmail = " ";

            userValidator.validateEmail(emptyEmail,  notification);

            assertAddedOneErrorCorrectly();
        }

        @Test
        @DisplayName("Should add error when email format is invalid")
        public void shouldAddErrorWhenEmailFormatIsInvalid() {
            String invalidEmail = "nothing.com";

            userValidator.validateEmail(invalidEmail,  notification);

            assertAddedOneErrorCorrectly();
        }

        @Test
        @DisplayName("Should add error when email is already in use")
        public void shouldAddErrorWhenEmailIsAlreadyInUse() {
            String email = "validEmail@gmail.com";
            when(userDAOmock.emailRegistered(email)).thenReturn(true);

            userValidator.validateEmail(email, notification);

            assertAddedOneErrorCorrectly();
        }
    }

    @Nested
    class ValidatePassword {
        @Test
        @DisplayName("Should not add error when password is valid")
        public void shouldNotAddErrorWhenPasswordIsValid() {
            String validPassword = "validPas$w0rd";

            userValidator.validatePassword(validPassword, notification);

            assertNoErrorsAdded();
        }


        @Test
        @DisplayName("Should add error when password is too short")
        public void shouldAddErrorWhenPasswordIsTooShort() {
            String shortPassword = "S#0rt";

            userValidator.validatePassword(shortPassword, notification);

            assertAddedOneErrorCorrectly();
        }

        @Test
        @DisplayName("Should add error when password does not contains upper case characters")
        public void shouldAddErrorWhenPasswordDoesNotContainsUppercase() {
            String passwordWithoutUppercase = "$tringwithoutuppercas3";

            userValidator.validatePassword(passwordWithoutUppercase, notification);

            assertAddedOneErrorCorrectly();
        }

        @Test
        @DisplayName("Should add error when password does not contains numbers")
        public void shouldAddErrorWhenPasswordDoesNotContainsNumbers() {
            String passwordWithoutNumbers = "stringWithoutDigit$";

            userValidator.validatePassword(passwordWithoutNumbers, notification);

            assertAddedOneErrorCorrectly();
        }

        @Test
        @DisplayName("Should add error when password does not contains special characters")
        public void shouldAddErrorWhenPasswordDoesNotContainsSpecialCharacters() {
            String passwordWithDigits = "str1ngW1thoutSpecial";

            userValidator.validatePassword(passwordWithDigits, notification);

            assertAddedOneErrorCorrectly();
        }
    }

    private void assertAddedOneErrorCorrectly() {
        assertTrue(notification.hasErrors());
        assertEquals(1, notification.getErrors().size());
    }

    private void assertNoErrorsAdded() {
        assertFalse(notification.hasErrors());
    }
}