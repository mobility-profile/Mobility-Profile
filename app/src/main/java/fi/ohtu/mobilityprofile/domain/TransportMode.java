package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save Transportmode preferences.
 */
public class TransportMode extends SugarRecord {

    private String name;
    private boolean favourite;

    /**
     * Creates TransportMode.
     */
    public TransportMode() {
    }

    /**
     * Creates TransportMode.
     * @param name name of the transport mode
     */
    public TransportMode(String name) {
        this.name = name;
        this.favourite = false;
    }

    public String getName() {
        return name;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
