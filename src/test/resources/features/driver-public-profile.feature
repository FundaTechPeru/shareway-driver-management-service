Feature: Public driver profile
  As a passenger
  I want to see public information about a driver before the trip
  So that I can decide whether to trust who will drive me

  Scenario: A passenger sees the public profile of a driver
    Given a driver is registered with email "maria.lopez@upc.edu.pe" and license number "Q1234567"
    And I am logged in as a passenger with email "ana.torres@upc.edu.pe"
    When I view the public profile of the driver "maria.lopez@upc.edu.pe"
    Then the response status should be 200
    And the response field "fullName" should be "Test User"
    And the response field "driverStatus" should be "PENDING_VERIFICATION"
    And the response field "licenseNumber" should be "Q1234567"

  Scenario: The public profile of a passenger is not found
    Given a user is registered with email "pedro.gomez@upc.edu.pe" and password "Secret123"
    And I am logged in as a passenger with email "ana.torres@upc.edu.pe"
    When I view the public profile of the driver "pedro.gomez@upc.edu.pe"
    Then the request should fail with status 404

  Scenario: The public profile requires authentication
    Given a driver is registered with email "maria.lopez@upc.edu.pe" and license number "Q1234567"
    And I am not logged in
    When I view the public profile of the driver "maria.lopez@upc.edu.pe"
    Then the request should fail with status 401
