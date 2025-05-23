Feature: User Authentication

  Background:
    Given the user is on the DemoBlaze homepage
    And the login dialog is open

  Scenario Outline: Login attempt with <description> credentials
    When the user logs in with username <username> and password <password>
    Then <outcome>

    Examples:
      | description | username         | password     | outcome                                            |
      | valid       | "lamhot_auto"    | "Querty@2023"| the user should be redirected to the homepage      |
      | invalid     | "wronguser"      | "wrongpass"  | an error message should be displayed               |
      | empty       | ""               | ""           | an error message should be displayed               |

  @logout
  Scenario: Logout after successful login
    Given the user is logged in with valid credentials
    When the user clicks the logout button
    Then the login button should be visible on the page
