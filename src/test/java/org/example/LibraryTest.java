package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    void setUp() {
        InitializeLibrary library = new InitializeLibrary();
        collection = library.getCollection();
        accounts = library.getAccounts();
        system = new LibrarySystem(collection);
    }

    @Test
    @DisplayName("Check library collection size is 20")
    void RESP_01_Test_1() {
        int size = collection.getCollectionSize();
        assertEquals(20, size);
    }

    @ParameterizedTest
    @CsvSource({
            "0, Great Gatsby",
            "9, Brave New World",
            "19, The Picture of Dorian Gray"
    })
    @DisplayName("Check library collection for valid books")
    void RESP_01_Test_2(int index, String expectedTitle) {
        // Testing for 1st, 10th and 20th book
        assertEquals(expectedTitle, collection.getBook(index).getTitle());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 9, 19})
    @DisplayName("Check book status for available")
    void RESP_01_Test_3(int index) {
        assertEquals("AVAILABLE", collection.getBook(index).getStatus());
    }

    @Test
    @DisplayName("Check account manager for number of accounts")
    void RESP_02_Test_1() {
        int size = accounts.getAccountSize();
        assertEquals(3, size);
    }

    @Test
    @DisplayName("Check account manager for valid account")
    void RESP_02_Test_2() {
        // Checks to make sure username and passwords are valid
        assertTrue(accounts.getAccount(0).isValidUsername());
        assertTrue(accounts.getAccount(0).isValidPassword());
    }

    @Test
    @DisplayName("Check account manager for borrowed books")
    void RESP_02_Test_3() {
        // Checks an account for borrowed books
        assertEquals(0, accounts.getAccount(0).getBorrowedBooks().size());
    }

    @Test
    @DisplayName("Check library system for valid username and password")
    void RESP_03_Test_1() {
        Account authenticated = accounts.authenticate("username2", "password2");
        assertNotNull(authenticated); // Method returns null with no matching account object
    }

    @ParameterizedTest
    @CsvSource({
            "'',''",
            "null, null",
            "wrongUsername, wrongPassword",
            "Username1, Password1"
    })
    // Tests for invalid authentication with empty, null, incorrect and caps attempts
    @DisplayName("Check account manager for invalid username and password")
    void RESP_03_Test_2(String username, String password) {
        assertNull(accounts.authenticate(username, password));
    }

    @Test
    @DisplayName("Check account manager for password prompt")
    void RESP_03_Test_3() {
        StringWriter output = new StringWriter();
        system.promptForPassword(new PrintWriter(output));
        assertTrue(output.toString().contains("Enter Password: "));
    }
    @Test
    @DisplayName("Check account manager for username prompt")
    void RESP_03_Test_4() {
        StringWriter output = new StringWriter();
        system.promptForUsername(new PrintWriter(output));
        assertTrue(output.toString().contains("Enter Username: "));
    }

    @Test
    @DisplayName("Check library system for correct authentication error prompt")
    void RESP_03_Test_5() {
        StringWriter output = new StringWriter();
        system.promptAuthenticationError(new PrintWriter(output));
        assertTrue(output.toString().contains("Authentication Failed."));
    }

    @Test
    @DisplayName("Check library system for session established")
    void RESP_04_Test_1() {
        testAccount = new Account("TestUsername", "TestPassword");
        accounts.addAccount(testAccount);
        assertNotNull(system.establishSession(testAccount));
        assertEquals("TestUsername", system.getCurrAccount().getUsername());
    }

    @Test
    @DisplayName("System presents operations")
    void RESP_05_Test_1() {
        StringWriter output = new StringWriter();
        system.promptOperations(new PrintWriter(output));
        assertTrue(output.toString().contains("(1) Borrow a book. (2) Return a book. (3) Logout. "));
    }

    @ParameterizedTest
    @CsvSource({
            "1, Great Gatsby, F. Scott FitzGerald",
            "10, Brave New World, Aldous Huxley",
            "20, The Picture of Dorian Gray, Oscar Wilde"
    })
    @DisplayName("Check library system displays book collection")
    void RESP_06_Test_1(int index, String title, String author) {
        StringWriter output = new StringWriter();
        collection.displayCollection(new PrintWriter(output));
        String result = output.toString();

        // Testing that the display contains the 1st, 10th and 20th book
        assertTrue(result.contains(index + ". Title: " + title));
        assertTrue(result.contains("Author: " + author));
        assertTrue(result.contains("Status: AVAILABLE"));
    }

    @Test
    @DisplayName("Check library system displays selected book and borrowing details")
    void RESP_07_Test_1() {
        StringWriter output = new StringWriter();
        Book newBook1 = new Book("Title1", "Author1");
        system.promptBookConfirmation(newBook1, new PrintWriter(output));
        assertTrue(output.toString().contains("You selected Title1 by Author1. Enter (1) to proceed with borrowing. "));
    }

    @Test
    @DisplayName("Verify borrowing result for borrowing allowed")
    void RESP_08_Test_1() {
        testAccount = new Account("test1", "test2");
        Book book1 = new Book("Title1", "Author1");
        system.establishSession(testAccount);

        assertEquals(LibrarySystem.BorrowResult.BORROW_ALLOWED, system.verifyBorrowingAvailability(book1));
    }

    @Test
    @DisplayName("Verify borrowing result for book already on hold")
    void RESP_08_Test_2() {
        testAccount = new Account("test1", "test2");
        Book book1 = new Book("Title1", "Author1");
        testAccount.addBorrowedBook(book1);
        system.establishSession(testAccount);

        assertEquals(LibrarySystem.BorrowResult.ALREADY_BORROWED, system.verifyBorrowingAvailability(book1));
    }

    @Test
    @DisplayName("Verify borrowing result for book already checked out")
    void RESP_08_Test_3() {
        testAccount = new Account("test1", "test2");
        Book book1 = new Book("Title1", "Author1");
        book1.addHold(testAccount);
        system.establishSession(testAccount);

        assertEquals(LibrarySystem.BorrowResult.ALREADY_ON_HOLD, system.verifyBorrowingAvailability(book1));
    }

    @Test
    @DisplayName("Verify borrowing result for max books borrowed")
    void RESP_08_Test_4() {
        testAccount = new Account("test1", "test2");
        Book book1 = new Book("Title1", "Author1");
        Book book2 = new Book("Title1", "Author1");
        Book book3 = new Book("Title1", "Author1");
        Book book4 = new Book("Title1", "Author1");
        testAccount.addBorrowedBook(book1);
        testAccount.addBorrowedBook(book2);
        testAccount.addBorrowedBook(book3);
        system.establishSession(testAccount);

        assertEquals(LibrarySystem.BorrowResult.MAX_BOOKS_REACHED, system.verifyBorrowingAvailability(book4));
    }

    @ParameterizedTest
    @CsvSource({
            "ALREADY_BORROWED, You already have this book checked out.",
            "MAX_BOOKS_REACHED, You already have 3 books borrowed. This book will be placed on hold for you.",
            "ALREADY_ON_HOLD, You already have a hold on this book."
    })
    // Tests each of the extensions messages
    @DisplayName("Check library system displays correct borrowing message")
    void RESP_09_Test_1(LibrarySystem.BorrowResult result, String expectedMessage){
        StringWriter output = new StringWriter();
        system.displayBorrowingMessages(result, new PrintWriter(output));
        assertTrue(output.toString().contains(expectedMessage));
    }

    @Test
    @DisplayName("Check library system displays the number of borrowed books by the current user")
    void RESP_10_Test_1() {

        // Setup with test account and test borrowed books
        testAccount = new Account("test", "test");
        Book book1 = new Book("Title1", "Author1");
        Book book2 = new Book("Title1", "Author1");
        testAccount.addBorrowedBook(book1);
        testAccount.addBorrowedBook(book2);
        system.establishSession(testAccount);

        StringWriter output = new StringWriter();
        system.displayBookCount(new PrintWriter(output));

        assertTrue(output.toString().contains("2"));
    }

    @Test
    @DisplayName("Library system calculates book due date.")
    void RESP_11_Test_1() {
        assertTrue(system.calculateBookDueDate().contains("2025-12-25"));
    }
    @Test
    @DisplayName("Check Library system handles borrowing transaction")
    void RESP_12_Test_1(){
        Book testBook1 = new Book("test", "test");
        testAccount = new Account("test", "test");
        system.establishSession(testAccount);
        system.borrowBook(testBook1);

        // Asserts to check if borrowBook correctly records and update book status
        assertEquals(Book.BookStatus.CHECKED_OUT.name(), testBook1.getStatus());
        assertTrue(testAccount.isBookInList(testBook1));
    }
    @Test
    @DisplayName("Check library system records a book hold")
    void RESP_13_Test_1(){
        testAccount = new Account("test", "test");
        Book testBook1 = new Book("test", "test");
        system.establishSession(testAccount);
        system.holdBook(testBook1);

        assertTrue(testBook1.isAccountInQueue(testAccount));
    }
    @Test
    @DisplayName("Check for notification of available held books")
    void RESP_14_Test_1(){
        testAccount = new Account("test", "test");
        system.establishSession(testAccount);
        Book testBook1 = new Book("test", "test");
        collection.addBook(testBook1);
        testBook1.addHold(testAccount);
        StringWriter output = new StringWriter();

        system.notifyAvailableBooks(new PrintWriter(output));
        assertTrue(output.toString().contains("has become available for you!"));
    }
    @Test
    @DisplayName("Check for do not notify of available held books")
    void RESP_14_Test_2(){
        testAccount = new Account("test", "test");
        system.establishSession(testAccount);
        Book testBook1 = new Book("test", "test");
        collection.addBook(testBook1);
        testBook1.addHold(testAccount);
        testBook1.setStatus(Book.BookStatus.CHECKED_OUT);
        StringWriter output = new StringWriter();

        system.notifyAvailableBooks(new PrintWriter(output));
        assertTrue(output.toString().isBlank());
    }

    @Test
    @DisplayName("Check that library system provides borrowing confirmation")
    void RESP_15_Test_1(){
        Book testBook1 = new Book("testTitle", "testAuthor");
        StringWriter output = new StringWriter();
        system.promptBorrowingConfirmation(testBook1, new PrintWriter(output));
        assertTrue(output.toString().contains("You selected " + testBook1.getTitle() + " by " + testBook1.getAuthor() + ". The due date is " + system.calculateBookDueDate() + ". Enter (1) to confirm "));

    }

    @ParameterizedTest
    @CsvSource({
            "testTitle1, testAuthor1, 2025-12-25",
            "testTitle2, testAuthor2, 2025-12-25",
    })
    @DisplayName("check library system displays current account borrowed books and due dates")
    void RESP_16_Test_1(String title, String author, String dueDate){
        // Initialize and add book to account for testing
        testAccount = new Account("test", "test");
        system.establishSession(testAccount);
        Book book = new Book(title, author);
        testAccount.addBorrowedBook(book);

        StringWriter output = new StringWriter();
        system.displayBorrowedBooksWithDueDates(new PrintWriter(output));
        String result = "Title: " + title + " Author: " + author + " Due Date: " + dueDate;
        assertTrue(output.toString().contains(result));

    }

    @Test
    @DisplayName("Check library system handles no books to return")
    void RESP_17_Test_1(){
        // Scenario where a user has no books to return
        testAccount = new Account("test1", "test2");
        Book book1 = new Book("test", "test");
        system.establishSession(testAccount);

        assertEquals(LibrarySystem.ReturnResult.NO_BOOKS_TO_RETURN, system.returnBook(book1));
    }

    @Test
    @DisplayName("Check library system handles return with a hold")
    void RESP_17_Test_2(){
        // Scenario where a borrowed book is on hold
        testAccount = new Account("test1", "test2");
        Account testHoldAccount = new Account("test", "test");
        Book book1 = new Book("test", "test");
        testAccount.addBorrowedBook(book1);
        book1.addHold(testHoldAccount);
        system.establishSession(testAccount);

        assertEquals(LibrarySystem.ReturnResult.BOOK_ON_HOLD, system.returnBook(book1));
    }

    @Test
    @DisplayName("Check library system handles return with no hold")
    void RESP_17_Test_3(){
        testAccount = new Account("test1", "test2");
        Book book1 = new Book("test", "test");
        testAccount.addBorrowedBook(book1);
        system.establishSession(testAccount);

        assertEquals(LibrarySystem.ReturnResult.RETURN_ALLOWED, system.returnBook(book1));
    }


}