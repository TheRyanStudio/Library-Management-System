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
}
