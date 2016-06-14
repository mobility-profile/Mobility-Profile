package fi.ohtu.mobilityprofile.data;

import com.orm.query.Condition;
import com.orm.query.Select;

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
        List<Visit> visits = Select.from(Visit.class)
                .orderBy("timestamp DESC")
                .limit("1")
                .list();

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
        List<Visit> visits = Select.from(Visit.class)
                .where(Condition.prop("location").eq(location))
                .orderBy("timestamp DESC")
                .list();

        return visits;
    }

    /**
     * Saves a visit to the database.
     *
     * @param visit Visit to be saved
     */
    public void insertVisit(Visit visit) {
        visit.save();
    }
}
