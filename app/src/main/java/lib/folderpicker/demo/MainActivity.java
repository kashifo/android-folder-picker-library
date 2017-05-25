package lib.folderpicker.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import lib.folderpicker.FolderPicker;

public class MainActivity extends AppCompatActivity {

    private static final int SDCARD_PERMISSION = 12;
    private static final int FILEPICKER_CODE = 786;
    TextView tv_folderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_folderPath = (TextView) findViewById(R.id.textView);
    }

    public void pickFile(View v) {

        if (Build.VERSION.SDK_INT < 23) {

            initPicker();

        } else {

            if (storagePermissionAvailable()) {

                initPicker();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        SDCARD_PERMISSION);
            }

        }

    }


    boolean storagePermissionAvailable() {
        // For api Level 23 and above.
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SDCARD_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initPicker();

                }
        }
    }


    void initPicker() {
        Intent intent = new Intent(this, FolderPicker.class);

        //Optional

        //intent.putExtra("title", "Select your backup folder");
        //intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        //intent.putExtra("pickFiles", true);

        //Optional

        startActivityForResult(intent, FILEPICKER_CODE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILEPICKER_CODE && resultCode == Activity.RESULT_OK) {
            String folderLocation = intent.getExtras().getString("data");
            tv_folderPath.setText(folderLocation);
        }
    }

}
