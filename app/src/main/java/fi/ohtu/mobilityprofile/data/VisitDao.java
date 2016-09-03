package fi.ohtu.mobilityprofile.data;

import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.Visit;

/**
 * VisitDAO is used for saving and reading Visits to/from the database.
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

    /**
     * Returns the latest Visit from the database
     * @return Visit
     */
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
        PlaceDao.insertPlace(visit.getPlace());
        visit.save();
    }

    /**
     * Deletes all Visits from the database
     */
    public static void deleteAllData() {
        Visit.deleteAll(Visit.class);
    }
}