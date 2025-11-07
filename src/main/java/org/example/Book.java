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
        AVAILABLE, CHECKED_OUT
    }

    Book(String title, String author){
        this.title = title;
        this.author = author;
        this.status = BookStatus.AVAILABLE;
    }

    public String getTitle() {
        return title;
    }

    public BookStatus getStatus() {
        return status;
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

    public Account getNextHolder(){
        return holdQueue.peek();
    }
    public void removeNextHolder(){
        holdQueue.poll();
    }

    // Gets the status of a book for a specific user
    public String getStatusForUser(Account account) {
        if (!holdQueue.isEmpty()){
            if(getNextHolder() == account && status == BookStatus.AVAILABLE){
                return "Available";
            } else{
                return "On Hold";
            }
        } else if (status == BookStatus.CHECKED_OUT){
            return "Checked Out";
        } else {
            return "Available";
        }
    }
}

