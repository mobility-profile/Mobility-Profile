package fi.ohtu.mobilityprofile;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * Used for processing incoming requests from other apps.
 */
public class RequestHandler extends Handler {
    public static final int REQUEST_MOST_LIKELY_DESTINATION = 101;

    public static final int RESPOND_MOST_LIKELY_DESTINATION = 201;

    public static final int ERROR_CODE_NOT_FOUND = 401;

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

        Message message;
        switch (msg.what) {
            case REQUEST_MOST_LIKELY_DESTINATION:
                message = processDestinationRequest();
                break;
            default:
                message = processErrorMessage();
        }

        try {
            // Make the RPC invocation
            msg.replyTo.send(message);
        } catch (RemoteException rme) {
            Toast.makeText(context, "Invocation failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private Message processDestinationRequest() {
        // Setup the reply message
        Bundle bundle = new Bundle();
        bundle.putString("201", journeyPlanner.getMostLikelyDestination("FOR TESTING"));
        Message message = Message.obtain(null, RESPOND_MOST_LIKELY_DESTINATION);
        message.setData(bundle);

        return message;
    }

    private Message processErrorMessage() {
        Message message = Message.obtain(null, ERROR_CODE_NOT_FOUND);
        return message;
    }
}
