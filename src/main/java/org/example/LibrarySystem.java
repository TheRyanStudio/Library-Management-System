package org.example;

import java.io.PrintWriter;

public class LibrarySystem {
    private Account currAccount;


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
    }
}