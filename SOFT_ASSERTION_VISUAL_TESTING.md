# Visual Regression Testing with Soft Assertions

## Overview

Your visual validation framework now uses **soft assertions** to collect all visual validation failures throughout a test scenario and report them together in the Serenity HTML report. This approach:

- ✅ Collects multiple visual validation failures instead of failing on first error
- ✅ Reports all failures with detailed metrics and file paths in Serenity report
- ✅ Displays failures at the scenario level in `target/site/serenity/index.html`
- ✅ Provides comprehensive error context for debugging

---

## Architecture

### 1. **SoftAssertionManager.java** (Thread-Safe Collection)
Manages cross-step error collection using ThreadLocal:

```java
public static void addError(String message) {
    softAssertions.get().fail(message);
}

public static void assertAll() {
    try {
        softAssertions.get().assertAll();
    } finally {
        softAssertions.remove();  // Cleanup
    }
}
```

**Key Features:**
- ThreadLocal storage ensures each test gets its own SoftAssertions instance
- `addError()` collects failures without stopping the test
- `assertAll()` throws AssertionError with all collected messages
- `clear()` resets state for next scenario

---

### 2. **VisualValidationSteps.java** (Step-Level Collection)
Steps now collect errors instead of throwing immediately:

```java
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
            imageName, result.getDiffPixels(), result.getTotalPixels(), 
            result.getDiffPercentage(), result.getBaselinePath(), 
            result.getActualPath(), result.getDiffImagePath()
        );
        
        // Collect error (test continues)
        SoftAssertionManager.addError(errorMessage);
    }
}
```

**Behavior:**
- Calls `compareWithBaselineDetailed()` to get detailed metrics
- If mismatch found, formats comprehensive error message
- Adds to soft assertions collection (does NOT throw)
- Test continues to next step

---

### 3. **VisualValidationHooks.java** (@Before / @After)
Lifecycle hooks for soft assertion management:

```java
@Before
public void setupVisualValidation(Scenario scenario) {
    SoftAssertionManager.clear();
    System.out.println("🔹 Visual Validation initialized for scenario: " + scenario.getName());
}

@After
public void verifyVisualValidation(Scenario scenario) {
    if (SoftAssertionManager.hasFailures()) {
        System.out.println("⚠️  Visual Validation Issues Found: " + SoftAssertionManager.getErrorCount());
    }
    
    // Throws if any failures were collected
    SoftAssertionManager.assertAll();
}
```

**Behavior:**
- `@Before`: Initializes clean SoftAssertions for each scenario
- `@After`: Verifies all collected errors and throws AssertionError with full details
- Serenity captures the AssertionError and marks scenario as FAILED

---

### 4. **VisualValidationResult.java** (Metrics Data Class)
Holds detailed comparison metrics:

```java
public class VisualValidationResult {
    private boolean match;
    private int diffPixels;
    private double totalPixels;
    private double diffPercentage;  // Calculated: (diffPixels / totalPixels) * 100
    private String baselinePath;
    private String actualPath;
    private String diffImagePath;
}
```

---

### 5. **VisualValidationUtils.java** (Enhanced Comparison)
New `compareWithBaselineDetailed()` method:

```java
public static VisualValidationResult compareWithBaselineDetailed(WebDriver driver, String imageName) 
    throws IOException {
    
    // Capture actual screenshot
    Screenshot actualScreenshot = new AShot().takeScreenshot(driver);
    
    // Load baseline
    BufferedImage baseline = ImageIO.read(baselineFile);
    
    // Compare and generate diff
    ImageDiff diff = differ.makeDiff(baseline, actualScreenshot.getImage());
    
    if (diff.hasDiff()) {
        int diffPixels = diff.getDiffSize();
        long totalPixels = baseline.getWidth() * baseline.getHeight();
        
        // Save diff image
        ImageIO.write(diff.getMarkedImage(), "PNG", diffFile);
        
        // Return result with metrics
        return new VisualValidationResult(
            false,  // match
            diffPixels,
            totalPixels,
            baselineFile.getAbsolutePath(),
            actualFile.getAbsolutePath(),
            diffFile.getAbsolutePath()
        );
    }
    
    return new VisualValidationResult(true, 0, totalPixels, ...);
}
```

---

## Test Execution Flow

```
1. Cucumber Scenario Starts
   ↓
2. @Before Hook (VisualValidationHooks)
   → SoftAssertionManager.clear()
   ↓
3. Steps Execute (including visual validation steps)
   Step 1: Login → Completes ✓
   Step 2: Captures baseline for "login-form" → Completes ✓
   Step 3: Modifies form → Completes ✓
   Step 4: Compares with baseline → Finds diff
     → SoftAssertionManager.addError() [Does NOT throw]
     → Test continues ✓
   Step 5: Captures for "contact-form" → Completes ✓
   ↓
4. @After Hook (VisualValidationHooks)
   → Calls SoftAssertionManager.assertAll()
   → Throws AssertionError with ALL collected errors
   ↓
5. Serenity Captures Failure
   → Marks scenario as FAILED
   → Stores full error message in report
   ↓
6. HTML Report Generated
   target/site/serenity/index.html
   → Scenario shows as FAILED
   → Error details visible on click
```

---

## Serenity HTML Report Output

