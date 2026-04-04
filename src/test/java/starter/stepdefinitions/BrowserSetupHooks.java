package starter.stepdefinitions;

import io.cucumber.java.Before;
import net.serenitybdd.core.steps.UIInteractionSteps;

/**
 * Browser setup hooks
 * Configures browser behavior before each scenario
 */
public class BrowserSetupHooks extends UIInteractionSteps {

    @Before
    public void maximizeBrowser() {
        try {
          //  WebDriver driver = getDriver();
           // driver.manage().window().maximize();
         //   System.out.println("✓ Browser window maximized");
        } catch (Exception e) {
            System.out.println("⚠ Could not maximize browser window: " + e.getMessage());
        }
    }
}
