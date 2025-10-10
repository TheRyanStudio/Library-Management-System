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

    public boolean isValidUsername() {
        return (username != null && !username.isEmpty());
    }
    public boolean isValidPassword() {
        return (password != null && !password.isEmpty());
    }

    public List<Book> getBorrowedBooks(){
        return borrowedBooks;
    }
}
