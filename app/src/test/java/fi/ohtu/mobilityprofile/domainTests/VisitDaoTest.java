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
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.Visit;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class VisitDaoTest {

    @Before
    public void setUp() {
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testInsert() {
        VisitDao.insert(new Visit(123, 124, new Place("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(1)))));
        assertEquals("Kumpula", VisitDao.getAll().get(0).getAddress());
    }

    @Test
    public void testInsertMultiple() {
        VisitDao.insert(new Visit(123, 124, new Place("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(1)))));
        VisitDao.insert(new Visit(345, 346, new Place("Kauppa", "Kauppakatu", new Coordinate(new Float(1), new Float(1)))));
        VisitDao.insert(new Visit(567, 568, new Place("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(1)))));

        assertEquals("Töölö", VisitDao.getLast().getAddress());
        assertEquals(3, VisitDao.getAll().size());
    }

    @Test
    public void testDeleteAll() {
        VisitDao.insert(new Visit(123, 124, new Place("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(1)))));
        VisitDao.insert(new Visit(345, 346, new Place("Kauppa", "Kauppakatu", new Coordinate(new Float(1), new Float(1)))));
        VisitDao.insert(new Visit(567, 568, new Place("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(1)))));
        assertEquals(3, VisitDao.getAll().size());
        VisitDao.deleteAll();
        assertEquals(0, VisitDao.getAll().size());

    }

}
