package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
    public static void main(String[] args) {

    }

    @Test
    @DisplayName("Check library collection size is 20")
    void RESP_01_Test_01() {
        InitializeLibrary library = new InitializeLibrary();
        Collection collection = library.initializeLibrary();
        int size = collection.getCollectionSize();
        assertEquals(20, size);
    }

    @Test
    @DisplayName("Check library collection for valid book - Great Gatsby")
    void RESP_01_Test_02() {
        InitializeLibrary library = new InitializeLibrary();
        Collection collection = library.initializeLibrary();
        Book book = collection.getBook(0);
        String title = book.getTitle();
        assertEquals("Great Gatsby",title);
    }

    @Test
    @DisplayName("Check book status for available")
    void RESP_01_Test_03(){
    InitializeLibrary library = new InitializeLibrary();
    Collection collection = library.initializeLibrary();
    Book book = collection.getBook(0);
    String status = book.getStatus();
        assertEquals("Available", status);
    }
}
