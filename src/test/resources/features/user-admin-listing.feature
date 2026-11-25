Feature: Administrative user directory
  As an administrator
  I want to list and filter the platform users
  So that I can manage them from the admin panel

  Background:
    Given the following users exist:
      | fullName    | email             | role      |
      | Ana Torres  | ana@shareway.pe   | PASSENGER |
      | Ana Beltran | anab@shareway.pe  | PASSENGER |
      | Beto Suarez | beto@shareway.pe  | PASSENGER |
      | Carla Jefe  | carla@shareway.pe | ADMIN     |

  Scenario: An administrator lists every user
    Given I am logged in as an administrator
    When I list users
    Then the response status should be 200
    And the user listing reports at least 4 total elements

  Scenario: Filter the directory by role
    Given I am logged in as an administrator
    When I list users filtered by role "PASSENGER"
    Then the response status should be 200
    And every listed user has role "PASSENGER"

  Scenario: Filter the directory by a partial full name
    Given I am logged in as an administrator
    When I list users filtered by full name "ana"
    Then the response status should be 200
    And the user listing contains the email "ana@shareway.pe"
    And the user listing contains the email "anab@shareway.pe"

  Scenario: Filter the directory by a partial email
    Given I am logged in as an administrator
    When I list users filtered by email "beto@"
    Then the response status should be 200
    And the user listing contains the email "beto@shareway.pe"

  Scenario: Pagination caps the page to the requested size
    Given I am logged in as an administrator
    When I list users with page 0 and size 2
    Then the response status should be 200
    And the user listing page contains 2 items

  Scenario: A passenger cannot read the directory
    Given a user is registered with email "pasajero@shareway.pe" and password "Secret123"
    And I am logged in as "pasajero@shareway.pe" with password "Secret123"
    When I list users
    Then the response status should be 403
