package fi.ohtu.mobilityprofile;

import android.content.Intent;
import android.os.IBinder;

import org.junit.Test;

import static org.junit.Assert.*;

public class RemoteServiceTest {
    @Test
    public void testOnBind() {
        IBinder iBinder = new RemoteService().onBind(new Intent());
        assertTrue(iBinder != null);
    }
}
