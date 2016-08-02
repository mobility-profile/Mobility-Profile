package fi.ohtu.mobilityprofile.domainTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;
import fi.ohtu.mobilityprofile.domain.Visit;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class VisitDaoTest {
    private VisitDao visitDao;

    @Before
    public void setUp() {
        this.visitDao = new VisitDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testInsert() {

    }

    @Test
    public void testInsertMultiple() {

    }

    @Test
    public void testDeleteAll() {


    }

}
