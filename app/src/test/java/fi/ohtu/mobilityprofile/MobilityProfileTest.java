package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.Visit;
import fi.ohtu.mobilityprofile.data.VisitDao;

public class MobilityProfileTest {

    private MobilityProfile mp;
    private CalendarTagDao calendarTagDao;
    private VisitDao visitDao;
    ArrayList<String> events;

    @Before
    public void setUp() throws Exception {
        calendarTagDao = mock(CalendarTagDao.class);
        visitDao = mock(VisitDao.class);

        mp = new MobilityProfile(calendarTagDao, visitDao);
        events = new ArrayList<>();
        events.add("Rautatieasema%02-02-2016");
        events.add("Kumpulan kampus%02-02-2016");

        when(calendarTagDao.findTheMostUsedTag(anyString())).thenReturn(null);
    }

    @Test
    public void suggestsFirstLocationFromTheEventList() throws Exception {
        mp.setCalendarEventList(events);

        String nextLocation = mp.getMostLikelyDestination("Kumpula");
        assertEquals("Rautatieasema", nextLocation);
    }

    @Test
    public void suggestsTheFirstValidLocationFromTheEventListIfItContainsNullEventLocations() {
        ArrayList<String> nullEvent = new ArrayList<>();
        nullEvent.add("null%");
        nullEvent.add("Rautatieasema%");
        mp.setCalendarEventList(nullEvent);

        String nextLocation = mp.getMostLikelyDestination("Kumpula");
        assertEquals("Rautatieasema", nextLocation);
    }


}
