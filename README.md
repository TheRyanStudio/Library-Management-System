# COMP4004 Library Management System Assignment #1 – TDD  
**By Ryan Johnson 101217600**  

**Files:** `Account.java`, `AccountManager.java`, `Book.java`, `Collection.java`, `InitializeLibrary.java`, `LibrarySystem.java`, `Main.java`  
**Test Files:** `UnitTest.java`, `AcceptanceTest.java`  

---

## TDD Testing

### 1. RESP-01_system_initialization
Test that the system initializes correctly with the predefined books and users, demonstrating that:  
- 20 books are created with unique titles and authors  
- 3 borrower accounts are created with usernames and passwords  
- All books are initially available  
- All borrowers have zero books  

**Assert:** System initialization is correct  
- Number of books and borrowers is asserted  
- Book availability is asserted  
- Borrower accounts start with zero books  

---

### 2. RESP-02_user_authentication
Test that users can log in correctly, demonstrating that:  
- Valid username/password allows login  
- Invalid username/password is rejected  
- Session is established upon successful login  
- Notifications for held books are displayed  

**Assert:** Authentication behaves correctly  
- Successful login sets session correctly  
- Invalid login prompts retry  
- Held book notifications are asserted  

---

### 3. RESP-03_borrow_book
Test borrowing a single available book, demonstrating that:  
- Borrower can borrow a book if under 3-book limit  
- Book availability updates to Checked Out  
- Borrower account updates correctly  
- Due date is calculated (14 days)  

**Assert:** Borrowing behaves correctly  
- Borrower’s book count increases  
- Book status updates are asserted  
- Borrowing confirmation includes correct due date  

---

### 4. RESP-04_place_hold
Test placing a hold on unavailable books, demonstrating that:  
- Borrower can place hold if book is checked out  
- Only one hold per borrower per book  
- FIFO queue ordering is maintained  
- Notifications sent when book becomes available  

**Assert:** Hold system behaves correctly  
- Holds are recorded and queued correctly  
- Only notified borrower can borrow reserved book  
- Queue advances properly when books are returned  

---

### 5. RESP-05_borrowing_limit_interactions
Test interactions between borrowing limits and holds, demonstrating that:  
- Users at 3-book limit cannot borrow more  
- Users at limit can still place holds  
- Borrower’s capacity increases after returning a book  
- Notifications are sent when held books become available  

**Assert:** Borrowing limit enforcement is correct  
- Borrowing is blocked at limit  
- Holds are allowed at limit  
- Borrowing capacity updates after return  
- Notification to next holder is asserted  

---

### 6. RESP-06_return_book
Test returning a book, demonstrating that:  
- Book status updates to Available or On Hold  
- Borrower account updates correctly  
- Hold notifications are sent to the next borrower  

**Assert:** Returns behave correctly  
- Returned book availability is asserted  
- Borrower account updated correctly  
- Notifications to next holder are asserted  

---

### 7. RESP-07_no_books_borrowed_scenario
Test system behaviour when borrower has no books, demonstrating that:  
- System handles return operation with empty borrowed list  
- No errors occur when trying to return nothing  
- Book availability remains correct  

**Assert:** System handles empty returns correctly  
- Attempted return of no books is handled gracefully  
- Book statuses remain correct  
