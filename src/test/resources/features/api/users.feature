Feature: User Management API

  Background:
    Given the JSONPlaceholder API is available at "/users"

  Scenario Outline: Fetch user with <type> ID
    When the client requests user with ID "<userId>"
    Then the response status code should be <statusCode>

    Examples:
      | type    | userId | statusCode |
      | valid   | 1      | 200        |
      | invalid | 99999  | 404        |

  Scenario: Retrieve all users
    When the client requests all users
    Then the response status code should be 200

  Scenario: Create a new user
    When the client creates a new user with name "New User" and email "newuser@example.com"
    Then the response status code should be 201

  Scenario: Delete an existing user
    When the client deletes the user with ID "1"
    Then the response status code should be 200