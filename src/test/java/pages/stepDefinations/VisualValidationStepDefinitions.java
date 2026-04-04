package pages.stepDefinations;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;
import pages.steps.VisualValidationSteps;

public class VisualValidationStepDefinitions {

    @Steps
    VisualValidationSteps visualValidation;

    @When("User captures baseline screenshot for {string}")
    public void user_captures_baseline_screenshot(String imageName) throws Exception {
        visualValidation.captureBaselineScreenshot(imageName);
    }

    @Then("User captures screenshot and compares with baseline {string}")
    public void user_captures_and_compares_screenshot(String imageName) throws Exception {
        visualValidation.captureAndCompareScreenshot(imageName);
    }

    @When("User updates visual baseline for {string}")
    public void user_updates_visual_baseline(String imageName) throws Exception {
        visualValidation.updateVisualBaseline(imageName);
    }

    @Then("User prints visual validation report")
    public void user_prints_visual_report() {
        visualValidation.printVisualReport();
    }
}
