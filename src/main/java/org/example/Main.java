package org.example;

import java.io.PrintWriter;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Initialize Library and Accounts
        InitializeLibrary library = new InitializeLibrary();
        Collection collection = library.getCollection();
        AccountManager accounts = library.getAccounts();
        LibrarySystem system = new LibrarySystem(collection);


        PrintWriter output = new PrintWriter(System.out, true);
        Scanner input = new Scanner(System.in);

        output.println("Welcome to the Library Management System.");


        while (true) {

            // Login stage
            system.promptForUsername(output);
            String username = input.nextLine().trim();

            system.promptForPassword(output);
            String password = input.nextLine().trim();
            Account authenticated = accounts.authenticate(username, password);

            if (authenticated == null) {
                system.displayAuthenticationError(output);
                continue; // retry logging in
            }

            // Establish session
            system.establishSession(authenticated);
            output.println("Login successful. Welcome " + authenticated.getUsername() + "!");
            system.notifyAvailableBooks(output);

            boolean activeSession = true;
            while (activeSession){
                system.promptOperations(output);
                String choice = input.nextLine().trim();

                switch(choice) {

                    // User borrows a book
                    case "1":
                        system.displayBookCount(output);
                }
            }

        }

    }
}