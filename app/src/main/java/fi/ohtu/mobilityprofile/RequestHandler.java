package fi.ohtu.mobilityprofile;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * Used for processing incoming requests from other apps.
 */
public class RequestHandler extends Handler {
    public static final int REQUEST_MOST_LIKELY_DESTINATION = 101;
    public static final int RESPOND_MOST_LIKELY_DESTINATION = 201;

    private Context context;
    private JourneyPlanner journeyPlanner;

    /**
     * Creates the RequestHandler.
     *
     * @param context Context used for toast messages
     * @param journeyPlanner Journey planner that provides the logic for our app
     */
    public RequestHandler(Context context, JourneyPlanner journeyPlanner) {
        this.context = context;
        this.journeyPlanner = journeyPlanner;
    }

    @Override
    public void handleMessage(Message msg) {
        // For testing
        Toast.makeText(context.getApplicationContext(), "Remote Service invoked (" + msg.what + ")", Toast.LENGTH_SHORT).show();

        switch (msg.what) {
            case REQUEST_MOST_LIKELY_DESTINATION:
                processDestinationRequest(msg.replyTo);
                break;
        }


    }

    private void processDestinationRequest(Messenger messenger) {
        // Setup the reply message
        Bundle bundle = new Bundle();
        bundle.putString("101", journeyPlanner.getMostLikelyDestination("FOR TESTING"));
        Message message = Message.obtain(null, RESPOND_MOST_LIKELY_DESTINATION);
        message.setData(bundle);
        try {
            // Make the RPC invocation
            messenger.send(message);
        } catch (RemoteException rme) {
            Toast.makeText(context, "Invocation failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
