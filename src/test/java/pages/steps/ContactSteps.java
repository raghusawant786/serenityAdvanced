package pages.steps;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.steps.UIInteractionSteps;
import pages.object.AddContactPage;

public class ContactSteps extends UIInteractionSteps {
    AddContactPage addContactPage;

    @Step("User enters basic contact information with first name {0} and last name {1}")
    public void enterBasicInformation(String firstName, String lastName) {
        addContactPage.element(AddContactPage.FIRST_NAME_INPUT).sendKeys(firstName);
        addContactPage.element(AddContactPage.LAST_NAME_INPUT).sendKeys(lastName);
    }

    @Step("User enters date of birth {0}")
    public void enterDateOfBirth(String birthdate) {
        addContactPage.element(AddContactPage.BIRTHDATE_INPUT).sendKeys(birthdate);
    }

    @Step("User enters contact information with email {0} and phone {1}")
    public void enterContactInformation(String email, String phone) {
        addContactPage.element(AddContactPage.EMAIL_INPUT).sendKeys(email);
        addContactPage.element(AddContactPage.PHONE_INPUT).sendKeys(phone);
    }

    @Step("User enters address information with street1 {0}, street2 {1}, city {2}, state {3}, postal code {4}, and country {5}")
    public void enterAddressInformation(String street1, String street2, String city, String state, String postalCode, String country) {
        addContactPage.element(AddContactPage.STREET1_INPUT).sendKeys(street1);
        addContactPage.element(AddContactPage.STREET2_INPUT).sendKeys(street2);
        addContactPage.element(AddContactPage.CITY_INPUT).sendKeys(city);
        addContactPage.element(AddContactPage.STATE_PROVINCE_INPUT).sendKeys(state);
        addContactPage.element(AddContactPage.POSTAL_CODE_INPUT).sendKeys(postalCode);
        addContactPage.element(AddContactPage.COUNTRY_INPUT).sendKeys(country);
    }

    @Step("User submits the add contact form")
    public void submitContactForm() {
        addContactPage.element(AddContactPage.SUBMIT_BUTTON).click();
    }
}
