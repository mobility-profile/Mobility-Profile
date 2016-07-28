package fi.ohtu.mobilityprofile.domainTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class SignificantPlaceDaoTest {
    private SignificantPlaceDao significantPlaceDao;

    @Before
    public void setUp() {
        this.significantPlaceDao = new SignificantPlaceDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testInsertandFindByLocation() {
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Kumpulan Kampus", new Float(111), new Float(222)));
        assertEquals("Kumpulan Kampus", significantPlaceDao.getSignificantPlaceBasedOnLocation("Kumpulan Kampus").getLocation());
    }

    @Test
    public void testInsertMultipleAndFindByLocation() {
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Kumpulan Kampus", new Float(111), new Float(222)));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Linnanmäki", new Float(333), new Float(444)));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Tikkurila", new Float(555), new Float(666)));

        assertEquals("Kumpulan Kampus", significantPlaceDao.getSignificantPlaceBasedOnLocation("Kumpulan Kampus").getLocation());
    }

    @Test
    public void testFindNothing() {
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Linnanmäki", new Float(333), new Float(444)));
        assertEquals(null, significantPlaceDao.getSignificantPlaceBasedOnLocation("Kumpulan Kampus"));
    }

    @Test
    public void deleteAll() {
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Kumpulan Kampus", new Float(111), new Float(222)));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Linnanmäki", new Float(333), new Float(444)));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Tikkurila", new Float(555), new Float(666)));

        List<SignificantPlace> places = significantPlaceDao.getAllSignificantPlaces();
        assertTrue(places.size() == 3);

        significantPlaceDao.deleteAllData();
        assertTrue(significantPlaceDao.getAllSignificantPlaces().size() == 0);
    }
}
