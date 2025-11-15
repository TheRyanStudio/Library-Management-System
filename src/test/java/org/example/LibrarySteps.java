package org.example;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;

public class LibrarySteps {
    private AccountManager accounts;
    private Collection collection;
    private LibrarySystem system;
    private LibrarySystem.BorrowResult borrowResult;
    private LibrarySystem.ReturnResult returnResult;

    private Book gatsby;
    private Book harryPotter;
    private Book theHobbit;
    private Book animalFarm;
    private Book catcherInRye;


    private Account alice;
    private Account bob;
    private Account charlie;

    @Before
    public void setupLibrary(){

        InitializeLibrary library = new InitializeLibrary();
        collection = library.getCollection();
        system = new LibrarySystem(collection);

        // Create accounts to use for scenarios
        accounts = library.getAccounts();
        alice = accounts.getAccountByUsername("alice");
        bob = accounts.getAccountByUsername("bob");
        charlie = accounts.getAccountByUsername("charlie");

    }

    @Given("a library with a book titled The Great Gatsby")
    public void setLibraryWithGatsby(){
        gatsby = collection.getBookByTitle("The Great Gatsby");
    }

    @When("{string} logs in with password {string}")
    public void userLogsIn(String username, String password) {
        Account authenticated = accounts.authenticate(username, password);
        system.establishSession(authenticated);
    }

    @When("borrows {string}")
    public void userBorrowsBook(String bookTitle){
        Book currBook = collection.getBookByTitle(bookTitle);
        system.borrowBook(currBook);
    }

    @Then("The Great Gatsby is unavailable")
    public void gatsbyBookStatusUnavailable(){
        assertEquals(Book.BookStatus.CHECKED_OUT, gatsby.getStatus());
    }

    @Then("alice is the current borrower of The Great Gatsby")
    public void bookCurrentBorrower(){
        assertTrue(alice.isBookInList(gatsby));
    }

    @When("user logs out")
    public void userLogsOut(){
        system.logout();
    }

    @Then("bob is unable to borrow the book")
    public void bobUnableToBorrow(){
        assertEquals(LibrarySystem.BorrowResult.UNAVAILABLE, borrowResult);
    }

    @When("returns {string}")
    public void returnsBook(String bookTitle){
        Book currBook = collection.getBookByTitle(bookTitle);
        system.returnBook(currBook);
    }

    @Then("The Great Gatsby becomes available")
    public void gatsbyAvailable(){
        assertEquals(Book.BookStatus.AVAILABLE, gatsby.getStatus());
    }

    @When("charlie logs in")
    public void charlieLogsIn(){
        system.establishSession(charlie);
    }

    @And("A hold is placed on {string}")
    public void holdIsPlaced(String bookTitle) {
        Book currBook = collection.getBookByTitle(bookTitle);
        system.holdBook(currBook);
    }

    @Then("bob is the next holder in line")
    public void BobIsNextHolder() {
        assertSame(gatsby.getNextHolder(), bob);
    }

    @Then("charlie cannot borrow the book while bob is next in line")
    public void CharlieUnableToBorrow() {
        assertEquals(LibrarySystem.BorrowResult.ALREADY_ON_HOLD, borrowResult);
    }

    @Then("bob is notified that The Great Gatsby is available")
    public void bobIsNotified() {
        assertTrue(system.notifyAvailableBooks());
    }

    @Then("charlie is the next holder in line")
    public void charlieNextHolder() {
        assertSame(gatsby.getNextHolder(), charlie);
    }

    @Then("charlie is notified that The Great Gatsby is available")
    public void charlieIsNotified() {
        assertTrue(system.notifyAvailableBooks());
    }

    @Given("a library with book titles The Hobbit, Harry Potter, The Catcher in Rye and Animal Farm")
    public void setLibraryWith4Books(){
        theHobbit = collection.getBookByTitle("The Hobbit");
        harryPotter = collection.getBookByTitle("Harry Potter");
        catcherInRye = collection.getBookByTitle("The Catcher in Rye");
        animalFarm = collection.getBookByTitle("Animal Farm");
    }

    @When("attempts to borrow {string}")
    public void verifyBorrowingAbility(String bookTitle){
        Book currBook = collection.getBookByTitle(bookTitle);
        borrowResult = system.verifyBorrowingAvailability(currBook);
    }

    @Then("alice is at max books and unable to borrow another book")
    public void aliceUnableToBorrow(){
        assertEquals(LibrarySystem.BorrowResult.MAX_BOOKS_REACHED, borrowResult);
    }

    @Then("alice is in the holding line")
    public void aliceIsInTheHoldingLine() {
        assertTrue(animalFarm.isAccountInQueue(alice));
    }


    @Then("alice is notified that the Animal Farm is available")
    public void aliceIsNotified() {
        assertTrue(system.notifyAvailableBooks());
    }

    @When("attempts to return The Great Gatsby")
    public void attemptToReturnBook() {
        returnResult = system.returnBook(gatsby);
    }

    @Then("There is no books to return")
    public void noBooksToReturn() {
        assertEquals(LibrarySystem.ReturnResult.NO_BOOKS_TO_RETURN, returnResult);
    }

    @And("notified that there are no books to return")
    public void NotifyNoBooksToReturn() {
        String message = system.getReturnMessage(returnResult);
        assertEquals("You have no books to return.", message);
    }

    @Then("All books show as available")
    public void allBooksShowAsAvailable() {
        for (int i = 0; i < collection.getCollectionSize(); i++){
            Book book = collection.getBook(i);
            assertEquals(Book.BookStatus.AVAILABLE, book.getStatus());
        }

    }

}

