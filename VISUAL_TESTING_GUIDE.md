# Visual Regression Testing Guide

## Overview
This framework implements visual regression testing using **AShot** (screenshot comparison library) integrated with Serenity BDD. It automatically captures baseline screenshots and compares current pages against them to detect UI differences.

## Architecture

### Core Components

1. **VisualValidationUtils.java**
   - `captureBaseline(WebDriver, imageName)` - Save baseline screenshot
   - `compareWithBaseline(WebDriver, imageName)` - Compare current vs baseline
   - `updateBaseline(WebDriver, imageName)` - Update baseline when UI intentionally changes
   - `printVisualReport()` - Print test report paths

2. **VisualValidationSteps.java**
   - Serenity @Step methods wrapping the utility functions

3. **VisualValidationStepDefinitions.java**
   - Cucumber @When/@Then annotations mapping feature file steps to Java code

## Directory Structure

```
src/test/resources/visual-baselines/    # Baseline screenshots (source of truth)
target/visual-results/
  ├── actual/                            # Current screenshots from test runs
  └── diffs/                             # Highlighted differences (red marked areas)
```

## How It Works

### First Run (Capture Baseline)
```gherkin
When User captures baseline screenshot for "login-page"
```
- Takes screenshot of current page
- Saves as `src/test/resources/visual-baselines/login-page.png`
- Serves as reference for future comparisons

### Subsequent Runs (Compare)
```gherkin
Then User captures screenshot and compares with baseline "login-page"
```
1. Takes current screenshot
2. Loads baseline image
3. Compares pixel-by-pixel using AShot's ImageDiffer
4. If differences found:
   - ✓ Generates `target/visual-results/actual/login-page.png` (current)
   - ✓ Generates `target/visual-results/diffs/login-page_diff.png` (differences marked in red)
   - Returns `false` (test fails)
5. If no differences:
   - ✓ Returns `true` (test passes)

### Update Baseline (When UI Changes Intentionally)
```gherkin
When User updates visual baseline for "login-page"
```
- Deletes old baseline
- Captures new baseline from current page
- Use this step when you intentionally update UI styling

## Feature File Examples

### Complete Scenario with Visual Validation
```gherkin
@dev
Scenario: Create a new user with visual validation
    Given I navigate to the login page
    When User captures baseline screenshot for "login-page"
    And User logs in with email "abc1994@gmail.com" and password "Raghu@1234"
    And User clicks on Add a New Contact button
    Then User captures screenshot and compares with baseline "add-contact-form"
    And User enters basic contact information with first name "John" and last name "Doe"
    And User enters date of birth "1990-01-15"
    And User captures screenshot and compares with baseline "contact-form-filled"
    And User submits the add contact form
    Then User captures screenshot and compares with baseline "contact-list-page"
    And User prints visual validation report
```

### Available Step Definitions
- `When User captures baseline screenshot for "{string}"`
- `Then User captures screenshot and compares with baseline "{string}"`
- `When User updates visual baseline for "{string}"`
- `Then User prints visual validation report`

## Running Tests

### Run with Visual Validation
```bash
mvn verify -Dtags="@dev"
```

### View Test Results
- **Serenity Report**: `target/site/serenity/index.html`
- **Visual Baselines**: `src/test/resources/visual-baselines/`
- **Actual Screenshots**: `target/visual-results/actual/`
- **Diff Images**: `target/visual-results/diffs/`

## Troubleshooting

### Visual Differences Detected
1. Check `target/visual-results/diffs/<test>_diff.png` to see what changed
2. If change is intentional:
   ```gherkin
   When User updates visual baseline for "page-name"
   ```
3. If change is unintended, fix the UI issue in your code

### Baseline Not Found
- First run automatically creates baseline
- Or explicitly capture: `When User captures baseline screenshot for "page-name"`

### Screenshot Quality Issues
- AShot uses `takeScreenshot(driver)` which captures viewport
- Supported browsers: Chrome, Firefox, Edge, Safari
- Tests run in parallel; ensure pages load completely before comparing

## Best Practices

1. **Capture baselines first** - Run scenario once to create baselines
2. **Run headless for CI/CD** - Use headless mode for consistent rendering
3. **Wait for page load** - Ensure elements are loaded before capturing
4. **Version control baselines** - Commit baseline images to git
5. **Review diffs carefully** - Not all pixel differences indicate failures (font rendering, anti-aliasing)
6. **Use meaningful names** - Name screenshots based on page/component (e.g., "login-form", "contact-list")

## Sample Test (Standalone)
```bash
java -cp target/test-classes pages.tests.VisualRegressionTest
```
This runs `VisualRegressionTest.java` to test visual validation independently without Serenity.

## Dependencies
- `ashot:1.5.4` - Screenshot capture and comparison
- `commons-io:2.15.1` - File operations
- `serenity-core:4.3.2` - Serenity BDD framework
