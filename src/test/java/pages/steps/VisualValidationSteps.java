package pages.steps;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.steps.UIInteractionSteps;
import org.openqa.selenium.WebDriver;
import pages.utils.VisualValidationUtils;
import pages.utils.VisualValidationResult;
import pages.utils.SoftAssertionManager;

public class VisualValidationSteps extends UIInteractionSteps {

    @Step("User captures baseline screenshot for {0}")
    public void captureBaselineScreenshot(String imageName) throws Exception {
        WebDriver driver = getDriver();
        VisualValidationUtils.captureBaseline(driver, imageName);
    }

    @Step("User captures screenshot and compares with baseline {0}")
    public void captureAndCompareScreenshot(String imageName) throws Exception {
        WebDriver driver = getDriver();
        VisualValidationResult result = VisualValidationUtils.compareWithBaselineDetailed(driver, imageName);
        
        if (!result.isMatch()) {
            String errorMessage = String.format(
                "❌ VISUAL VALIDATION FAILED: %s\n" +
                "Mismatch Details:\n" +
                "  • Diff Pixels: %d / Total Pixels: %.0f (%.2f%% difference)\n" +
                "  • Baseline: %s\n" +
                "  • Actual: %s\n" +
                "  • Diff Image: %s",
                imageName, 
                result.getDiffPixels(), 
                result.getTotalPixels(), 
                result.getDiffPercentage(),
                result.getBaselinePath(), 
                result.getActualPath(), 
                result.getDiffImagePath()
            );
            
            // Use soft assertion to collect error
            SoftAssertionManager.addError(errorMessage);
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
