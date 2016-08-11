package fi.ohtu.mobilityprofile.data;

import com.orm.query.Select;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.TransportMode;

/**
 * DAO used for reading TransportModes from the database.
 */
public class TransportModeDao {

    /**
     * Creates TransportModeDao.
     */
    public TransportModeDao() {
    }

    /**
     * Returns a list of preferred transport modes.
     * @return list of transport modes
     */
    public static List<TransportMode> getPreferredTransportModes() {
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

    /**
     * Returns names of the preferred transport modes in JsonArray.
     * @return jsonArray of transport mode names
     */
    public static JSONArray getNamesOfPreferredTransportModes() {
        JSONArray modes = new JSONArray();
        for (TransportMode m : getPreferredTransportModes()) {
            modes.put(m.getName());
        }
        return modes;
    }

    /**
     * Deletes all TransportModes from the database.
     */
    public void deleteAllData() {
        TransportMode.deleteAll(TransportMode.class);
    }
}
