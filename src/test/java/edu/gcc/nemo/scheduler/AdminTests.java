package edu.gcc.nemo.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AdminTests {

    @Test
    void printInfo_ShouldPrintAdminInfo() {
        // Arrange
        User admin = new Admin("admin", "password", "John Doe", 1);
        // Act
        admin.printInfo();

    admin.printInfo();
    }

    @Test
    void checkPassword_ShouldReturnTrue_WhenPasswordMatches() {
        // Arrange
        User user = new Admin("admin", "password", "John Doe", 1);

        // Act
        boolean result = user.checkPassword("password");

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void checkPassword_ShouldReturnFalse_WhenPasswordDoesNotMatch() {
        // Arrange
        User user = new Admin("admin", "password", "John Doe", 1);

        // Act
        boolean result = user.checkPassword("wrong-password");

        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void toString_ShouldReturnUserName() {
        // Arrange
        User user = new Admin("admin", "password", "John Doe", 1);

        // Act
        String result = user.toString();

        // Assert
        Assertions.assertEquals("John Doe", result);
    }
}