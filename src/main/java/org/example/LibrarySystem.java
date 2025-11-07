package org.example;

import java.io.PrintWriter;

public class LibrarySystem {
    private Account currAccount;
    private Collection collection;

    public LibrarySystem(Collection collection){
        this.collection = collection;
    }

    // Enum values to track the result of borrowing a book
    public enum BorrowResult {
        BORROW_ALLOWED, MAX_BOOKS_REACHED, ALREADY_ON_HOLD, ALREADY_BORROWED, UNAVAILABLE
    }

    // Enum values to handle potential outcomes of returning a book
    public enum ReturnResult {
        BOOK_AVAILABLE, NO_BOOKS_TO_RETURN, BOOK_ON_HOLD
    }

    public void promptForPassword(PrintWriter output){
        output.print("Enter Password: ");
        output.flush();
    }
    public void promptForUsername(PrintWriter output) {
        output.print("Enter Username: ");
        output.flush();
    }
    public void displayAuthenticationError(PrintWriter output){
        output.println("Authentication Failed. Please Retry");
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
        // Check for the current account already in the holding queue, except for the next holder when the book is available for them to borrow
        if (book.isAccountInQueue(currAccount) && !((book.getNextHolder() == currAccount) && (book.getStatus() == Book.BookStatus.AVAILABLE))) {
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
        // Check for book is already checked out
        if (book.getStatus() == Book.BookStatus.CHECKED_OUT) {
            return BorrowResult.UNAVAILABLE;
        }
        // Check if the book is on hold by another account
        if (book.getStatus() == Book.BookStatus.AVAILABLE && book.getNextHolder() != null && !book.getNextHolder().equals(currAccount)) {
            return BorrowResult.UNAVAILABLE;
        }
        return BorrowResult.BORROW_ALLOWED;
    }

    // Messages for handling UC-02 extensions
    public void displayBorrowingMessages(BorrowResult result, PrintWriter output){
        switch (result){
            case ALREADY_ON_HOLD:
                output.println("You already have a hold on this book. ");
                break;
            case MAX_BOOKS_REACHED:
                output.println("You already have 3 books borrowed. This book will be placed on hold for you. ");
                break;
            case ALREADY_BORROWED:
                output.println("You already have this book checked out. ");
                break;
            case UNAVAILABLE:
                output.println("This book is unavailable. This book will be placed on hold for you. ");
                break;
            default:
                break;
        }
        output.flush();
    }

    public void displayBookCount(PrintWriter output){
        output.println("Your current book count is " + currAccount.getBorrowedBooks().size() + ".");
        output.flush();
    }

    public String calculateBookDueDate(){
        String currDate = "2025-12-11";
        return "2025-12-25";
    }
    public void borrowBook(Book book){
        if(book.getNextHolder() == currAccount)
            book.removeNextHolder();
        currAccount.addBorrowedBook(book);
        book.setStatus(Book.BookStatus.CHECKED_OUT);
    }

    public void holdBook(Book book){
        book.addHold(currAccount);
    }

    public void notifyAvailableBooks(PrintWriter output) {

        // Loop through the collection of books checking for the next holder to match the current account
        for (int i = 0; i < collection.getCollectionSize(); i++){
            Book curBook = collection.getBook(i);
            Account next = curBook.getNextHolder();

            // If the next holder matches the current account and the book status is available notify the current account
            if (next != null && next.equals(currAccount) && curBook.getStatus() == Book.BookStatus.AVAILABLE){
                output.println(curBook.getTitle() + " has become available for you!" );

            }
        }
        output.flush();
    }

    // Prompt confirmation message after account borrows a book
    public void promptBorrowingConfirmation(Book book, PrintWriter output){
        output.print("You selected " + book.getTitle() + " by " + book.getAuthor() + ". The due date is " + calculateBookDueDate() + ". Enter (1) to confirm ");
        output.flush();
    }

    public void displayBorrowedBooksWithDueDates(PrintWriter output){
    output.println("Your borrowed books are:");
        for (int i = 0; i < currAccount.getBorrowedBooks().size(); i++){
            Book book = currAccount.getBorrowedBooks().get(i);
            output.println(i+ 1 + ". Title: " + book.getTitle() + " Author: " + book.getAuthor() + " Due Date: " + calculateBookDueDate());
        }
    output.flush();
    }

    public ReturnResult returnBook(Book book){

        // Check whether there are no books to be returned
        if (currAccount.getBorrowedBooks().isEmpty()) {
            return ReturnResult.NO_BOOKS_TO_RETURN;
        }
        // Check whether there is a hold queue
        if (book.getNextHolder() != null){
            currAccount.removeBorrowedBook(book);
            book.setStatus(Book.BookStatus.AVAILABLE);
            return ReturnResult.BOOK_ON_HOLD;
        }
        // Normal return operation
        currAccount.removeBorrowedBook(book);
        book.setStatus(Book.BookStatus.AVAILABLE);
        return ReturnResult.BOOK_AVAILABLE;

    }
    public void displayReturnMessages(ReturnResult result, PrintWriter output){
        if (result == ReturnResult.NO_BOOKS_TO_RETURN){
            output.println("You have no books to return.");
            output.flush();
        }
    }
    // Prompt message after user returns a book
    public void promptReturnConfirmation(Book book, PrintWriter output){
        output.print("You would like to return " + book.getTitle() + " by " + book.getAuthor() + ". Enter (1) to confirm ");
        output.flush();
    }

    public void logout(PrintWriter output){
        currAccount = null; // Clear the current user
        output.println("You have logged out.");
        output.flush();
    }
}