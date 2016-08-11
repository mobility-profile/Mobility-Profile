package fi.ohtu.mobilityprofile.suggestions;

import fi.ohtu.mobilityprofile.domain.Coordinate;

/**
 * This class represents a route suggestion that can be made to the user.
 */
public class Suggestion {
    private String destination;
    private SuggestionAccuracy accuracy;
    private int source;
    private Coordinate coordinate;

    /**
     * Creates the suggestion.
     *
     * @param destination Destination the user will be suggested to go
     * @param accuracy Estimated accuracy of the suggestion
     * @param source Source of the suggestion
     * @param coordinate coordinates of the suggestion location
     */
    public Suggestion(String destination, SuggestionAccuracy accuracy, int source, Coordinate coordinate) {
        this.destination = destination;
        this.accuracy = accuracy;
        this.source = source;
        this.coordinate = coordinate;
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

    public Coordinate getCoordinate() {
        return coordinate;
    }
}