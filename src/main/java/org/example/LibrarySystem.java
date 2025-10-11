package org.example;

import java.io.PrintWriter;

public class LibrarySystem {
    private AccountManager accounts;

    boolean authenticate(String username, String password) {
        // Checks each account for a matching username and password
        for (int i = 0; i < accounts.getAccountSize(); i++) {
            Account account = accounts.getAccount(i);
            if (account.getUsername().equals(username) && account.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    public void setAccounts(AccountManager accounts) {
        this.accounts = accounts;
    }

    public void promptForPassword(PrintWriter output){
        output.print("Enter Username: ");
        output.flush();
    }
    public void promptForUsername(PrintWriter output) {
        output.print("Enter Password: ");
        output.flush();
    }
}