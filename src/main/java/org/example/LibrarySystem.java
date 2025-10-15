package org.example;

import java.io.PrintWriter;

public class LibrarySystem {
    private Account currAccount;

    // Enum values to track the result of borrowing a book
    public enum BorrowResult {
        SUCCESS, MAX_BOOKS_REACHED, ALREADY_ON_HOLD, ALREADY_BORROWED
    }

    public void promptForPassword(PrintWriter output){
        output.print("Enter Password: ");
        output.flush();
    }
    public void promptForUsername(PrintWriter output) {
        output.print("Enter Username: ");
        output.flush();
    }
    public void promptAuthenticationError(PrintWriter output){
        output.print("Authentication Failed.");
        output.flush();
    }
    public Account establishSession(Account account) {
        currAccount = account;
        return currAccount;
    }
    public Account getCurrAccount(){
        return currAccount;
    }
    public void promptOperations(PrintWriter output){
        output.print("(1) Borrow a book. (2) Return a book. (3) Logout. ");
        output.flush();
    }
    public void promptBookConfirmation(Book book, PrintWriter output){
        output.print("You selected " + book.getTitle() + " by " + book.getAuthor() + ". Enter (1) to proceed with borrowing. ");
        output.flush();
    }

    // Returns the corresponding borrowing book result
    public BorrowResult verifyBorrowingAvailability(Book book) {
        // Check for max borrowed book size
        if (currAccount.getBorrowedBooks().size() >= 3) {
            return BorrowResult.MAX_BOOKS_REACHED;
        }
        // Check for the current account already in the holding queue
        if (book.isAccountInQueue(currAccount)) {
            return BorrowResult.ALREADY_ON_HOLD;
        }
        // Check for the book already in the borrowers account
        if (currAccount.isBookInList(book)) {
            return BorrowResult.ALREADY_BORROWED;
        }
        return BorrowResult.SUCCESS;
    }
}