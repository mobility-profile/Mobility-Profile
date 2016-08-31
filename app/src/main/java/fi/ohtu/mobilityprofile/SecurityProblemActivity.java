package fi.ohtu.mobilityprofile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

/**
 * Used to show information about application that have conflicting permissions defined.
 */
public class SecurityProblemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_problem);

        appendConflictInfo();
    }

    private void appendConflictInfo() {
        List<String> conflictApps = getIntent().getStringArrayListExtra(MainActivity.CONFLICT_APPS);
        StringBuilder conflictInfo = new StringBuilder();

        for (String conflictApp : conflictApps) {
            conflictInfo.append(conflictApp).append("\n");
        }

        TextView conflictAppsList = (TextView) findViewById(R.id.security_problem_conflict_apps_list);
        conflictAppsList.setText(conflictInfo);
    }
}
