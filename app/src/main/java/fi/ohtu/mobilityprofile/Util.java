package fi.ohtu.mobilityprofile;

import java.sql.Time;
import java.util.Date;

public class Util {
    /**
     * Checks if the selected location was visited or set as destination around the same time in the past,
     * max x hours earlier or max y hours later than current time.
     *
     * @param visitTime    timestamp of the location
     * @param hoursEarlier hours earlier than current time
     * @param hoursLater   hours later than current time
     * @return true if location was visited within the time frame, false if not.
     */
    public static boolean aroundTheSameTime(Time visitTime, int hoursEarlier, int hoursLater) {
        Date currentTime = new Date(System.currentTimeMillis());

        int visitHour = visitTime.getHours();
        int visitMin = visitTime.getMinutes();
        int currentHour = currentTime.getHours();
        int currentMin = currentTime.getMinutes();

        if ((visitHour > currentHour - hoursEarlier || (visitHour == currentHour - hoursEarlier && visitMin >= currentMin))
                && (visitHour < currentHour + hoursLater || (visitHour == currentHour + hoursLater && visitMin <= currentMin))) {
            return true;
        }
        return false;
    }
}
