Feature: User Registration
  I want to verify user creation works as expected

  @dev
  Scenario: Create a new user
    Given I navigate to the login page
    Then User captures screenshot and compares with baseline "login-page"
    And User logs in with email "abc1994@gmail.com" and password "Raghu@1234"
    And User clicks on Add a New Contact button
    And User enters basic contact information with first name "John" and last name "Doe"
    And User enters date of birth "1990-01-15"
    And User enters contact information with email "john.doe@email.com" and phone "8005551234"
    And User enters address information with street1 "123 Main St" street2 "Apt 4B" city "New York" state "NY" postal code "10001" and country "USA"
    Then User captures screenshot and compares with baseline "contact-form"
    And User submits the add contact form
    Then User captures screenshot and compares with baseline "contact-list-page"
    Then User prints visual validation report