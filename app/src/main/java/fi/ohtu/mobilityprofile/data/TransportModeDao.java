package fi.ohtu.mobilityprofile.data;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.TransportMode;

/**
 * DAO used for reading TransportModes from the database.
 */
public class TransportModeDao {

    public TransportModeDao() {
    }

    public List<TransportMode> getPreferredTransportModes() {
        List<TransportMode> modes = TransportMode.listAll(TransportMode.class);
        List<TransportMode> remove = new ArrayList<>();

        for (TransportMode m : modes) {
            if (!m.isFavourite()) {
                remove.add(m);
            }
        }

        modes.removeAll(remove);

        return modes;
    }

    public ArrayList<String> getNamesOfPreferredTransportModes() {
        ArrayList<String> modes = new ArrayList<>();
        for (TransportMode m : getPreferredTransportModes()) {
            modes.add(m.getName());
        }

        return modes;
    }
}
