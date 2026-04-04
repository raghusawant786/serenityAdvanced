package pages.steps;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.steps.UIInteractionSteps;
import org.openqa.selenium.WebDriver;
import pages.utils.VisualValidationUtils;

public class VisualValidationSteps extends UIInteractionSteps {

    @Step("User captures baseline screenshot for {0}")
    public void captureBaselineScreenshot(String imageName) throws Exception {
        WebDriver driver = getDriver();
        VisualValidationUtils.captureBaseline(driver, imageName);
    }

    @Step("User captures screenshot and compares with baseline {0}")
    public void captureAndCompareScreenshot(String imageName) throws Exception {
        WebDriver driver = getDriver();
        boolean isValid = VisualValidationUtils.compareWithBaseline(driver, imageName);
        
        if (!isValid) {
            throw new AssertionError("Visual differences detected for: " + imageName + 
                                   ". Check the diff image for details.");
        }
    }

    @Step("User updates visual baseline for {0}")
    public void updateVisualBaseline(String imageName) throws Exception {
        WebDriver driver = getDriver();
        VisualValidationUtils.updateBaseline(driver, imageName);
    }

    @Step("User prints visual validation report")
    public void printVisualReport() {
        VisualValidationUtils.printVisualReport();
    }
}
