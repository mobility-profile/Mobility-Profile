package fi.ohtu.mobilityprofile.domainTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.InterCitySearchDao;
import fi.ohtu.mobilityprofile.domain.InterCitySearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class InterCitySearchDaoTest {
    private InterCitySearchDao interCitySearchDao;

    @Before
    public void setUp() {
        interCitySearchDao = new InterCitySearchDao();
    }

    @Test
    public void testInsertInterCitySearch() {
        interCitySearchDao.insertInterCitySearch(new InterCitySearch("Helsinki", "Tampere", 122324));
        assertEquals(1, interCitySearchDao.getAllSearches().size());
    }

    @Test
    public void testDeleteAllData() {
        interCitySearchDao.insertInterCitySearch(new InterCitySearch("Helsinki", "Tampere", 122324));
        interCitySearchDao.insertInterCitySearch(new InterCitySearch("Vaasa", "Turku", 2312411));

        assertEquals(2, interCitySearchDao.getAllSearches().size());

        interCitySearchDao.deleteAllData();
        assertEquals(0, interCitySearchDao.getAllSearches().size());
    }
}
