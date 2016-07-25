package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class SignificantPlaceDaoTest {
    private SignificantPlaceDao significantPlaceDao;

    @Before
    public void setUp() {
        this.significantPlaceDao = new SignificantPlaceDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }
}
