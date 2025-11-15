# COMP4004 Library Management System Assignment #1 – TDD 
**By Ryan Johnson 101217600**   

**Files:** `Account.java`, `AccountManager.java`, `Book.java`, `Collection.java`, `InitializeLibrary.java`, `LibrarySystem.java`, `Main.java`  
**Test Files:** `AcceptanceTest.java`, `LibraryTest.java`  

---

### System Functionality
- User authentication with username/password  
- Book collection display  
- Single book borrowing with eligibility validation  
- Single book return processing  
- Single book hold placement  
- Real-time book availability tracking  
- Session management  
- Text-based system  

### Borrowing Rules
- Single-item transactions only  
- Maximum 3 books per borrower  
- 14-day borrowing period (YYYY-MM-DD format)  
- Only available books can be borrowed  
- No renewals  

### Hold System Rules
- FIFO hold queue  
- One hold per borrower per book  
- Holds only on unavailable books  
- Books on hold cannot be borrowed by others  
- Notifications sent when books become available  
- Borrowers at 3-book limit may place holds but cannot borrow  

### System Initialization
- Starts with 20 books and 3 borrower accounts  
- Data is in-memory only; resets on application restart  

---

## Responsibilities and TDD Cycle

### RESP-01: System book collection initialization
- Ensure collection is created with 20 unique books  
- Unit tests validate initial availability and book data  

### RESP-02: System user accounts initialization
- Ensure 3 borrower accounts created  
- Unit tests validate usernames, passwords, and zero borrowed books  

### RESP-03: User Authentication
- Validate username/password and establish session  
- Tests include valid and invalid credentials  

### RESP-04: Borrow Book Eligibility
- Check if borrower has < 3 books  
- Validate selected book is available  
- Test borrowing transaction updates borrower and book status  

### RESP-05: Place Hold on Book
- Allow placing a hold if book unavailable  
- Enforce one hold per borrower per book  
- Notify correct borrower when book becomes available  

### RESP-06: Return Book
- Update book status to Available or On Hold  
- Remove book from borrower account  
- Notify next borrower in queue if hold exists  

### RESP-07: Session Management
- Maintain current user session until logout  
- Clear session on logout  
- Tests include login → operations → logout workflow  

### RESP-08: Acceptance Tests
- **A-TEST-01:** Multi-User Borrow and Return with Availability Validated  
  - Path: UC-01 → UC-02 → UC-04 → UC-01 → UC-02 → UC-04 → UC-01 → UC-03 → UC-04 → UC-01 → UC-02 → UC-04  
  - Scenario: User1 borrows a book, User2 sees it checked out, User1 returns it, User2 sees availability  

- **A-TEST-02:** Initialization and Authentication with Error Handling  
  - Path: UC-01  
  - Scenario: Valid login succeeds, invalid login rejected with error message and retry prompt 
