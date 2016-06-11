package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class MobilityProfileTest {

    private MobilityProfile mp;
    ArrayList<String> events;

    @Before
    public void setUp() throws Exception {
        mp = new MobilityProfile();
        events = new ArrayList<>();
        events.add("Rautatieasema%02-02-2016");
        events.add("Kumpulan kampus%02-02-2016");
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
