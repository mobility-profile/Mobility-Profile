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
    private String event;

    @Before
    public void setUp() throws Exception {
        calendarTagDao = mock(CalendarTagDao.class);
        visitDao = mock(VisitDao.class);

        mp = new MobilityProfile(calendarTagDao, visitDao, routeSearchDao);
        event = "Rautatieasema%02-02-2016";

        when(calendarTagDao.findTheMostUsedTag(anyString())).thenReturn(null);
    }

    @Test
    public void suggestsFirstLocationFromTheCalendar() throws Exception {
        mp.setCalendarEvent(event);

        String nextLocation = mp.getMostLikelyDestination("Kumpula");
        assertEquals("Rautatieasema", nextLocation);
    }

    @Test
    public void suggestHomeIfNoVisitsMade() {
        mp.setCalendarEvent(null);

        String nextLocation = mp.getMostLikelyDestination("Rovaniemi");
        assertEquals("home", nextLocation);

    }

    @Test
    public void suggestTheFirstVisitFromAllVisits() {
        mp.setCalendarEvent(null);
        visitDao.insertVisit(new Visit(1234, "Kumpula"));

        String nextLocation = mp.getMostLikelyDestination("Kumpula");
        assertEquals("Kumpula", nextLocation);
    }

}
