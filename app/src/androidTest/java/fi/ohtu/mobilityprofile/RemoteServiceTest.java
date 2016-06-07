package fi.ohtu.mobilityprofile;

import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class RemoteServiceTest {
    public RemoteServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
    }

    @Test
    public void testOnBind() {
        IBinder iBinder = new RemoteService().onBind(new Intent());
        assertTrue(iBinder != null);
    }
}
