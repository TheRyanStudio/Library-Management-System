package org.example;

import java.io.PrintWriter;
import java.util.ArrayList;

public class Collection {
    ArrayList<Book> collection;

    public Collection(){
        collection = new ArrayList<>();
    }

    public int getCollectionSize(){
        return collection.size();
    }

    public void addBook(Book book){
        collection.add(book);
    }

    Book getBook(int index){
        return collection.get(index);
    }
    public void displayCollection(Account account, PrintWriter output){

        // Iterates through and displays the collection of books
        for (int i=0; i<collection.size(); i++) {
            Book book = getBook(i);

            // Checks if the book is available for the next holder
            if (book.getNextHolder() != null && book.getNextHolder().equals(account)){
                output.println(i +1 + ". Title: " + book.getTitle() + " Author: " + book.getAuthor() + " Status: " + "AVAILABLE");
                continue;
            }
            output.println(i +1 + ". Title: " + book.getTitle() + " Author: " + book.getAuthor() + " Status: " + book.getStatus());
        }
        output.flush();
    }
}
