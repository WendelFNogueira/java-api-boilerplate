Feature: User Management

  Background:
    Given the application is running

  Scenario: Create a new user
    When I create a user with name "John Doe" and email "john@example.com"
    Then the user should be created successfully
    And the response should contain the user details

  Scenario: Get all users
    Given there is a user with name "Jane Doe" and email "jane@example.com"
    When I request all users
    Then I should receive a list of users
    And the list should contain the user "Jane Doe"

  Scenario: Get user by ID
    Given there is a user with name "Bob Smith" and email "bob@example.com"
    When I request the user by ID
    Then I should receive the user details for "Bob Smith"

  Scenario: Update user
    Given there is a user with name "Alice Johnson" and email "alice@example.com"
    When I update the user with name "Alice Updated" and email "alice.updated@example.com"
    Then the user should be updated successfully

  Scenario: Delete user
    Given there is a user with name "Charlie Brown" and email "charlie@example.com"
    When I delete the user
    Then the user should be deleted successfully
