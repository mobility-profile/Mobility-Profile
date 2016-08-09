package fi.ohtu.mobilityprofile.data;

import android.location.Location;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.TestLocation;

/**
 * DAO used for clustering visited locations.
 */
public class TestLocationDao {

    public static void insert(TestLocation location) {
        location.save();
    }

}
