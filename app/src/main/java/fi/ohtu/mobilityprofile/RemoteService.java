package fi.ohtu.mobilityprofile;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;

import com.orm.SugarContext;

import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.VisitDao;

/**
 * Used to enable cross-app communication.
 */
public class RemoteService extends Service {
    private Messenger messenger;

    @Override
    public IBinder onBind(Intent intent) {
        SugarContext.init(this);

        synchronized (RemoteService.class) {
            if (messenger == null) {
                CalendarTagDao calendarTagDao = new CalendarTagDao();
                VisitDao visitDao = new VisitDao();
                messenger = new Messenger(new RequestHandler(this, new MobilityProfile(calendarTagDao, visitDao), calendarTagDao, visitDao));
            }
        }

        return messenger.getBinder();
    }
}
