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
                        collection.displayCollection(output);

                        output.print("Enter book number to borrow: ");
                        output.flush();
                        int borrowIndex = Integer.parseInt(input.nextLine()) - 1;

                        Book selectedBook = collection.getBook(borrowIndex);
                        system.promptBookConfirmation(selectedBook, output);

                        String confirmBorrow = input.nextLine().trim();
                        if(!confirmBorrow.equals("1")) break;

                        // Verifies to see if the account can borrow this book
                        LibrarySystem.BorrowResult result = system.verifyBorrowingAvailability(selectedBook);

                            // Handles extensions for borrowing a book
                        if (result != LibrarySystem.BorrowResult.BORROW_ALLOWED) {
                            system.displayBorrowingMessages(result, output);
                            if (result == LibrarySystem.BorrowResult.MAX_BOOKS_REACHED || result == LibrarySystem.BorrowResult.UNAVAILABLE) {
                                system.holdBook(selectedBook);
                                output.println("Hold placed successfully.");
                            }
                            break;
                        }




                }
            }

        }

    }
}