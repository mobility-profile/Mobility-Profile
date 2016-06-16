package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.data.Visit;
import fi.ohtu.mobilityprofile.data.VisitDao;

public class MobilityProfileTest {

    private MobilityProfile mp;
    private CalendarTagDao calendarTagDao;
    private VisitDao visitDao;
    private RouteSearchDao routeSearchDao;
    private String eventLocation;

    @Before
    public void setUp() throws Exception {
        calendarTagDao = mock(CalendarTagDao.class);
        visitDao = mock(VisitDao.class);

        mp = new MobilityProfile(calendarTagDao, visitDao, routeSearchDao);
        eventLocation = "Rautatieasema";

        when(calendarTagDao.findTheMostUsedTag(anyString())).thenReturn(null);
    }

    @Test
    public void suggestsFirstLocationFromTheCalendar() throws Exception {
        mp.setCalendarEventLocation(eventLocation);

        String nextLocation = mp.getMostLikelyDestination("Kumpula");
        assertEquals("Rautatieasema", nextLocation);
    }

    @Test
    public void suggestHomeIfNoVisitsMade() {
        mp.setCalendarEventLocation(null);

        String nextLocation = mp.getMostLikelyDestination("Rovaniemi");
        assertEquals("home", nextLocation);

    }

    @Test
    public void suggestTheFirstVisitFromAllVisits() {
        mp.setCalendarEventLocation(null);
        visitDao.insertVisit(new Visit(1234, "Kumpula"));

        String nextLocation = mp.getMostLikelyDestination("Kumpula");
        assertEquals("Kumpula", nextLocation);
    }

}
