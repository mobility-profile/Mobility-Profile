package fi.ohtu.mobilityprofile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class BackUpActivity extends AppCompatActivity {

    private ProfileBackup profileBackup;
    private Button backupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        profileBackup = new ProfileBackup(this);
        backupButton = (Button) findViewById(R.id.backup_button);

        setListenerForBackupButton();
    }

    private void setListenerForBackupButton() {
        backupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileBackup.handleBackup("back up");
            }
        });
    }
}
