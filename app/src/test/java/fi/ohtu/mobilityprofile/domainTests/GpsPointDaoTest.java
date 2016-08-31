package fi.ohtu.mobilityprofile.domainTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.GpsPointDao;
import fi.ohtu.mobilityprofile.domain.GpsPoint;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class GpsPointDaoTest {
    private static fi.ohtu.mobilityprofile.data.GpsPointDao GpsPointDao;

    private GpsPoint kumpula;
    private GpsPoint hakaniemi;
    private GpsPoint lauttasaari;

    @Before
    public void setUp() {
        this.GpsPointDao = new GpsPointDao();
        Robolectric.setupActivity(MainActivityStub.class);
        createGpsPoints();
    }

   @Test
    public void testInsertAndFindLatest() {
        GpsPointDao.insert(kumpula);
        assertEquals(kumpula.getCoordinate(), GpsPointDao.getLatest().getCoordinate());
        GpsPointDao.insert(hakaniemi);
        assertEquals(hakaniemi.getCoordinate(), GpsPointDao.getLatest().getCoordinate());
        GpsPointDao.insert(lauttasaari);
        assertEquals(lauttasaari.getCoordinate(), GpsPointDao.getLatest().getCoordinate());
    }

    @Test
    public void testGetAll() {
        GpsPointDao.insert(kumpula);
        GpsPointDao.insert(hakaniemi);
        GpsPointDao.insert(lauttasaari);

        assertEquals(3, GpsPointDao.getAll().size());
    }

    @Test
    public void testDelete() {
        GpsPointDao.insert(kumpula);
        GpsPointDao.insert(hakaniemi);
        assertEquals(2, GpsPointDao.getAll().size());

        GpsPointDao.delete(kumpula);
        assertEquals(1, GpsPointDao.getAll().size());
        assertEquals(hakaniemi.getCoordinate(), GpsPointDao.getAll().get(0).getCoordinate());
    }

    @Test
    public void nothingIsDoneIfUnknownGpsPointIsDeleted() {
        GpsPointDao.insert(kumpula);
        assertEquals(1, GpsPointDao.getAll().size());

        GpsPointDao.delete(lauttasaari);
        assertEquals(1, GpsPointDao.getAll().size());
        assertEquals(kumpula.getCoordinate(), GpsPointDao.getAll().get(0).getCoordinate());
    }

    @Test
    public void testDeleteAllData() {
        GpsPointDao.insert(kumpula);
        GpsPointDao.insert(hakaniemi);
        GpsPointDao.insert(lauttasaari);

        assertEquals(3, GpsPointDao.getAll().size());
        GpsPointDao.deleteAllData();
        assertEquals(0, GpsPointDao.getAll().size());
    }

    private void createGpsPoints() {
        kumpula = new GpsPoint(122434, 50, new Float(60.209108), new Float(24.964735));
        hakaniemi = new GpsPoint(343543, 50, new Float(60.17885), new Float(24.95006));
        lauttasaari = new GpsPoint(565633, 50, new Float(60.157330), new Float(24.877253));
    }
}
