package fi.ohtu.mobilityprofile.data;

import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.SignificantPlace;
import fi.ohtu.mobilityprofile.domain.Visit;

/**
 * DAO used for saving and reading Visits to/from the database.
 */
public class VisitDao {
    private SignificantPlaceDao significantPlaceDao;

    /**
     * Creates VisitDao.
     * @param significantPlaceDao SignificantPlaceDao
     */
    public VisitDao(SignificantPlaceDao significantPlaceDao) {
        this.significantPlaceDao = significantPlaceDao;
    }

    public VisitDao() {

    }

    /**
     * Returns a list of all Visits.
     * @return list of all visits
     */
    public List<Visit> getAllVisitsToSignificantPlaces() {
        return Select.from(Visit.class)
                .orderBy("timestamp DESC")
                .list();
    }

    public Visit getLastVisit() {
        return Select.from(Visit.class)
                .orderBy("timestamp DESC")
                .first();
    }

    /**
     * Saves a Visit to the database.
     * @param visit Visit to be saved
     */
    public void insertVisit(Visit visit) {
        visit.save();
    }

    /**
     * Deletes all Visits from the database
     */
    public void deleteAllData() {
        Visit.deleteAll(Visit.class);
    }
}