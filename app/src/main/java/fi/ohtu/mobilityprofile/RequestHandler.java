package fi.ohtu.mobilityprofile;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

import static fi.ohtu.mobilityprofile.RequestCode.*;

/**
 * Used for processing incoming requests from other apps.
 */
public class RequestHandler extends Handler {
    private Context context;
    private MobilityProfile mobilityProfile;

    /**
     * Creates the RequestHandler.
     *
     * @param context Context used for toast messages
     * @param mobilityProfile Journey planner that provides the logic for our app
     */
    public RequestHandler(Context context, MobilityProfile mobilityProfile) {
        this.context = context;
        this.mobilityProfile = mobilityProfile;
    }

    @Override
    public void handleMessage(Message msg) {
        // For testing
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), "Remote Service invoked (" + msg.what + ")", Toast.LENGTH_SHORT).show();
        }

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
            if (context != null) {
                Toast.makeText(context, "Invocation failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Message processDestinationRequest() {
        // Setup the reply message
        Bundle bundle = new Bundle();
        bundle.putString("201", mobilityProfile.getMostLikelyDestination("FOR TESTING"));
        Message message = Message.obtain(null, RESPOND_MOST_LIKELY_DESTINATION);
        message.setData(bundle);

        return message;
    }

    private Message processErrorMessage() {
        Message message = Message.obtain(null, ERROR_UNKNOWN_CODE);
        return message;
    }
}
