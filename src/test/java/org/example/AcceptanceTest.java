package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptanceTest {

    @Test
    @DisplayName("A-TEST-01: Multi-User Borrow and Return with Availability Validated")
    void A_TEST_01() {
        // Setup library
        InitializeLibrary library = new InitializeLibrary();
        Collection collection = library.getCollection();
        AccountManager accounts = library.getAccounts();
        LibrarySystem system = new LibrarySystem(collection);

        // Users
        Account user1 = accounts.getAccount(0); // username1
        Account user2 = accounts.getAccount(1); // username2

        Book gatsby = collection.getBook(0); // "Great Gatsby"

        // User1 logs in and borrows the book
        system.establishSession(user1);
        assertEquals(LibrarySystem.BorrowResult.BORROW_ALLOWED, system.verifyBorrowingAvailability(gatsby));
        system.borrowBook(gatsby);
        assertTrue(user1.isBookInList(gatsby));
        assertEquals(Book.BookStatus.CHECKED_OUT, gatsby.getStatus());

        // User1 logs out
        system.logout(new PrintWriter(System.out));
        assertNull(system.getCurrAccount());

        // User2 logs in and sees book is checked out
        system.establishSession(user2);
        assertEquals(Book.BookStatus.CHECKED_OUT, gatsby.getStatus());
        assertFalse(user2.isBookInList(gatsby));

        // User1 logs back in and returns the book
        system.establishSession(user1);
        assertEquals(LibrarySystem.ReturnResult.BOOK_AVAILABLE, system.returnBook(gatsby));
        assertFalse(user1.isBookInList(gatsby));
        assertEquals(Book.BookStatus.AVAILABLE, gatsby.getStatus());

        // User2 should see the book available now
        system.establishSession(user2);
        assertEquals(Book.BookStatus.AVAILABLE, gatsby.getStatus());
    }

    @Test
    @DisplayName("Initialization and Authentication with Error Handling")
    void A_TEST_02() {
        // Setup library
        InitializeLibrary library = new InitializeLibrary();
        Collection collection = library.getCollection();
        AccountManager accounts = library.getAccounts();
        LibrarySystem system = new LibrarySystem(collection);

        // Check initialization
        assertEquals(20, collection.getCollectionSize());
        assertEquals(3, accounts.getAccountSize());

        // Valid login
        Account validAccount = accounts.authenticate("alice", "pass123");
        assertNotNull(validAccount);
        system.establishSession(validAccount);

        // Check menu display
        StringWriter menuOutput = new StringWriter();
        system.promptOperations(new PrintWriter(menuOutput));
        assertTrue(menuOutput.toString().contains("(1) Borrow a book. (2) Return a book. (3) Logout."));

        // Logout
        StringWriter logoutOutput = new StringWriter();
        system.logout(new PrintWriter(logoutOutput));
        assertTrue(logoutOutput.toString().contains("You have logged out."));
        assertNull(system.getCurrAccount());

        // Invalid login
        Account invalidAccount = accounts.authenticate("wrongUser", "wrongPass");
        assertNull(invalidAccount);

        StringWriter authenticateFailOutput = new StringWriter();
        system.displayAuthenticationError(new PrintWriter(authenticateFailOutput));
        assertTrue(authenticateFailOutput.toString().contains("Authentication Failed. Please Retry"));

    }

}
