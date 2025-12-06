const express = require('express');
const app = express();
const PORT = 3000;

app.use(express.json());
app.use(express.static('public'));

function resetLibrary() {
    currAccount = null;
    accountManager.accounts.forEach(acc => acc.borrowedBooks = []);
    collection.books.forEach(book => {
        book.status = 'AVAILABLE';
        book.holdQueue = [];
    });
}

// --- In-memory Data Store ---
let currAccount = null;

// --- Account class ---
class Account {
    constructor(username, password) {
        this.username = username;
        this.password = password;
        this.borrowedBooks = [];
    }

    isBookInList(book) {
        return this.borrowedBooks.includes(book);
    }

    addBorrowedBook(book) {
        this.borrowedBooks.push(book);
    }

    removeBorrowedBook(book) {
        this.borrowedBooks = this.borrowedBooks.filter(b => b !== book);
    }
}

// --- Book class ---
class Book {
    constructor(title, author) {
        this.title = title;
        this.author = author;
        this.status = 'AVAILABLE'; // AVAILABLE, CHECKED_OUT
        this.holdQueue = [];
    }

    addHold(account) {
        if (!this.holdQueue.includes(account)) this.holdQueue.push(account);
    }

    isAccountInQueue(account) {
        return this.holdQueue.includes(account);
    }

    getNextHolder() {
        return this.holdQueue.length > 0 ? this.holdQueue[0] : null;
    }

    removeNextHolder() {
        this.holdQueue.shift();
    }

    getStatusForUser(account) {
        if (this.holdQueue.length > 0) {
            if (this.getNextHolder() === account && this.status === 'AVAILABLE') return 'Available';
            else return 'On Hold';
        }
        return this.status === 'CHECKED_OUT' ? 'Checked Out' : 'Available';
    }
}

// --- Collection class ---
class Collection {
    constructor() {
        this.books = [];
    }

    addBook(book) {
        this.books.push(book);
    }

    getBook(index) {
        return this.books[index];
    }

    getBookByTitle(title) {
        return this.books.find(b => b.title === title) || null;
    }

    getCollectionForUser(account) {
        return this.books.map(book => ({
            title: book.title,
            author: book.author,
            status: book.getStatusForUser(account)
        }));
    }
}

// --- AccountManager class ---
class AccountManager {
    constructor() {
        this.accounts = [];
    }

    addAccount(account) {
        this.accounts.push(account);
    }

    authenticate(username, password) {
        return this.accounts.find(acc => acc.username === username && acc.password === password) || null;
    }
}

// --- Initialize Library ---
const collection = new Collection();
const accountManager = new AccountManager();

// Add 20 books
[
    ["The Great Gatsby", "F. Scott Fitzgerald"],
    ["To Kill a Mockingbird", "Harper Lee"],
    ["1984", "George Orwell"],
    ["Pride and Prejudice", "Jane Austen"],
    ["The Hobbit", "J.R.R. Tolkien"],
    ["Harry Potter", "J. K. Rowling"],
    ["The Catcher in the Rye", "J.D. Salinger"],
    ["Animal Farm", "George Orwell"],
    ["Lord of the Flies", "William Golding"],
    ["Jane Eyre", "Charlotte Brontë"],
    ["Wuthering Heights", "Emily Brontë"],
    ["Moby Dick", "Herman Melville"],
    ["The Odyssey", "Homer"],
    ["Hamlet", "William Shakespeare"],
    ["War and Peace", "Leo Tolstoy"],
    ["The Divine Comedy", "Dante Alighieri"],
    ["Crime and Punishment", "Fyodor Dostoevsky"],
    ["Don Quixote", "Miguel de Cervantes"],
    ["The Iliad", "Homer"],
    ["Ulysses", "James Joyce"]
].forEach(([title, author]) => collection.addBook(new Book(title, author)));

// Add 3 accounts
accountManager.addAccount(new Account("alice", "pass123"));
accountManager.addAccount(new Account("bob", "pass456"));
accountManager.addAccount(new Account("charlie", "pass789"));

// --- Library System logic ---
const verifyBorrowingAvailability = (book, account) => {
    if (account.isBookInList(book)) return 'ALREADY_BORROWED';
    if (account.borrowedBooks.length >= 3) return 'MAX_BOOKS_REACHED';
    if (book.status === 'CHECKED_OUT') return 'UNAVAILABLE';
    if (book.isAccountInQueue(account) && book.getNextHolder() !== account) return 'ALREADY_ON_HOLD';
    if (book.status === 'AVAILABLE' && book.getNextHolder() && book.getNextHolder() !== account) return 'UNAVAILABLE';
    return 'BORROW_ALLOWED';
};

