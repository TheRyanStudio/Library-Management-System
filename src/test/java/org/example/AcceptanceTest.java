package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

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
        assertEquals("CHECKED_OUT", gatsby.getStatus());

        // User1 logs out
        system.logout(new PrintWriter(System.out));
        assertNull(system.getCurrAccount());

        // User2 logs in and sees book is checked out
        system.establishSession(user2);
        assertEquals("CHECKED_OUT", gatsby.getStatus());
        assertFalse(user2.isBookInList(gatsby));

        // User1 logs back in and returns the book
        system.establishSession(user1);
        assertEquals(LibrarySystem.ReturnResult.BOOK_AVAILABLE, system.returnBook(gatsby));
        assertFalse(user1.isBookInList(gatsby));
        assertEquals("AVAILABLE", gatsby.getStatus());

        // User2 should see the book available now
        system.establishSession(user2);
        assertEquals("AVAILABLE", gatsby.getStatus());
    }
}
