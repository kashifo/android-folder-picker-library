package lib.folderpicker.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import lib.folderpicker.FolderPicker;

public class MainActivity extends AppCompatActivity {

    private static final int SDCARD_PERMISSION_FOLDER = 12,
            SDCARD_PERMISSION_FILE = 123,
            FOLDER_PICKER_CODE = 78,
            FILE_PICKER_CODE = 786;

    TextView tv_folderPath, tv_filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_folderPath = (TextView) findViewById(R.id.tv_folder);
        tv_filePath = (TextView) findViewById(R.id.tv_file);
    }

    public void pickFolder(View v) {
        pickFolderOrFile(true);
    }

    public void pickFile(View v) {
        pickFolderOrFile(false);
    }

    void pickFolderOrFile(boolean folder) {

        if (Build.VERSION.SDK_INT < 23) {

            if (folder)
                pickFolder();
            else
                pickFile();

        } else {

            if (storagePermissionAvailable()) {

                if (folder)
                    pickFolder();
                else
                    pickFile();

            } else {
                if (folder) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            SDCARD_PERMISSION_FOLDER);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            SDCARD_PERMISSION_FILE);
                }
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
            case SDCARD_PERMISSION_FOLDER:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    pickFolder();

                }
                break;

            case SDCARD_PERMISSION_FILE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    pickFile();

                }
                break;
        }
    }


    void pickFolder() {
        Intent intent = new Intent(this, FolderPicker.class);
        startActivityForResult(intent, FOLDER_PICKER_CODE);
    }

    void pickFile() {
        Intent intent = new Intent(this, FolderPicker.class);

        //Optional

        intent.putExtra("title", "Select your backup folder");
        intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        intent.putExtra("pickFiles", true);

        //Optional

        startActivityForResult(intent, FILE_PICKER_CODE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FOLDER_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            String folderLocation = intent.getExtras().getString("data");
            tv_folderPath.setText(folderLocation);
        } else if (requestCode == FILE_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            String folderLocation = intent.getExtras().getString("data");
            tv_filePath.setText(folderLocation);
        }
    }

}
