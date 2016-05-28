package fi.ohtu.mobilityprofile;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;

public class RemoteService extends Service {
    private Messenger messenger;

    @Override
    public IBinder onBind(Intent intent) {
        synchronized (RemoteService.class) {
            if (this.messenger == null) {
                this.messenger = new Messenger(new IncomingHandler(this));
            }
        }

        return this.messenger.getBinder();
    }
}