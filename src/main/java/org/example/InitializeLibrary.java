package org.example;

public class InitializeLibrary {
    Collection collection = new Collection();

    public Collection initializeLibrary(){
        collection.addBook(new Book("Great Gatsby", "F. Scott FitzGerald"));
        collection.addBook(new Book("To Kill a Mockingbird", "Harper Lee"));
        collection.addBook(new Book("1984", "George Orwell"));
        collection.addBook(new Book("Pride and Prejudice", "Jane Austen"));
        collection.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
        collection.addBook(new Book("Moby Dick", "Herman Melville"));
        collection.addBook(new Book("War and Peace", "Leo Tolstoy"));
        collection.addBook(new Book("The Catcher in the Rye", "J.D. Salinger"));
        collection.addBook(new Book("The Hobbit", "J.R.R. Tolkien"));
        collection.addBook(new Book("Brave New World", "Aldous Huxley"));
        collection.addBook(new Book("Crime and Punishment", "Fyodor Dostoevsky"));
        collection.addBook(new Book("Jane Eyre", "Charlotte Brontë"));
        collection.addBook(new Book("The Odyssey", "Homer"));
        collection.addBook(new Book("The Brothers Karamazov", "Fyodor Dostoevsky"));
        collection.addBook(new Book("Great Expectations", "Charles Dickens"));
        collection.addBook(new Book("Fahrenheit 451", "Ray Bradbury"));
        collection.addBook(new Book("Wuthering Heights", "Emily Brontë"));
        collection.addBook(new Book("The Lord of the Rings", "J.R.R. Tolkien"));
        collection.addBook(new Book("Anna Karenina", "Leo Tolstoy"));
        collection.addBook(new Book("The Picture of Dorian Gray", "Oscar Wilde"));
        return collection;
    }

}

