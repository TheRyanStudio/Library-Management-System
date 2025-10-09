package org.example;

public class Book {
    String title;
    String author;
    String status;

    Book(String title, String author){
        this.title = title;
        this.author = author;
        this.status = "Available";
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }
}
