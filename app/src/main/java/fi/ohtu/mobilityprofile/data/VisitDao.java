package fi.ohtu.mobilityprofile.data;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * DAO used for saving and reading Visits to/from the database.
 */
public class VisitDao {
    private UserLocationDao userLocationDao;

    /**
     * Creates VisitDao.
     * @param userLocationDao 
     */
    public VisitDao(UserLocationDao userLocationDao) {
        this.userLocationDao = userLocationDao;
    }

    /**
     * Returns the latest visit from the database, or null if there is none.
     *
     * @return Latest visit
     */
    public Visit getLatestVisit() {
        return getLatestVisit(Select.from(Visit.class)
                .orderBy("timestamp DESC")
                .limit("1"));
    }

    /**
     * Returns the latest visit from the database based on custom query,
     * or null if there is none.
     * @param query custom query
     * @return latest visit
     */
    private Visit getLatestVisit(Select<Visit> query) {
        List<Visit> visits = query.list();

        assert visits.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        if (visits.size() == 0) {
            return null;
        }

        return visits.get(0);
    }

    /**
     * Returns a list of visits where the nearestKnownLocation matches the given one.
     *
     * @param location Location of the visits
     * @return List of visits
     */
    public List<Visit> getVisitsByLocation(String location) {
        List<Visit> visits = Select.from(Visit.class)
                .where(Condition.prop("original_location").eq(location))
                .orderBy("timestamp DESC")
                .list();

        return visits;
    }

    /**
     * Returns a list of all visits.
     * @return list of visits
     */
    public List<Visit> getAllVisits() {
        return Select.from(Visit.class)
                .orderBy("timestamp DESC")
                .list();
    }

    /**
     * Saves a visit to the database. Also sets the visit's nearest known location.
     *
     * @param visit Visit to be saved
     */
    public void insertVisit(Visit visit) {
        UserLocation nearestLocation = userLocationDao.getNearestLocation(visit.getOriginalLocation(), 50);
        visit.nearestKnownLocation = nearestLocation;
        visit.save();
    }
}
