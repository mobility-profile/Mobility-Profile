package fi.ohtu.mobilityprofile.domainTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.StartLocationDao;
import fi.ohtu.mobilityprofile.domain.StartLocation;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class StartLocationDaoTest {
    private static StartLocationDao startLocationDao;

    private StartLocation kumpula;
    private StartLocation hakaniemi;

    @Before
    public void setUp() {
        this.startLocationDao = new StartLocationDao();
        Robolectric.setupActivity(MainActivityStub.class);
        createStartLocations();
    }

    @Test
    public void testInsertAndGetLatestStartLocation() {
        startLocationDao.insert(kumpula);
        assertEquals(kumpula.getCoordinate(), startLocationDao.getStartLocation().getCoordinate());
    }

    @Test
    public void testInsertMultipleAndGetLatestStartLocation() {
        startLocationDao.insert(kumpula);
        startLocationDao.insert(hakaniemi);
        assertEquals(hakaniemi.getCoordinate(), startLocationDao.getStartLocation().getCoordinate());
    }

    private void createStartLocations() {
        kumpula = new StartLocation(123123, 50, new Float(60.209108), new Float(24.964735));
        hakaniemi = new StartLocation(342341, 50, new Float(60.17885), new Float(24.95006));
    }
}
