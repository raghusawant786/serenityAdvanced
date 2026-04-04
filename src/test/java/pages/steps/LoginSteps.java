package pages.steps;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.steps.UIInteractionSteps;
import pages.object.LoginPage;
import pages.object.ContactListPage;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class LoginSteps extends UIInteractionSteps {
    LoginPage loginPage;
    ContactListPage contactListPage;

    @Step("User opens the Contacts List App")
    public void openLoginPage() {
        loginPage.open();
        withTimeoutOf(Duration.ofSeconds(10))
                .waitFor(presenceOfElementLocated(LoginPage.SIGNUP_BUTTON));
    }

    @Step("User logs in with email {0} and password {1}")
    public void userLogsIn(String email, String password) {
        loginPage.element(LoginPage.EMAIL_INPUT).sendKeys(email);
        loginPage.element(LoginPage.PASSWORD_INPUT).sendKeys(password);
        loginPage.element(LoginPage.SUBMIT_BUTTON).click();
    }

    @Step("User clicks on Add a New Contact button")
    public void clickAddNewContact() {
        contactListPage.element(ContactListPage.ADD_CONTACT_BUTTON).click();
    }

}
