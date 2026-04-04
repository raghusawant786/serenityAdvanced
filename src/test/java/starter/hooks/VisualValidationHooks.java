package starter.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import pages.utils.SoftAssertionManager;

/**
 * Serenity hooks for visual validation test lifecycle
 * - @Before: Initialize soft assertions for each scenario
 * - @After: Verify and report all collected visual validation failures
 */
public class VisualValidationHooks {

    @Before
    public void setupVisualValidation(Scenario scenario) {
        // Clear and initialize soft assertions for this scenario
        SoftAssertionManager.clear();
        System.out.println("🔹 Visual Validation initialized for scenario: " + scenario.getName());
    }

    @After
    public void verifyVisualValidation(Scenario scenario) {
        try {
            // Verify all soft assertions collected during the scenario
            if (SoftAssertionManager.hasFailures()) {
                System.out.println("\n⚠️  Visual Validation Issues Found:");
                System.out.println("   Error Count: " + SoftAssertionManager.getErrorCount());
                System.out.println("   Scenario: " + scenario.getName());
                System.out.println("\n" + "=".repeat(80));
            }
            
            // This will throw if any soft assertions failed
            SoftAssertionManager.assertAll();
            
        } catch (AssertionError e) {
            // Log the errors for Serenity report and re-throw
            System.out.println("\n❌ VISUAL VALIDATION FAILURES:\n");
            System.out.println(e.getMessage());
            System.out.println("\n" + "=".repeat(80) + "\n");
            
            // Re-throw to mark scenario as failed in Serenity report
            throw e;
        }
    }
}
