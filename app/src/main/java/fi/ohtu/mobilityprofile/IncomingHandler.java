package fi.ohtu.mobilityprofile;

import android.app.Service;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

public class IncomingHandler extends Handler {
    private Service service;

    public IncomingHandler(Service service) {
        this.service = service;
    }

    @Override
    public void handleMessage(Message msg) {
        int what = msg.what;

        Toast.makeText(service.getApplicationContext(), "Remote Service invoked (" + what + ")", Toast.LENGTH_SHORT).show();

        // Setup the reply message
        Message message = Message.obtain(null, 2, 0, 0);
        try {
            // Make the RPC invocation
            Messenger replyTo = msg.replyTo;
            replyTo.send(message);
        } catch (RemoteException rme) {
            // Show an Error Message
            Toast.makeText(service, "Invocation failed!", Toast.LENGTH_SHORT).show();
        }
    }
}