package fi.ohtu.mobilityprofile.suggestions;

/**
 * This class represents a route suggestion that can be made to the user.
 */
public class Suggestion {
    private String destination;
    private SuggestionAccuracy accuracy;
    private int source;

    /**
     * Creates the suggestion.
     *
     * @param destination Destination the user will be suggested to go
     * @param accuracy Estimated accuracy of the suggestion
     * @param source Source of the suggestion
     */
    public Suggestion(String destination, SuggestionAccuracy accuracy, int source) {
        this.destination = destination;
        this.accuracy = accuracy;
        this.source = source;
    }

    /**
     * Returns the suggested destination.
     *
     * @return Suggested destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Returns the estimated accuracy
     *
     * @return Estimated accuracy
     */
    public SuggestionAccuracy getAccuracy() {
        return accuracy;
    }

    /**
     * Returns the source of the suggestion
     *
     * @return Source of the suggestion
     */
    public int getSource() {
        return source;
    }
}