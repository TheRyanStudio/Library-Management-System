Feature: Library System Operations
  As a user
  I want to perform library operations
  So that I can borrow books


  Scenario: A1_scenario
    Given a library with a book titled The Great Gatsby
    When "alice" logs in with password "pass123"
    And borrows "The Great Gatsby"
    Then The Great Gatsby is unavailable
    And alice is the current borrower of The Great Gatsby
    And user logs out
    When "bob" logs in with password "pass456"
    And attempts to borrow "The Great Gatsby"
    Then bob is unable to borrow the book
    And user logs out
    When "alice" logs in with password "pass123"
    And returns "The Great Gatsby"
    Then The Great Gatsby becomes available

  Scenario: multiple_holds_queue_processing
    Given a library with a book titled The Great Gatsby
    When "alice" logs in with password "pass123"
    And borrows "The Great Gatsby"
    And user logs out
    When "bob" logs in with password "pass456"
    And A hold is placed on "The Great Gatsby"
    And user logs out
    When "charlie" logs in with password "pass789"
    And A hold is placed on "The Great Gatsby"
    When "alice" logs in with password "pass123"
    And returns "The Great Gatsby"
    And user logs out
    Then bob is the next holder in line
    When "charlie" logs in with password "pass789"
    And attempts to borrow "The Great Gatsby"
    Then charlie cannot borrow the book while bob is next in line
    When "bob" logs in with password "pass456"
    Then bob is notified that The Great Gatsby is available
    And borrows "The Great Gatsby"
    And user logs out
    When "bob" logs in with password "pass456"
    And  returns "The Great Gatsby"
    Then charlie is the next holder in line
    When "charlie" logs in with password "pass789"
    Then charlie is notified that The Great Gatsby is available

  Scenario Outline: borrowing_limit_and_hold_interactions
    Given a library with book titles The Hobbit, Harry Potter, The Catcher in Rye and Animal Farm
    When "<user>" logs in with password "<password>"
    And borrows "<b1>"
    And borrows "<b2>"
    And borrows "<b3>"
    And attempts to borrow "<b4>"
    Then alice is at max books and unable to borrow another book
    When A hold is placed on "<b4>"
    Then alice is in the holding line
    And returns "<b3>"
    Then alice is notified that the Animal Farm is available

    Examples:
      | user  |password| b1         | b2           | b3                     | b4         |
      | alice | pass123| The Hobbit | Harry Potter | The Catcher in the Rye | Animal Farm|

  Scenario: no_books_borrowed_scenario
    Given a library with a book titled The Great Gatsby
    When "alice" logs in with password "pass123"
    And attempts to return The Great Gatsby
    Then There is no books to return
    And notified that there are no books to return
    And user logs out
    When "bob" logs in with password "pass456"
    And attempts to return The Great Gatsby
    Then There is no books to return
    And notified that there are no books to return
    And user logs out
    When "charlie" logs in with password "pass789"
    And attempts to return The Great Gatsby
    Then There is no books to return
    And notified that there are no books to return
    And user logs out
    Then All books show as available




