package fi.ohtu.mobilityprofile.domainTests;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.StartLocationDao;
import fi.ohtu.mobilityprofile.data.TransportModeDao;
import fi.ohtu.mobilityprofile.domain.TransportMode;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class TransportModeDaoTest {
    private TransportModeDao transportModeDao;

    private TransportMode bus;
    private TransportMode train;

    @Before
    public void setUp() {
        this.transportModeDao = new TransportModeDao();
        Robolectric.setupActivity(MainActivityStub.class);
        createTransportModes();
    }

    @Test
    public void testGetPreferredTransportModes() {
        train.setFavourite(true);

        assertEquals(1, transportModeDao.getPreferredTransportModes().size());
        assertEquals("train", transportModeDao.getPreferredTransportModes().get(0).getName());
    }

    @Test
    public void testGetNoPreferredTransportModesWhenThereIsNonePreferred() {
        assertEquals(0, transportModeDao.getPreferredTransportModes().size());
    }

    @Test
    public void testGetNamesOfPreferredTransportModes() throws JSONException {
        train.setFavourite(true);
        JSONArray array = transportModeDao.getNamesOfPreferredTransportModes();

        assertEquals(1, array.length());
        assertEquals("train", array.get(0));
    }

    @Test
    public void testGetByName() {
        assertEquals(train.getName(), transportModeDao.getByName("train").getName());
    }

    @Test
    public void testDeleteAllData() {
        train.setFavourite(true);
        bus.setFavourite(true);

        assertEquals(2, transportModeDao.getPreferredTransportModes().size());
        transportModeDao.deleteAllData();
        assertEquals(0, transportModeDao.getPreferredTransportModes().size());
    }

    private void createTransportModes() {
        bus = new TransportMode("bus");
        train = new TransportMode("train");
        bus.save();
        train.save();
    }
}
