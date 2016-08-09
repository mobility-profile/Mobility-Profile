package fi.ohtu.mobilityprofile.domainTests;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.GpsPointDao;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class GpsPointDaoTest {
    private static GpsPointDao GpsPointDao;

    @Before
    public void setUp() {
        this.GpsPointDao = new GpsPointDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }

  /*  @Test
    public void testInsertAndFindLatest() {
        GpsPointDao.insert(new GPSPoint(1234, "Kumpula"));
        assertEquals("Kumpula", GpsPointDao.getLatest().getOriginalLocation());
        GpsPointDao.insert(new GPSPoint(1300, "Herttoniemi"));
        assertEquals("Herttoniemi", GpsPointDao.getLatest().getOriginalLocation());
        GpsPointDao.insert(new GPSPoint(1100, "Lammassaari"));
        assertEquals("Herttoniemi", GpsPointDao.getLatest().getOriginalLocation());
    }
    @Test
    public void testInsertAndFindByLocation() {
        GpsPointDao.insert(new GPSPoint(2345, "Helsinki"));
        List<GPSPoint> places = GpsPointDao.getVisitsByLocation("Helsinki");
        assertEquals(1, places.size());
        assertEquals("Helsinki", places.get(0).getOriginalLocation());
    }
    @Test
    public void testMultipleInsertAndFindByLocation() {
        GpsPointDao.insert(new GPSPoint(123, "Kumpula"));
        GpsPointDao.insert(new GPSPoint(234, "Kumpula"));
        GpsPointDao.insert(new GPSPoint(345, "Kalasatama"));
        GpsPointDao.insert(new GPSPoint(456, "Tikkurila"));
        GpsPointDao.insert(new GPSPoint(987, "Kumpula"));
        GpsPointDao.insert(new GPSPoint(567, "Kumpulan kampus"));
        List<GPSPoint> places = GpsPointDao.getVisitsByLocation("Kumpula");
        assertEquals(3, places.size());
        assertEquals("Kumpula", places.get(0).getOriginalLocation());
        assertEquals("Kumpula", places.get(1).getOriginalLocation());
        assertEquals("Kumpula", places.get(2).getOriginalLocation());
        List<GPSPoint> visits2 = GpsPointDao.getVisitsByLocation("Tikkurila");
        assertEquals(1, visits2.size());
        assertEquals("Tikkurila", visits2.get(0).getOriginalLocation());
    }
    @Test
    public void testFindNothing() {
        assertTrue(GpsPointDao.getLatest() == null);
        assertTrue(GpsPointDao.getVisitsByLocation("Kumpula").isEmpty());
        GpsPointDao.insert(new GPSPoint(234, "Kumpula"));
        assertTrue(GpsPointDao.getVisitsByLocation("Herttoniemi").isEmpty());
    }
    @Test
    public void testDeleteAll() {
        GpsPointDao.insert(new GPSPoint(123, "Kumpula"));
        GpsPointDao.insert(new GPSPoint(234, "Kumpula"));
        assertEquals(2, GpsPointDao.getVisitsByLocation("Kumpula").size());
        fi.ohtu.mobilityprofile.data.GpsPointDao.deleteAll();
        assertEquals(0, GpsPointDao.getVisitsByLocation("Kumpula").size());
    }*/
}