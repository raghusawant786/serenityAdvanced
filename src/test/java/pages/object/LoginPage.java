package pages.object;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;

@DefaultUrl("https://thinking-tester-contact-list.herokuapp.com/login")
public class LoginPage extends PageObject {
    public static final By SIGNUP_BUTTON = By.id("signup");
}