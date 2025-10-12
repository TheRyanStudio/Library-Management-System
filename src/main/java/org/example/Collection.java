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
    }
}
