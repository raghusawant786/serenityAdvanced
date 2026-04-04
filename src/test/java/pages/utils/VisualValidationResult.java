package pages.utils;

/**
 * Data class holding detailed visual comparison results
 * Used for constructing comprehensive error messages with metrics
 */
public class VisualValidationResult {
    private boolean match;
    private int diffPixels;
    private double totalPixels;
    private double diffPercentage;
    private String baselinePath;
    private String actualPath;
    private String diffImagePath;

    public VisualValidationResult(boolean match, int diffPixels, double totalPixels,
                                  String baselinePath, String actualPath, String diffImagePath) {
        this.match = match;
        this.diffPixels = diffPixels;
        this.totalPixels = totalPixels;
        this.diffPercentage = totalPixels > 0 ? (diffPixels / totalPixels) * 100 : 0;
        this.baselinePath = baselinePath;
        this.actualPath = actualPath;
        this.diffImagePath = diffImagePath;
    }

    // Getters
    public boolean isMatch() {
        return match;
    }

    public int getDiffPixels() {
        return diffPixels;
    }

    public double getTotalPixels() {
        return totalPixels;
    }

    public double getDiffPercentage() {
        return diffPercentage;
    }

    public String getBaselinePath() {
        return baselinePath;
    }

    public String getActualPath() {
        return actualPath;
    }

    public String getDiffImagePath() {
        return diffImagePath;
    }
}
