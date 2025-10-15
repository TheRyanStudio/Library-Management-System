package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class Book {
    String title;
    String author;
    BookStatus status;
    Queue<Account> holdQueue = new LinkedList<>();

    // Enum values to keep track of a books status
    public enum BookStatus {
        AVAILABLE, CHECKED_OUT, ON_HOLD
    }

    Book(String title, String author){
        this.title = title;
        this.author = author;
        this.status = BookStatus.AVAILABLE;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status.name();
    }

    void setStatus(BookStatus status){
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    // Adds account to the holding queue
    public void addHold(Account account){
        holdQueue.add(account);
    }

    // Checks to see if account is in holding queue
    public boolean isAccountInQueue(Account account){
        return holdQueue.contains(account);
    }
}
