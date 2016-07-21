package fi.ohtu.mobilityprofile.remoteconnection;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.CalendarConnection;
import fi.ohtu.mobilityprofile.suggestions.CalendarSuggestions;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.MainActivity;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.data.UserLocationDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.suggestions.FavoriteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.RouteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;
import fi.ohtu.mobilityprofile.suggestions.locationHistory.VisitSuggestions;

/**
 * Used to enable cross-app communication.
 */
public class RemoteService extends Service {
    private Messenger messenger;

    @Override
    public IBinder onBind(Intent intent) {
        // If there is a security problem, we shouldn't allow any application to get information
        // from us. Check SecurityCheck.java for more information.
        if (!SecurityCheck.securityCheckOk(getSharedPreferences(MainActivity.SHARED_PREFERENCES, MODE_PRIVATE))) {
            return null;
        }

        synchronized (RemoteService.class) {
            if (messenger == null) {
                CalendarTagDao calendarTagDao = new CalendarTagDao();
                VisitDao visitDao = new VisitDao(new UserLocationDao());
                RouteSearchDao routeSearchDao = new RouteSearchDao();
                FavouritePlaceDao favouritePlaceDao = new FavouritePlaceDao();

                List<SuggestionSource> suggestionSources = new ArrayList<>();
                suggestionSources.add(new CalendarSuggestions(new CalendarConnection(this), calendarTagDao));
                suggestionSources.add(new VisitSuggestions(visitDao));
                suggestionSources.add(new RouteSuggestions(routeSearchDao));
                suggestionSources.add(new FavoriteSuggestions(favouritePlaceDao));
                DestinationLogic destinationLogic = new DestinationLogic(suggestionSources);

                messenger = new Messenger(new RequestHandler(this, destinationLogic, calendarTagDao, visitDao, routeSearchDao, favouritePlaceDao));

            }
        }

        return messenger.getBinder();
    }
}
