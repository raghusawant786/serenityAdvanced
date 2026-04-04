package pages.object;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;

public class AddContactPage extends PageObject {
    // Basic Information
    public static final By FIRST_NAME_INPUT = By.id("firstName");
    public static final By LAST_NAME_INPUT = By.id("lastName");
    
    // Date of Birth
    public static final By BIRTHDATE_INPUT = By.id("birthdate");
    
    // Contact Information
    public static final By EMAIL_INPUT = By.id("email");
    public static final By PHONE_INPUT = By.id("phone");
    
    // Address Information
    public static final By STREET1_INPUT = By.id("street1");
    public static final By STREET2_INPUT = By.id("street2");
    public static final By CITY_INPUT = By.id("city");
    public static final By STATE_PROVINCE_INPUT = By.id("stateProvince");
    public static final By POSTAL_CODE_INPUT = By.id("postalCode");
    public static final By COUNTRY_INPUT = By.id("country");
    
    // Form Controls
    public static final By SUBMIT_BUTTON = By.id("submit");
    public static final By CANCEL_BUTTON = By.id("cancel");
    public static final By ERROR_MESSAGE = By.id("error");
}
