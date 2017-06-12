@txn
Feature: Finding a book by ISBN
  Background: Preload DB Mock Data
    Given packt-books fixture is loaded
  Scenario: Load one book
    Given 3 books available in the catalogue
    When searching for book by isbn 972-1-78528-415-1
    Then book title will be Orchestrating Docker