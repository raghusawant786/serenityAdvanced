package pages.steps;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.steps.UIInteractionSteps;
import pages.object.LoginPage;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class LoginSteps extends UIInteractionSteps {
    LoginPage loginPage;

    @Step("User opens the Contacts List App")
    public void openLoginPage() {
       // loginPage.open();
        withTimeoutOf(Duration.ofSeconds(10))
                .waitFor(presenceOfElementLocated(LoginPage.SIGNUP_BUTTON));
    }

}
