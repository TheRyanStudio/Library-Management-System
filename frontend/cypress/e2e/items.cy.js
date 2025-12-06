describe('Library Book Management', () => {
    const bookTitle = 'The Great Gatsby';

    beforeEach(() => {
        // Reset server state before each test
        cy.request('POST', 'http://localhost:3000/api/reset');
        cy.intercept('GET', '/api/books').as('getBooks');
        cy.visit('http://localhost:3000');
    });

    it('should allow two users to borrow and return a book correctly', () => {
        // Alice logs in
        cy.get('#username').type('alice');
        cy.get('#password').type('pass123');
        cy.get('#loginButton').click();

        // Assertion: checks correct url handling
        cy.url().should('eq', 'http://localhost:3000/dashboard.html');

        // Alice borrows a book
        cy.contains('td', bookTitle)
            .parent('tr')
            .within(() => {
                cy.get('.borrowButton').click();
            });

        // Alice logs out
        cy.get('#logoutButton').click();

        cy.url().should('eq', 'http://localhost:3000/index.html');

        // Bob logs in
        cy.get('#username').type('bob');
        cy.get('#password').type('pass456');
        cy.get('#loginButton').click();

        cy.url().should('eq', 'http://localhost:3000/dashboard.html');

        // Assertion: Checking that book should now show as Checked Out
        // Demonstrates that A borrowed book becomes unavailable to other users
        cy.contains('td', bookTitle)
            .parent('tr')
            .find('.status')
            .should('have.text', 'Checked Out');

        // Alice logs back in
        cy.get('#logoutButton').click();
        cy.get('#username').type('alice');
        cy.get('#password').type('pass123');
        cy.get('#loginButton').click();

        // Alice returns the book
        cy.get('#booksTable')
            .contains('td', bookTitle)
            .parent('tr')
            .find('.returnButton')
            .click();

        // Bob logs back in
        cy.get('#logoutButton').click();
        cy.get('#username').type('bob');
        cy.get('#password').type('pass456');
        cy.get('#loginButton').click();

        // Assertion: Checking that book should now show as Available
        // Demonstrates after returning a book it becomes available to other users
        cy.contains('td', bookTitle)
            .parent('tr')
            .find('.status')
            .should('have.text', 'Available');
    });

    it('should process multiple holds in correct order', () => {
        const bookTitle = 'The Great Gatsby';
        const users = [{
                username: 'alice',
                password: 'pass123'
            },
            {
                username: 'bob',
                password: 'pass456'
            },
            {
                username: 'charlie',
                password: 'pass789'
            }
        ];

        // Helper to login
        const login = (user) => {
            cy.get('#username').clear().type(user.username);
            cy.get('#password').clear().type(user.password);
            cy.get('#loginButton').click();
        };

        // Helper to logout
        const logout = () => {
            cy.get('#logoutButton').click();
        };

        // Alice borrows the book
        login(users[0]);
        cy.contains('td', bookTitle)
            .parent('tr')
            .within(() => {
                cy.get('.borrowButton').click();
            });
        logout();

        // Bob places a hold
        login(users[1]);
        cy.contains('td', bookTitle)
            .parent('tr')
            .within(() => {
                cy.get('.holdButton').click();
            });
        logout();

        // Charlie places a hold
        login(users[2]);
        cy.contains('td', bookTitle)
            .parent('tr')
            .within(() => {
                cy.get('.holdButton').click();
            });
        logout();

        // Alice returns the book
        login(users[0]);
        cy.get('#booksTable')
            .contains('td', bookTitle)
            .parent('tr')
            .find('.returnButton')
            .click();
        logout();

        // Assertion: Bob sees the notification that the book is now available
        // Demonstrates that Bob is next in line properly advancing the queue
        login(users[1]);
        cy.get('#notificationList')
            .should('contain.text', `"${bookTitle}" by F. Scott Fitzgerald is now available`);

        // Bob borrows the book
        cy.contains('td', bookTitle)
            .parent('tr')
            .within(() => {
                cy.get('.borrowButton').click();
            });
        logout();

        // Bob returns the book, making Charlie next
        login(users[1]);
        cy.get('#booksTable')
            .contains('td', bookTitle)
            .parent('tr')
            .find('.returnButton')
            .click();
        logout();

        // Assertion: Charlie sees the notification that the book is now available
        // Demonstrates that Charlie is next in line properly advancing the queue
        login(users[2]);
        cy.get('#notificationList')
            .should('contain.text', `"${bookTitle}" by F. Scott Fitzgerald is now available`);
    });

    it('should handle borrowing limit and hold interactions correctly', () => {
        const bookTitles = [
            '1984',
            'Pride and Prejudice',
            'The Hobbit',
            'Harry Potter'
        ];

        const user = {
            username: 'alice',
            password: 'pass123'
        };

        // Helper to login
        const login = (user) => {
            cy.get('#username').clear().type(user.username);
            cy.get('#password').clear().type(user.password);
            cy.get('#loginButton').click();
        };

        // Helper to logout
        const logout = () => {
            cy.get('#logoutButton').click();
        };

        // Alice logs in
        login(user);

        // Alice borrows 3 books
        for (let i = 0; i < 3; i++) {
            cy.contains('td', bookTitles[i])
                .parent('tr')
                .within(() => {
                    cy.get('.borrowButton').click();
                });
        }

        // Assertion: Alice book count is at 3
        cy.get('#borrowedCount').should('have.text', '3');

        // Attempt to borrow a 4th book
        cy.contains('td', bookTitles[3])
            .parent('tr')
            .within(() => {
                cy.get('.borrowButton').click();
            });

        // Assertion: Alice cannot borrow another book
        // Demonstrates that the system stops additional borrows
        cy.get('#errorMessage').should('contain.text', 'MAX_BOOKS_REACHED');

        // Alice places a hold on the 4th book
        cy.contains('td', bookTitles[3])
            .parent('tr')
            .within(() => {
                cy.get('.holdButton').click();
            });

        // Assertion: Checking that a success message appears confirming the hold
        // Demonstrates that users at their borrowing limit can still place holds on books
        cy.get('#successMessage').should('contain.text', 'Hold placed');

        // Alice returns one book
        cy.get('#booksTable')
            .contains('td', bookTitles[0])
            .parent('tr')
            .find('.returnButton')
            .click();

        // Assertion: Alice book count is now 2
        // Demonstrates that returning a book properly updates the user's borrowed list
        cy.get('#borrowedCount').should('have.text', '2');

        logout();
    });

});