### Report Structure
```
SCENARIO: User Registration and Contact Form
Status: ❌ FAILED

Error Message:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
❌ VISUAL VALIDATION FAILED: login-form

Mismatch Details:
  • Diff Pixels: 2,543 / Total Pixels: 921,600 (0.28% difference)
  • Baseline: /path/to/src/test/resources/visual-baselines/login-form.png
  • Actual: /path/to/target/visual-results/actual/login-form.png
  • Diff Image: /path/to/target/visual-results/diffs/login-form_diff.png
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Steps:
  ✓ User opens application
  ✓ User captures baseline screenshot for login-form
  ✓ User modifies form
  ✗ User captures screenshot and compares with baseline login-form
    └─ Failure details shown above
  ✓ User captures baseline screenshot for contact-form
  ✓ User fills contact details
```

---

## Multiple Validation Failures Example

If your scenario has multiple visual validations:

```gherkin
@dev
Scenario: Multi-step visual validation
    When User opens application
    And User captures baseline screenshot for "login-form"
    And User captures screenshot and compares with baseline "login-form"
    And User navigates to dashboard
    And User captures baseline screenshot for "dashboard"
    And User captures screenshot and compares with baseline "dashboard"
    And User opens contact form
    And User captures screenshot and compares with baseline "contact-form"
```

If failures occur at steps 3, 6, and 8:

**Console Output:**
```
⚠️  Visual Validation Issues Found:
   Error Count: 3
   
❌ VISUAL VALIDATION FAILURES:

❌ VISUAL VALIDATION FAILED: login-form
Mismatch Details:
  • Diff Pixels: 2,543 / Total Pixels: 921,600 (0.28% difference)
  ...

❌ VISUAL VALIDATION FAILED: dashboard
Mismatch Details:
  • Diff Pixels: 5,120 / Total Pixels: 2,073,600 (0.25% difference)
  ...

❌ VISUAL VALIDATION FAILED: contact-form
Mismatch Details:
  • Diff Pixels: 1,280 / Total Pixels: 1,228,800 (0.10% difference)
  ...
```

**Report Status:** ❌ FAILED (with all 3 errors visible)

---

## Directory Structure

```
Project Root/
├── src/test/resources/
│   └── visual-baselines/           # ✓ Baseline images
│       ├── login-form.png
│       ├── dashboard.png
│       └── contact-form.png
│
└── target/visual-results/
    ├── actual/                      # Current screenshots
    │   ├── login-form.png
    │   ├── dashboard.png
    │   └── contact-form.png
    │
    └── diffs/                       # Diff visualizations (if mismatch)
        ├── login-form_diff.png      # Shows differences in red
        ├── dashboard_diff.png
        └── contact-form_diff.png
```

---

## Running Tests

### Execute with soft assertions active:
```bash
# Run all @dev scenarios
mvn verify -Dtags="@dev"

# Run with specific browser (headless for CI)
mvn verify -Dtags="@dev" -Dwebdriver.headless=true

# Run in parallel
mvn verify -Dtags="@dev" -Dthreads=5

# View report
open target/site/serenity/index.html
```

---

## Advantages of Soft Assertions

| Aspect | Hard Assertions (Immediate Throw) | Soft Assertions (Collect All) |
|--------|-----------------------------------|-------------------------------|
| **Failure Point** | Stops at first error | Completes all steps |
| **Error Count** | 1 error per run | All errors in 1 run |
| **Report Clarity** | "First thing that broke" | "Everything that's broken" |
| **Debugging** | Must fix and re-run | All issues visible at once |
| **Runs Needed** | Multiple (1 error at a time) | Single run captures all |
| **Total Time** | Longer (false negatives hide later issues) | Shorter (all issues found once) |

---

## Troubleshooting

### Issue: Report shows PASSED but output shows errors

**Reason:** Soft assertions not being verified in @After hook

**Fix:** Ensure `VisualValidationHooks.java` @After method calls `SoftAssertionManager.assertAll()`

### Issue: Errors not appearing in HTML report

**Reason:** AssertionError not being properly captured by Serenity

**Fix:** Ensure `@After` hook is in:
- Correct package: `starter.hooks`
- Proper import: `import io.cucumber.java.After`
- Re-throw the AssertionError: `throw e;`

### Issue: "ThreadLocal not cleaned up" or "Errors from previous test"

**Reason:** SoftAssertionManager not clearing between tests

**Fix:** Ensure `@Before` hook calls `SoftAssertionManager.clear()` at start

---

## Key Files Modified

1. **SoftAssertionManager.java** (NEW)
   - ThreadLocal SoftAssertions management
   - addError(), assertAll(), clear(), hasFailures()

2. **VisualValidationSteps.java** (UPDATED)
   - Uses `SoftAssertionManager.addError()` instead of throwing
   - Calls `compareWithBaselineDetailed()` for metrics

3. **VisualValidationHooks.java** (NEW)
   - @Before: Initializes soft assertions
   - @After: Verifies all collected errors

4. **VisualValidationResult.java** (NEW)
   - Data class for comparison metrics
   - Calculates diff percentage

5. **VisualValidationUtils.java** (UPDATED)
   - New `compareWithBaselineDetailed()` method
   - Returns VisualValidationResult instead of boolean

---

## Next Steps

1. Run your @dev scenario: `mvn verify -Dtags="@dev"`
2. Check console output for visual validation details
3. View Serenity report: `open target/site/serenity/index.html`
4. Verify failures appear at scenario level
5. Click on failed scenario to see detailed error messages with file paths