const borrowBook = (book, account) => {
    if (book.getNextHolder() === account) book.removeNextHolder();
    account.addBorrowedBook(book);
    book.status = 'CHECKED_OUT';
};

const holdBook = (book, account) => {
    if (!book.isAccountInQueue(account)) book.addHold(account);
};

const returnBook = (book, account) => {
    if (!account.isBookInList(book)) return { success: false, message: 'You have no books to return.' };
    account.removeBorrowedBook(book);
    book.status = book.holdQueue.length > 0 ? 'AVAILABLE' : 'AVAILABLE';
    return { success: true, message: 'Book returned successfully.' };
};

const notifyAvailableBooks = account => {
    return collection.books.filter(book => {
        return book.getNextHolder() === account && book.status === 'AVAILABLE' &&
            verifyBorrowingAvailability(book, account) === 'BORROW_ALLOWED';
    });
};

// --- REST Endpoints ---

app.post('/api/login', (req, res) => {
    const { username, password } = req.body;
    const account = accountManager.authenticate(username, password);
    if (account) {
        currAccount = account;
        const notifications = notifyAvailableBooks(account).map(b => ({ title: b.title, author: b.author }));
        res.json({ success: true, username: account.username, notifications });
    } else {
        res.status(401).json({ error: 'Authentication Failed' });
    }
});

app.post('/api/logout', (req, res) => {
    currAccount = null;
    res.json({ success: true });
});

app.get('/api/user', (req, res) => {
    if (currAccount) {
        res.json({
            username: currAccount.username,
            borrowedBooks: currAccount.borrowedBooks,
        });
    } else {
        res.status(404).json({ error: 'No user logged in' });
    }
});

app.get('/api/books', (req, res) => {
    if (!currAccount) return res.status(401).json({ error: 'Not authenticated' });
    res.json(collection.getCollectionForUser(currAccount));
});

app.post('/api/borrow', (req, res) => {
    if (!currAccount) return res.status(401).json({ error: 'Not authenticated' });
    const { title } = req.body;
    const book = collection.getBookByTitle(title);
    if (!book) return res.status(404).json({ error: 'Book not found' });

    const result = verifyBorrowingAvailability(book, currAccount);
    if (result === 'BORROW_ALLOWED') {
        borrowBook(book, currAccount);
        return res.json({ success: true, reason: result });
    } else if (result === 'MAX_BOOKS_REACHED' || result === 'UNAVAILABLE') {
        holdBook(book, currAccount);
        return res.json({ success: false, reason: result, holdPlaced: true });
    } else {
        return res.json({ success: false, reason: result });
    }
});

app.post('/api/return', (req, res) => {
    if (!currAccount) return res.status(401).json({ error: 'Not authenticated' });
    const { title } = req.body;
    const book = collection.getBookByTitle(title);
    if (!book) return res.status(404).json({ error: 'Book not found' });

    const result = returnBook(book, currAccount);
    res.json(result);
});

app.post('/api/place-hold', (req, res) => {
    if (!currAccount) return res.status(401).json({ error: 'Not authenticated' });
    const { title } = req.body;
    const book = collection.getBookByTitle(title);
    if (!book) return res.status(404).json({ error: 'Book not found' });

    holdBook(book, currAccount);
    res.json({ success: true });
});

app.get('/api/notifications', (req, res) => {
    if (!currAccount) return res.status(401).json({ error: 'Not authenticated' });
    const notifications = notifyAvailableBooks(currAccount).map(b => ({ title: b.title, author: b.author }));
    res.json({ notifications });
});

// --- Reset endpoint for testing ---
app.post('/api/reset', (req, res) => {
    // Clear current logged-in account
    currAccount = null;
    resetLibrary();

    // Reset borrowed books for all accounts
    accountManager.accounts.forEach(acc => {
        acc.borrowedBooks = [];
    });

    // Reset all books to AVAILABLE and clear hold queues
    collection.books.forEach(book => {
        book.status = 'AVAILABLE';
        book.holdQueue = [];
    });

    res.json({ success: true });
});


// --- Start server ---
app.listen(PORT, () => console.log(`Server running at http://localhost:${PORT}`));
