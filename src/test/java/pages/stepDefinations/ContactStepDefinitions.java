package pages.stepDefinations;

import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import pages.steps.LoginSteps;
import pages.steps.ContactSteps;

public class ContactStepDefinitions {

    @Steps
    LoginSteps loginSteps;

    @Steps
    ContactSteps contactSteps;

    @When("User logs in with email {string} and password {string}")
    public void user_logs_in_with_email_and_password(String email, String password) {
        loginSteps.userLogsIn(email, password);
    }

    @When("User clicks on Add a New Contact button")
    public void user_clicks_on_add_a_new_contact_button() {
        loginSteps.clickAddNewContact();
    }

    @When("User enters basic contact information with first name {string} and last name {string}")
    public void user_enters_basic_contact_information(String firstName, String lastName) {
        contactSteps.enterBasicInformation(firstName, lastName);
    }

    @When("User enters date of birth {string}")
    public void user_enters_date_of_birth(String birthdate) {
        contactSteps.enterDateOfBirth(birthdate);
    }

    @When("User enters contact information with email {string} and phone {string}")
    public void user_enters_contact_information(String email, String phone) {
        contactSteps.enterContactInformation(email, phone);
    }

    @When("User enters address information with street1 {string} street2 {string} city {string} state {string} postal code {string} and country {string}")
    public void user_enters_address_information(String street1, String street2, String city, String state, String postalCode, String country) {
        contactSteps.enterAddressInformation(street1, street2, city, state, postalCode, country);
    }

    @When("User submits the add contact form")
    public void user_submits_the_add_contact_form() {
        contactSteps.submitContactForm();
    }
}
