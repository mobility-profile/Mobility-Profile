package fi.ohtu.mobilityprofile.domainTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
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
        visitDao.insertVisit(new Visit(1223, new SignificantPlace("Kumpulan kampus", new Float(123), new Float(234))));
        assertEquals("Kumpulan Kampus", visitDao.getAllVisitsToSignificantPlaces().get(0).getSignificantPlace().getLocation());
    }

    @Test
    public void testInsertMultiple() {
        visitDao.insertVisit(new Visit(1223, new SignificantPlace("Kumpulan kampus", new Float(123), new Float(234))));
        visitDao.insertVisit(new Visit(1223, new SignificantPlace("Linnanmäki", new Float(234), new Float(111))));
        visitDao.insertVisit(new Visit(1223, new SignificantPlace("Tikkurila", new Float(1222), new Float(244))));
        assertTrue(visitDao.getAllVisitsToSignificantPlaces().size() == 3);
    }

    @Test
    public void testDeleteAll() {
        visitDao.insertVisit(new Visit(1223, new SignificantPlace("Kumpulan kampus", new Float(123), new Float(234))));
        visitDao.insertVisit(new Visit(1223, new SignificantPlace("Linnanmäki", new Float(234), new Float(111))));
        visitDao.insertVisit(new Visit(1223, new SignificantPlace("Tikkurila", new Float(1222), new Float(244))));
        assertTrue(visitDao.getAllVisitsToSignificantPlaces().size() == 3);

        visitDao.deleteAllData();
        assertTrue(visitDao.getAllVisitsToSignificantPlaces().size() == 0);
    }

}
