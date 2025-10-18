package org.example;

import java.io.PrintWriter;

public class LibrarySystem {
    private Account currAccount;

    // Enum values to track the result of borrowing a book
    public enum BorrowResult {
        BORROW_ALLOWED, MAX_BOOKS_REACHED, ALREADY_ON_HOLD, ALREADY_BORROWED
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
        // Check for the current account already in the holding queue
        if (book.isAccountInQueue(currAccount)) {
            return BorrowResult.ALREADY_ON_HOLD;
        }
        // Check for max borrowed book size
        if (currAccount.getBorrowedBooks().size() >= 3) {
            return BorrowResult.MAX_BOOKS_REACHED;
        }
        // Check for the book already in the borrowers account
        if (currAccount.isBookInList(book)) {
            return BorrowResult.ALREADY_BORROWED;
        }
        return BorrowResult.BORROW_ALLOWED;
    }

    // Messages for handling UC-02 extensions
    public void displayBorrowingMessages(BorrowResult result, PrintWriter output){
        switch (result){
            case ALREADY_ON_HOLD:
                output.print("You already have a hold on this book. ");
                break;
            case MAX_BOOKS_REACHED:
                output.print("You already have 3 books borrowed. This book will be placed on hold for you. ");
                break;
            case ALREADY_BORROWED:
                output.print("You already have this book checked out. ");
                break;
            default:
                break;
        }
        output.flush();
    }

    public void displayBookCount(PrintWriter output){
        output.print("Your current book count is " + currAccount.getBorrowedBooks().size() + ".");
        output.flush();
    }

    public void calculateBookDueDate(PrintWriter output){
        String currDate = "2025-12-11";
        String dueDate = "2025-12-25";
        output.print(dueDate);
    }
}