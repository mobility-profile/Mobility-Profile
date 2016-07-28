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

    }

    @Test
    public void testInsertMultipleAndFindByLocation() {

    }

    @Test
    public void testFindNothing() {

    }

    @Test
    public void deleteAll() {

    }
}
