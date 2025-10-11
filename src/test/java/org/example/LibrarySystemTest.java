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
        system = new LibrarySystem(accounts);
        accounts = library.getAccounts();
    }

    @Test
    @DisplayName("Check library system for valid username and password")
    void RESP_03_Test_1(){
        Account authenticated = accounts.authenticate("username1", "password1");
        assertNotNull(authenticated); // Method returns null with no matching account object
    }

    @Test
    @DisplayName("Check account manager for invalid username and password")
    void RESP_03_Test_2(){
        assertNull(accounts.authenticate("", ""));
        assertNull(accounts.authenticate(null, null));
        assertNull(accounts.authenticate("wrongUsername", "wrongPassword"));
        assertNull(accounts.authenticate("Username1", "Password1")); // Test case-sensitive
    }

    @Test
    @DisplayName("Check account manager for username and password prompt")
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

    // Implemented a test stub to allow for independent testing of the login method
    static class StubAccountManager extends AccountManager {
        @Override
        public Account authenticate(String username, String password){
            if("username2".equals(username) && "password2".equals(password)){
                return new Account(username, password);
            }
            return null;
        }
    }

    @Test
    @DisplayName("Check library system for login and session established")
    void RESP_04_Test_1(){
        // Uses a stub authenticate method inside login
        AccountManager stub = new StubAccountManager();
        LibrarySystem system = new LibrarySystem(stub);

        assertTrue(system.login("username2", "password2"));
        assertEquals("username2", system.getCurrAccount().getUsername());
    }

    }
