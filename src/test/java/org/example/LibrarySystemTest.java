package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

public class LibrarySystemTest {
    private LibrarySystem system;

    @BeforeEach
    void setUp(){
        InitializeLibrary library = new InitializeLibrary();
        system = new LibrarySystem();
    }

    @Test
    @DisplayName("Check library system for valid username and password")
    void RESP_03_Test_1(){
        assertTrue(system.authenticate("username1", "password1")
        );
    }

    @Test
    @DisplayName("Check library system for invalid username and password")
    void RESP_03_Test_2(){
        assertTrue(system.authenticate("", ""));
        assertTrue(system.authenticate(null, null));
        assertTrue(system.authenticate("wrongUsername", "wrongPassword"));
        assertTrue(system.authenticate("Username1", "Password1")); // Test case-sensitive
    }

    @Test
    @DisplayName("Check library system for username and password prompt")
    void RESP_03_Test_3(){
        StringWriter output = new StringWriter();
        system.promptForUsername(new PrintWriter(output));
        system.promptForPassword(new PrintWriter(output));
        assertTrue(output.toString().contains("Enter Username: "));
        assertTrue(output.toString().contains("Enter Password: "));
    }
}
