package pages.utils;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VisualValidationUtils {
    private static final String BASELINE_DIR = "src/test/resources/visual-baselines";
    private static final String ACTUAL_DIR = "target/visual-results/actual";
    private static final String DIFF_DIR = "target/visual-results/diffs";

    /**
     * Take screenshot and save as baseline
     */
    public static void captureBaseline(WebDriver driver, String imageName) throws IOException {
        ensureDirectoryExists(BASELINE_DIR);

        // Take screenshot using AShot (similar to your working sample)
        Screenshot screenshot = new AShot().takeScreenshot(driver);

        File baselineFile = new File(BASELINE_DIR + File.separator + imageName + ".png");
        ImageIO.write(screenshot.getImage(), "PNG", baselineFile);
        System.out.println("✓ Baseline captured: " + baselineFile.getAbsolutePath());
    }

    /**
     * Compare current screenshot with baseline image
     */
    public static boolean compareWithBaseline(WebDriver driver, String imageName) throws IOException {
        ensureDirectoryExists(ACTUAL_DIR);
        ensureDirectoryExists(DIFF_DIR);

        // Take current screenshot using AShot
        Screenshot actualScreenshot = new AShot().takeScreenshot(driver);

        // Load baseline
        File baselineFile = new File(BASELINE_DIR + File.separator + imageName + ".png");
        if (!baselineFile.exists()) {
            System.out.println("⚠ Baseline not found: " + baselineFile.getAbsolutePath());
            captureBaseline(driver, imageName);
            return true;
        }

        BufferedImage baseline = ImageIO.read(baselineFile);

        // Save actual screenshot
        File actualFile = new File(ACTUAL_DIR + File.separator + imageName + ".png");
        ImageIO.write(actualScreenshot.getImage(), "PNG", actualFile);
        System.out.println("✓ Actual screenshot saved: " + actualFile.getAbsolutePath());

        // Compare images
        ImageDiffer differ = new ImageDiffer();
        ImageDiff diff = differ.makeDiff(baseline, actualScreenshot.getImage());

        if (diff.hasDiff()) {
            // Save diff image
            File diffFile = new File(DIFF_DIR + File.separator + imageName + "_diff.png");
            ImageIO.write(diff.getMarkedImage(), "PNG", diffFile);

            int diffPixels = diff.getDiffSize();
            System.out.println("❌ Visual difference detected!");
            System.out.println("   Diff pixels: " + diffPixels);
            System.out.println("   Baseline: " + baselineFile.getAbsolutePath());
            System.out.println("   Actual: " + actualFile.getAbsolutePath());
            System.out.println("   Diff image: " + diffFile.getAbsolutePath());

            return false;
        } else {
            System.out.println("✓ UI matches baseline image!");
            return true;
        }
    }

    /**
     * Compare current screenshot with baseline and return detailed result object
     * Includes metrics like diff pixels, percentage, and file paths
     */
    public static VisualValidationResult compareWithBaselineDetailed(WebDriver driver, String imageName) throws IOException {
        ensureDirectoryExists(ACTUAL_DIR);
        ensureDirectoryExists(DIFF_DIR);

        // Take current screenshot using AShot
        Screenshot actualScreenshot = new AShot().takeScreenshot(driver);

        // Load baseline
        File baselineFile = new File(BASELINE_DIR + File.separator + imageName + ".png");
        if (!baselineFile.exists()) {
            System.out.println("⚠ Baseline not found: " + baselineFile.getAbsolutePath());
            captureBaseline(driver, imageName);
            // Return match=true since baseline just created
            return new VisualValidationResult(true, 0, 0, baselineFile.getAbsolutePath(), baselineFile.getAbsolutePath(), "");
        }

        BufferedImage baseline = ImageIO.read(baselineFile);

        // Save actual screenshot
        File actualFile = new File(ACTUAL_DIR + File.separator + imageName + ".png");
        ImageIO.write(actualScreenshot.getImage(), "PNG", actualFile);

        // Compare images
        ImageDiffer differ = new ImageDiffer();
        ImageDiff diff = differ.makeDiff(baseline, actualScreenshot.getImage());

        File diffFile = new File(DIFF_DIR + File.separator + imageName + "_diff.png");
        String diffFilePath = "";

        if (diff.hasDiff()) {
            // Save diff image
            ImageIO.write(diff.getMarkedImage(), "PNG", diffFile);
            diffFilePath = diffFile.getAbsolutePath();

            int diffPixels = diff.getDiffSize();
            long totalPixels = (long) baseline.getWidth() * baseline.getHeight();

            System.out.println("❌ Visual difference detected for: " + imageName);
            System.out.println("   Diff pixels: " + diffPixels + " / " + totalPixels);
            System.out.println("   Percentage: " + String.format("%.2f%%", (diffPixels / (double)totalPixels) * 100));

            return new VisualValidationResult(false, diffPixels, totalPixels, 
                baselineFile.getAbsolutePath(), actualFile.getAbsolutePath(), diffFilePath);
        } else {
            System.out.println("✓ UI matches baseline image for: " + imageName);
            return new VisualValidationResult(true, 0, baseline.getWidth() * baseline.getHeight(),
                baselineFile.getAbsolutePath(), actualFile.getAbsolutePath(), diffFilePath);
        }
    }

    /**
     * Update baseline with current screenshot (when UI intentionally changes)
     */
    public static void updateBaseline(WebDriver driver, String imageName) throws IOException {
        ensureDirectoryExists(BASELINE_DIR);

        // Delete old baseline
        File oldBaseline = new File(BASELINE_DIR + File.separator + imageName + ".png");
        if (oldBaseline.exists()) {
            oldBaseline.delete();
        }

        // Capture new baseline
        captureBaseline(driver, imageName);
        System.out.println("✓ Baseline updated: " + imageName);
    }

    /**
     * Get visual testing report paths
     */
    public static void printVisualReport() {
        // System.out.println("\n" + "=".repeat(80));
        System.out.println("VISUAL VALIDATION REPORT");
        // System.out.println("=".repeat(80));
        System.out.println("Baselines: " + new File(BASELINE_DIR).getAbsolutePath());
        System.out.println("Actuals:   " + new File(ACTUAL_DIR).getAbsolutePath());
        System.out.println("Diffs:     " + new File(DIFF_DIR).getAbsolutePath());
        // System.out.println("=".repeat(80) + "\n");
    }

    private static void ensureDirectoryExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
