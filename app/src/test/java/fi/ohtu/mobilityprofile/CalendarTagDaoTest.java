package fi.ohtu.mobilityprofile;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;

import com.orm.SugarContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.data.CalendarTag;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class, sdk = 21)
public class CalendarTagDaoTest {
    private CalendarTagDao calendarTagDao;

    @Before
    public void setUp() {
        calendarTagDao = new CalendarTagDao();
        Robolectric.setupActivity(MainActivityTest.class);
    }

    @Test
    public void testInsertAndFind() {
        calendarTagDao.insertCalendarTag(new CalendarTag("Kumpula", "Helsinki"));

        assertEquals("Helsinki", calendarTagDao.findTheMostUsedTag("Kumpula").getValue());
    }

    @Test
    public void testFindUnknown() {
        assertEquals(null, calendarTagDao.findTheMostUsedTag("Kumpula"));

        calendarTagDao.insertCalendarTag(new CalendarTag("Kumpula", "Helsinki"));

        assertEquals(null, calendarTagDao.findTheMostUsedTag("asdf"));
    }

    @Test
    public void testFindMultiple() {
        calendarTagDao.insertCalendarTag(new CalendarTag("Kumpula", "Helsinki"));
        calendarTagDao.insertCalendarTag(new CalendarTag("Kumpula", "Keskusta"));
        calendarTagDao.insertCalendarTag(new CalendarTag("Kumpula", "Keskusta"));

        assertEquals("Keskusta", calendarTagDao.findTheMostUsedTag("Kumpula").getValue());
    }

    @Test
    public void testFindMultiple2() {
        calendarTagDao.insertCalendarTag(new CalendarTag("Oulunkylä", "Helsinki"));
        calendarTagDao.insertCalendarTag(new CalendarTag("Oulunkylä", "Kaupunki"));
        calendarTagDao.insertCalendarTag(new CalendarTag("Kumpula", "Kaupunki"));
        calendarTagDao.insertCalendarTag(new CalendarTag("Kumpula", "Tali"));
        calendarTagDao.insertCalendarTag(new CalendarTag("Kumpula", "Tali"));
        calendarTagDao.insertCalendarTag(new CalendarTag("Oulunkylä", "Myllypuro"));
        calendarTagDao.insertCalendarTag(new CalendarTag("Oulunkylä", "Myllypuro"));
        calendarTagDao.insertCalendarTag(new CalendarTag("Kumpula", "Myllypuro"));

        assertEquals("Tali", calendarTagDao.findTheMostUsedTag("Kumpula").getValue());
        assertEquals("Myllypuro", calendarTagDao.findTheMostUsedTag("Oulunkylä").getValue());
    }
}
