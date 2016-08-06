package fi.ohtu.mobilityprofile.data;

import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.Visit;

/**
 * DAO used for saving and reading Visits to/from the database.
 */
public class VisitDao {

    /**
     * Returns a list of all Visits.
     * @return list of all visits
     */
    public static List<Visit> getAll() {
        return Select.from(Visit.class)
                .orderBy("enter_time DESC")
                .list();
    }

    public static Visit getLast() {
        return Select.from(Visit.class)
                .orderBy("enter_time DESC")
                .first();
    }

    /**
     * Saves a Visit to the database.
     * @param visit Visit to be saved
     */
    public static void insert(Visit visit) {
        visit.getPlace().getCoordinate().save();
        visit.getPlace().save();
        visit.save();
    }

    /**
     * Deletes all Visits from the database
     */
    public static void deleteAll() {
        Visit.deleteAll(Visit.class);
    }
}