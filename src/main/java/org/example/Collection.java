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
    public void displayCollection(PrintWriter output){

        // Iterates through and displays the collection of books
        for (int i=0; i<collection.size(); i++) {
            Book book = getBook(i);
            output.print(i +1 + ". Title: " + book.getTitle() + "Author: " + book.getAuthor() + "Status: " + book.getStatus());
        }
        output.flush();
    }
}
