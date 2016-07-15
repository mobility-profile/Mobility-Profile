package fi.ohtu.mobilityprofile.remoteconnection;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;

import fi.ohtu.mobilityprofile.DestinationLogic;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.data.UserLocationDao;
import fi.ohtu.mobilityprofile.data.VisitDao;

/**
 * Used to enable cross-app communication.
 */
public class RemoteService extends Service {
    private Messenger messenger;

    @Override
    public IBinder onBind(Intent intent) {
        synchronized (RemoteService.class) {
            if (messenger == null) {
                CalendarTagDao calendarTagDao = new CalendarTagDao();
                VisitDao visitDao = new VisitDao(new UserLocationDao());
                RouteSearchDao routeSearchDao = new RouteSearchDao();
                FavouritePlaceDao favouritePlaceDao = new FavouritePlaceDao();
                messenger = new Messenger(new RequestHandler(this, new DestinationLogic(this, calendarTagDao, visitDao,
                        routeSearchDao, favouritePlaceDao), calendarTagDao, visitDao, routeSearchDao, favouritePlaceDao));

            }
        }

        return messenger.getBinder();
    }
}
