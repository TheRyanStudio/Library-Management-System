package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
    private Collection collection;
    private AccountManager accounts;
    private LibrarySystem system;
    private Account testAccount;

    @BeforeEach
    void setUp(){
        InitializeLibrary library = new InitializeLibrary();
        collection = library.getCollection();
        accounts = library.getAccounts();
        system = new LibrarySystem();
    }

    @Test
    @DisplayName("Check library collection size is 20")
    void RESP_01_Test_1() {
        int size = collection.getCollectionSize();
        assertEquals(20, size);
    }

    @Test
    @DisplayName("Check library collection for valid books")
    void RESP_01_Test_2() {
        // Testing for 1st, 10th and 20th book
        assertEquals("Great Gatsby",collection.getBook(0).getTitle());
        assertEquals("Brave New World",collection.getBook(9).getTitle());
        assertEquals("The Picture of Dorian Gray",collection.getBook(19).getTitle());
    }

    @ParameterizedTest
    @ValueSource (ints = {0, 9, 19})
    @DisplayName("Check book status for available")
    void RESP_01_Test_3(int index){
        assertEquals("Available", collection.getBook(index).getStatus());
    }

    @Test
    @DisplayName("Check account manager for number of accounts")
    void RESP_02_Test_1(){
        int size = accounts.getAccountSize();
        assertEquals(3,size);
    }

    @Test
    @DisplayName("Check account manager for valid account")
    void RESP_02_Test_2(){
        // Checks to make sure username and passwords are valid
        assertTrue(accounts.getAccount(0).isValidUsername());
        assertTrue(accounts.getAccount(0).isValidPassword());
    }

    @Test
    @DisplayName("Check account manager for borrowed books")
    void RESP_02_Test_3(){
        // Checks an account for borrowed books
        assertEquals(0,accounts.getAccount(0).getBorrowedBooks().size());
    }

    @Test
    @DisplayName("Check library system for valid username and password")
    void RESP_03_Test_1(){
        Account authenticated = accounts.authenticate("username2", "password2");
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

    @Test
    @DisplayName("Check library system for session established")
    void RESP_04_Test_1(){
        testAccount = new Account("TestUsername", "TestPassword");
        accounts.addAccount(testAccount);
        assertNotNull(system.establishSession(testAccount));
        assertEquals("TestUsername", system.getCurrAccount().getUsername());
    }

    @Test
    @DisplayName("System presents operations")
    void RESP_05_Test_1(){
        StringWriter output = new StringWriter();
        system.promptOperations(new PrintWriter(output));
        assertTrue(output.toString().contains("(1) Borrow a book. (2) Return a book. (3) Logout. "));
    }
    @Test
    @DisplayName("Check library system displays book collection")
    void RESP_06_Test_1(){
        StringWriter output = new StringWriter();
        collection.displayCollection(new PrintWriter(output));
        String result = output.toString();

        // Testing that the display contains the 1st, 10th and 20th book
        assertTrue(result.contains("1. Title: Great Gatsby"));
        assertTrue(result.contains("Author: F. Scott FitzGerald"));
        assertTrue(result.contains("Status: AVAILABLE"));

        assertTrue(result.contains("10. Title: Brave New World"));
        assertTrue(result.contains("Author: Aldous Huxley"));
        assertTrue(result.contains("Status: AVAILABLE"));

        assertTrue(result.contains("20. Title: The Picture of Dorian Gray"));
        assertTrue(result.contains("Author: Oscar Wilde"));
        assertTrue(result.contains("Status: AVAILABLE"));
    }

    @Test
    @DisplayName("Check library system displays selected book and borrowing details")
    void RESP_07_Test_1(){
        StringWriter output = new StringWriter();
        Book newBook1 = new Book("Title1", "Author1");
        collection.promptBookConfirmation(newBook1 ,new PrintWriter(output));
        assertTrue(output.toString().contains("You selected Title1 by Author1. Enter (1) to proceed with borrowing. "));
    }
}

