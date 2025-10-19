package org.example;

import java.util.List;
import java.util.ArrayList;

public class Account {
    String username;
    String password;
    List<Book> borrowedBooks; // Each account tracks its borrowed books

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        // Initialize borrowed books to 0
        borrowedBooks = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isValidUsername() {
        return (username != null && !username.isEmpty());
    }
    public boolean isValidPassword() {
        return (password != null && !password.isEmpty());
    }

    public List<Book> getBorrowedBooks(){
        return borrowedBooks;
    }

    public void addBorrowedBook(Book book){
        borrowedBooks.add(book);
    }

    // Checks to see if a book is in the borrowed list
    public boolean isBookInList(Book book){
        return borrowedBooks.contains(book);
    }

    public void removeBorrowedBook(Book book){
        borrowedBooks.remove(book);
    }

}
