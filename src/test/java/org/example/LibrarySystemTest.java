package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

public class LibrarySystemTest {
    private LibrarySystem system;
    private AccountManager accounts;

    @BeforeEach
    void setUp(){
        InitializeLibrary library = new InitializeLibrary();
        system = new LibrarySystem();
        accounts = library.getAccounts();
    }

    @Test
    @DisplayName("Check library system for valid username and password")
    void RESP_03_Test_1(){
        assertTrue(accounts.authenticate("username1", "password1")
        );
    }

    @Test
    @DisplayName("Check library system for invalid username and password")
    void RESP_03_Test_2(){
        assertFalse(accounts.authenticate("", ""));
        assertFalse(accounts.authenticate(null, null));
        assertFalse(accounts.authenticate("wrongUsername", "wrongPassword"));
        assertFalse(accounts.authenticate("Username1", "Password1")); // Test case-sensitive
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

    @Test
    @DisplayName("Check library system for correct authentication error prompt")
    void RESP_03_Test_4(){
        StringWriter output = new StringWriter();
        system.promptAuthenticationError(new PrintWriter(output));
        assertTrue(output.toString().contains("Authentication Failed."));
    }
    }
