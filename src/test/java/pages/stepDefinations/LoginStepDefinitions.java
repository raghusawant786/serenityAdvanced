package pages.stepDefinations;

import io.cucumber.java.en.Given;
import net.serenitybdd.annotations.Steps;
import pages.object.LoginPage;
import pages.steps.LoginSteps;

public class LoginStepDefinitions {

    @Steps
    LoginSteps login;

    LoginPage loginPage;

    @Given("I navigate to the login page")
    public void i_navigate_to_the_login_page() {
      //  login.openLoginPage();
        loginPage.open();
    }
}