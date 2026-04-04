package pages.utils;

import org.assertj.core.api.SoftAssertions;

/**
 * Manages ThreadLocal SoftAssertions for cross-step error collection
 * Allows visual validation errors to be collected throughout a scenario
 * and reported at the end in Serenity HTML report
 */
public class SoftAssertionManager {
    private static final ThreadLocal<SoftAssertions> softAssertions = 
        ThreadLocal.withInitial(SoftAssertions::new);
    
    /**
     * Get the current thread's SoftAssertions instance
     */
    public static SoftAssertions getSoftAssertions() {
        return softAssertions.get();
    }
    
    /**
     * Add a soft assertion error with message
     */
    public static void addError(String message) {
        softAssertions.get().fail(message);
    }
    
    /**
     * Add a soft assertion with condition check
     */
    public static void assertThat(String message, boolean condition) {
        if (!condition) {
            softAssertions.get().fail(message);
        }
    }
    
    /**
     * Verify all soft assertions and throw if any failed
     * This is called by the @After hook
     */
    public static void assertAll() {
        try {
            softAssertions.get().assertAll();
        } finally {
            // Clean up for next test
            softAssertions.remove();
        }
    }
    
    /**
     * Clear all soft assertions (useful for cleanup)
     */
    public static void clear() {
        softAssertions.remove();
        softAssertions.set(new SoftAssertions());
    }
    
    /**
     * Check if any soft assertions have failed
     */
    public static boolean hasFailures() {
        return !softAssertions.get().errorsCollected().isEmpty();
    }
    
    /**
     * Get count of collected errors
     */
    public static int getErrorCount() {
        return softAssertions.get().errorsCollected().size();
    }
}
