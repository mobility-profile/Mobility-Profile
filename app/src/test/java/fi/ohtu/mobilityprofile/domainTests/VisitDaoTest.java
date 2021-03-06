package fi.ohtu.mobilityprofile.domainTests;

import android.location.Address;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.Visit;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class VisitDaoTest {

    private VisitDao visitDao;
    private Visit kumpula;
    private Visit hakaniemi;
    private Visit lauttasaari;

    @Before
    public void setUp() {
        visitDao = new VisitDao();
        Robolectric.setupActivity(MainActivityStub.class);
        createVisits();
    }

    @Test
    public void placeAndCoordinateAreSaveIntoDatabase() {
        visitDao.insert(kumpula);
        assertTrue(VisitDao.getLast().getPlace() != null && VisitDao.getLast().getPlace().getCoordinate() != null);
    }

    @Test
    public void testInsert() {
        visitDao.insert(kumpula);
        assertEquals(kumpula.getPlace().getCoordinate(), visitDao.getLast().getPlace().getCoordinate());
    }

    @Test
    public void testInsertMultiple() {
        visitDao.insert(kumpula);
        visitDao.insert(hakaniemi);
        visitDao.insert(lauttasaari);

        assertEquals(lauttasaari.getPlace().getCoordinate(), visitDao.getLast().getPlace().getCoordinate());
        assertEquals(3, VisitDao.getAll().size());
    }

    @Test
    public void testDeleteAll() {
        visitDao.insert(kumpula);
        visitDao.insert(hakaniemi);
        visitDao.insert(lauttasaari);

        assertEquals(3, VisitDao.getAll().size());
        visitDao.deleteAllData();
        assertEquals(0, VisitDao.getAll().size());
    }

    public void createVisits() {
        Place kump = new Place("Kumpula", new Address(Locale.getDefault()));
        Place haka = new Place("Hakaniemi", new Address(Locale.getDefault()));
        Place laut = new Place("Lauttasaari", new Address(Locale.getDefault()));
        kump.setCoordinate(new Coordinate(new Float(60.209108), new Float(24.964735)));
        haka.setCoordinate( new Coordinate(new Float(60.17885), new Float(24.95006)));
        laut.setCoordinate(new Coordinate(new Float(60.157330), new Float(24.877253)));
        kumpula = new Visit(123123, 232444, kump);
        hakaniemi = new Visit(238788, 343444, haka);
        lauttasaari = new Visit(454545,743433, laut);

    }
}
