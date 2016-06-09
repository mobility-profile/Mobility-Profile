package fi.ohtu.mobilityprofile;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;

import fi.ohtu.mobilityprofile.data.CalendarTagDao;

/**
 * Used to enable cross-app communication.
 */
public class RemoteService extends Service {
    private Messenger messenger;

    @Override
    public IBinder onBind(Intent intent) {
        synchronized (RemoteService.class) {
            if (messenger == null) {
                messenger = new Messenger(new RequestHandler(this, new MobilityProfile()));
            }
        }

        return messenger.getBinder();
    }
}
