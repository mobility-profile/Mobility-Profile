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
        return getLatestVisit(Select.from(Visit.class)
                .orderBy("timestamp DESC")
                .limit("1"));
    }

    public Visit getLatestVisit(int visitType) {
        return getLatestVisit(Select.from(Visit.class)
                .where(Condition.prop("visit_type").eq(visitType))
                .orderBy("timestamp DESC")
                .limit("1"));
    }

    private Visit getLatestVisit(Select<Visit> query) {
        List<Visit> visits = query.list();

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

        //testing
//        Visit visit = new Visit(1, "naantali");
//        visit.save();
//        visits.add(0, visit);

        return visits;
    }

    public List<Visit> getVisitsByLocation(String location, int visitType) {
        List<Visit> visits = Select.from(Visit.class)
                .where(Condition.prop("location").eq(location))
                .and(Condition.prop("visit_type").eq(visitType))
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
