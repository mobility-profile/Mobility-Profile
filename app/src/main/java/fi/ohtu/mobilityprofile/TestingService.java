package fi.ohtu.mobilityprofile;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

public class TestingService extends IntentService {

    public TestingService() {
        super("LocationUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("hehehehehe");
        Bundle bundle = intent.getExtras();

        if (bundle == null) {
            return;
        }

        Location location = bundle.getParcelable("com.google.android.location.LOCATION");

        if (location == null) {
            return;
        }

        System.out.println(location);

        LocationHandler.setLocation(location);
    }
}

