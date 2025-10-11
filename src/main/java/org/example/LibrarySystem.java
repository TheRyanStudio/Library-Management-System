package org.example;

import java.io.PrintWriter;

public class LibrarySystem {
    private AccountManager accounts;
    private Account currAccount;

    public LibrarySystem(AccountManager accounts){
        this.accounts = accounts;
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
    public boolean login(String username, String password) {
        Account account = accounts.authenticate(username, password);
        currAccount = account;
        return account != null;
    }

}