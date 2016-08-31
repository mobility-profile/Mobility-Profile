package fi.ohtu.mobilityprofile.suggestions;

import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;

/**
 * This class represents a destination suggestion that can be made for the user.
 */
public class Suggestion implements Comparable {
    private Place destination;
    private SuggestionAccuracy accuracy;
    private int source;

    /**
     * Creates the suggestion.
     *
     * @param destination Destination the user will be suggested to go
     * @param accuracy Estimated accuracy of the suggestion
     * @param source Source of the suggestion
     */
    public Suggestion(Place destination, SuggestionAccuracy accuracy, int source) {
        this.destination = destination;
        this.accuracy = accuracy;
        this.source = source;
    }

    /**
     * Returns the suggested destination.
     *
     * @return Suggested destination
     */
    public Place getDestination() {
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
        return this.destination.getCoordinate();
    }

    /**
     * Compares this Suggestion to given Suggestion based on SuggestionAccuracy.
     *
     * @param another the Suggestion to compare to this Suggestion.
     * @return a negative integer if this Suggestion's SuggestionAccuracy is lower than the accuracy of the given Suggestion,
     * a positive integer if this accuracy is greater than given accuracy,
     * 0 if accuracies are the same.
     */
    @Override
    public int compareTo(Object another) {
        return this.accuracy.compareTo(((Suggestion) another).accuracy);
    }
}