# COMP4004 Library Management System Assignment #1  
**By Ryan Johnson 101217600**  

**Files:** `Account.java`, `AccountManager.java`, `Book.java`, `Collection.java`, `InitializeLibrary.java`, `LibrarySystem.java`, `Main.java`  
**Test Files:** `AcceptanceTest.java`, `LibraryTest.java`  

---

## BDD Testing

### 1. A1_scenario
Test the basic borrow-return cycle with two users and one book, demonstrating that:  
- A borrowed book becomes unavailable to other users  
- A returned book becomes available again  
- Only one user can have a book at a time  

**Assert:** Availability changes and borrow attempts behave correctly  
- Book availability is asserted after borrow  
- The inability to borrow an unavailable book is asserted  
- Book availability is correctly asserted after return  

---

### 2. multiple_holds_queue_processing
Test the hold queue system with three users competing for the same book, demonstrating that:  
- Users can place holds on unavailable books  
- Hold queue follows FIFO ordering  
- Notifications are sent to the correct user when book becomes available  
- Only the notified user can borrow the reserved book  
- Queue advances properly when reserved books are borrowed or returned  

**Assert:** Hold-queue order and notifications are correct  
- Hold queue FIFO ordering is correctly asserted  
- Notifications to correct users are asserted  
- Only notified user can borrow reserved book is asserted  
- Queue advances properly when books are borrowed/returned  

---

### 3. borrowing_limit_and_hold_interactions
Test the interaction between borrowing limits and holds, demonstrating that:  
- Users cannot exceed the 3-book borrowing limit  
- Users can place holds even when at the borrowing limit (i.e., when they already have 3 books borrowed)  
- When a user at the borrowing limit returns a book, they drop below the limit  
- If that user is next in the hold queue for a book, they receive a notification that the book is now available for them to borrow  

**Assert:** Users can't exceed 3 books, can place holds at the limit, gain borrowing capacity after returns, and receive notifications when their held books are returned by others (regardless of their current borrowing capacity)  
- 3-book borrowing limit is enforced and asserted  
- Users can place holds at borrowing limit is asserted  
- Borrowing capacity increases after return is asserted  
- Notification received when held book available is asserted  

---

### 4. no_books_borrowed_scenario
Test system behaviour when users have no borrowed books, demonstrating that:  
- System correctly handles return operations with an empty borrowed list  
- All books show as available when none are borrowed  
- Multiple users with no books borrowed are handled correctly  

**Assert:** System correctly reports when users have no borrowed books  
- System correctly handles return with empty borrowed list is asserted  
- All books show as available when none borrowed is asserted  
