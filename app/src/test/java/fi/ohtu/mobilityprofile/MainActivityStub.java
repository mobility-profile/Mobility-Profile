package fi.ohtu.mobilityprofile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.orm.SugarContext;

public class MainActivityStub extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SugarContext.init(this);
    }
}
