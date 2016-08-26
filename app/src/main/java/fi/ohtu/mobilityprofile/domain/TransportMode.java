package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * Class is used to save Transport modes.
 */
public class TransportMode extends SugarRecord {

    private String name;
    private boolean favourite;

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
        this.save();
    }
}
