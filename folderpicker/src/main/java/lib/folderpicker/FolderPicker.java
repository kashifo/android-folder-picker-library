package lib.folderpicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FolderPicker extends Activity {

    //Folders and Files have separate lists because we show all folders first then files
    ArrayList<FilePojo> folderAndFileList;
    ArrayList<FilePojo> foldersList;
    ArrayList<FilePojo> filesList;

    TextView tv_title;
    TextView tv_location;

    String location = Environment.getExternalStorageDirectory().getAbsolutePath();
    boolean pickFiles;
    Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fp_main_layout);

        if (!isExternalStorageReadable()) {
            Toast.makeText(this, "Storage access permission not given", Toast.LENGTH_LONG).show();
            finish();
        }

        tv_title = (TextView) findViewById(R.id.fp_tv_title);
        tv_location = (TextView) findViewById(R.id.fp_tv_location);

        try {
            receivedIntent = getIntent();

            if (receivedIntent.hasExtra("title")) {
                String receivedTitle = receivedIntent.getExtras().getString("title");
                if (receivedTitle != null) {
                    tv_title.setText(receivedTitle);
                }
            }

            if (receivedIntent.hasExtra("location")) {
                String reqLocation = receivedIntent.getExtras().getString("location");
                if (reqLocation != null) {
                    File requestedFolder = new File(reqLocation);
                    if (requestedFolder.exists())
                        location = reqLocation;
                }
            }

            if (receivedIntent.hasExtra("pickFiles")) {
                pickFiles = receivedIntent.getExtras().getBoolean("pickFiles");
                if (pickFiles) {
                    findViewById(R.id.fp_btn_select).setVisibility(View.GONE);
                    findViewById(R.id.fp_btn_new).setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadLists(location);

    }

    /* Checks if external storage is available to at least read */
    boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    void loadLists(String location) {
        try {

            File folder = new File(location);

            if (!folder.isDirectory())
                exit();

            tv_location.setText("Location : " + folder.getAbsolutePath());
            File[] files = folder.listFiles();

            foldersList = new ArrayList<>();
            filesList = new ArrayList<>();

            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    FilePojo filePojo = new FilePojo(currentFile.getName(), true);
                    foldersList.add(filePojo);
                } else {
                    FilePojo filePojo = new FilePojo(currentFile.getName(), false);
                    filesList.add(filePojo);
                }
            }

            // sort & add to final List - as we show folders first add folders first to the final list
            Collections.sort(foldersList, comparatorAscending);
            folderAndFileList = new ArrayList<>();
            folderAndFileList.addAll(foldersList);

            //if we have to show files, then add files also to the final list
            if (pickFiles) {
                Collections.sort( filesList, comparatorAscending );
                folderAndFileList.addAll(filesList);
            }

            showList();

        } catch (Exception e) {
            e.printStackTrace();
        }

    } // load List


    Comparator<FilePojo> comparatorAscending = new Comparator<FilePojo>() {
        @Override
        public int compare(FilePojo f1, FilePojo f2) {
            return f1.getName().compareTo(f2.getName());
        }
    };


    void showList() {

        try {
            FolderAdapter FolderAdapter = new FolderAdapter(this, folderAndFileList);
            ListView listView = (ListView) findViewById(R.id.fp_listView);
            listView.setAdapter(FolderAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    listClick(position);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void listClick(int position) {

        if (pickFiles && !folderAndFileList.get(position).isFolder()) {
            String data = location + File.separator + folderAndFileList.get(position).getName();
            receivedIntent.putExtra("data", data);
            setResult(RESULT_OK, receivedIntent);
            finish();
        } else {
            location = location + File.separator + folderAndFileList.get(position).getName();
            loadLists(location);
        }

    }

    @Override
    public void onBackPressed(){
        goBack(null);
    }

    public void goBack(View v) {

        if (location != null && !location.equals("") && !location.equals("/")) {
            int start = location.lastIndexOf('/');
            String newLocation = location.substring(0, start);
            location = newLocation;
            loadLists(location);
        }else{
            exit();
        }

    }

    void exit(){
        setResult(RESULT_CANCELED, receivedIntent);
        finish();
    }

    void createNewFolder(String filename) {
        try {

            File file = new File(location + File.separator + filename);
            file.mkdirs();
            loadLists(location);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error:" + e.toString(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void newFolderDialog(View v) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Enter Folder Name");

        final EditText et = new EditText(this);
        dialog.setView(et);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Create",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        createNewFolder(et.getText().toString());
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        dialog.show();

    }


    public void select(View v) {

        if (pickFiles) {
            Toast.makeText(this, "You have to select a file", Toast.LENGTH_LONG).show();
        } else if (receivedIntent != null) {
            receivedIntent.putExtra("data", location);
            setResult(RESULT_OK, receivedIntent);
            finish();
        }
    }


    public void cancel(View v) {
        exit();
    }


} // class
