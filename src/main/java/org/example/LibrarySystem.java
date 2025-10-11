package org.example;

import java.io.PrintWriter;

public class LibrarySystem {

    public void promptForPassword(PrintWriter output){
        output.print("Enter Username: ");
        output.flush();
    }
    public void promptForUsername(PrintWriter output) {
        output.print("Enter Password: ");
        output.flush();
    }
    public void promptAuthenticationError(PrintWriter output){
        output.print("Authentication Failed.");
        output.flush();
    }
}