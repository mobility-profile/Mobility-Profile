package fi.ohtu.mobilityprofile.suggestions;

public class Suggestion {
    private String destination;
    private SuggestionAccuracy accuracy;
    private int source;

    public Suggestion(String destination, SuggestionAccuracy accuracy, int source) {
        this.destination = destination;
        this.accuracy = accuracy;
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public SuggestionAccuracy getAccuracy() {
        return accuracy;
    }

    public int getSource() {
        return source;
    }
}
