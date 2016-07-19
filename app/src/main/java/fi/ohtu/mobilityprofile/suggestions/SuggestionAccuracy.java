package fi.ohtu.mobilityprofile.suggestions;

public enum SuggestionAccuracy {
    VERY_HIGH(8),
    HIGH(6),
    MODERATE(4),
    LOW(2),
    VERY_LOW(0);

    private final int accuracy;

    SuggestionAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getAccuracy() {
        return accuracy;
    }
}
