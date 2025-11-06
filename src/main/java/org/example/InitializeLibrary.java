package org.example;

public class InitializeLibrary {
    private Collection collection;
    private AccountManager accounts;

    public InitializeLibrary(){
        collection = new Collection();
        accounts = new AccountManager();

        //Initialize the collection of books
        collection.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
        collection.addBook(new Book("To Kill a Mockingbird", "Harper Lee"));
        collection.addBook(new Book("1984", "George Orwell"));
        collection.addBook(new Book("Pride and Prejudice", "Jane Austen"));
        collection.addBook(new Book("The Hobbit", "J.R.R. Tolkien"));
        collection.addBook(new Book("Harry Potter", "J. K. Rowling"));
        collection.addBook(new Book("The Catcher in the Rye", "J.D. Salinger"));
        collection.addBook(new Book("Animal Farm", "George Orwell"));
        collection.addBook(new Book("Lord of the Flies", "William Golding"));
        collection.addBook(new Book("Jane Eyre", "Charlotte Brontë"));
        collection.addBook(new Book("Wuthering Heights", "Emily Brontë"));
        collection.addBook(new Book("Moby Dick", "Herman Melville"));
        collection.addBook(new Book("The Odyssey", "Homer"));
        collection.addBook(new Book("Hamlet", "William Shakespeare"));
        collection.addBook(new Book("War and Peace", "Leo Tolstoy"));
        collection.addBook(new Book("The Divine Comedy", "Dante Alighieri"));
        collection.addBook(new Book("Crime and Punishment", "Fyodor Dostoevsky"));
        collection.addBook(new Book("Don Quixote", "Miguel de Cervantes"));
        collection.addBook(new Book("The Iliad", "Homer"));
        collection.addBook(new Book("Ulysses", "James Joyce"));

        // Initialize the accounts
        accounts.addAccount(new Account("alice", "pass123"));
        accounts.addAccount(new Account("bob", "pass456"));
        accounts.addAccount(new Account("charlie", "pass789"));
    }

    public Collection getCollection() {
        return collection;
    }

    public AccountManager getAccounts() {
        return accounts;
    }
}

