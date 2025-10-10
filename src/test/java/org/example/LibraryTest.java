package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
    private Collection collection;
    private AccountManager accounts;

    @BeforeEach
    void setUp(){
        InitializeLibrary library = new InitializeLibrary();
        collection = library.initializeBooks();
        accounts = library.initializeAccounts();
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
}
