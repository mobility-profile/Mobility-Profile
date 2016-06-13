package fi.ohtu.mobilityprofile.data;

import java.util.List;

/**
 * DAO used for saving and reading Visits to/from the database.
 */
public class VisitDao {
    /**
     * Returns the latest visit from the database, or null if there is none.
     *
     * @return Latest visit
     */
    public Visit getLatestVisit() {
        List<Visit> visits = Visit.findWithQuery(Visit.class,
                "SELECT * FROM Visit " +
                        "ORDER BY timestamp DESC " +
                        "LIMIT 1");

        assert visits.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        if (visits.size() == 0) {
            return null;
        }

        return visits.get(0);
    }

    /**
     * Returns a list of visits where the location matches the given one.
     *
     * @param location Location of the visits
     * @return List of visits
     */
    public List<Visit> getVisitsByLocation(String location) {
        List<Visit> visits = CalendarTag.findWithQuery(Visit.class,
                "SELECT * FROM Visit " +
                        "WHERE location = ? " +
                        "ORDER BY timestamp DESC",
                location);

        return visits;
    }

    /**
     * Inserts a visit to the database. If there is at least one visit in the database already,
     * the latest visit's nextVisit will be set to point to the given visit.
     *
     * @param visit Visit to be saved
     */
    public void insertVisit(Visit visit) {
        Visit latestVisit = getLatestVisit();

        if (latestVisit != null) {
            latestVisit.nextVisit = visit;
            latestVisit.save();
        }

        visit.save();
    }
}
