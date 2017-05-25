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

public class FolderPicker extends Activity {

    ArrayList<String> namesList;
    ArrayList<String> typesList;

    ArrayList<String> foldersList;
    ArrayList<String> filesList;

    TextView tv_title;
    TextView tv_location;

    String location = Environment.getExternalStorageDirectory().getAbsolutePath();
    boolean pickFiles;
    Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fp_main_layout);

        tv_title = (TextView) findViewById(R.id.fp_tv_title);
        tv_location = (TextView) findViewById(R.id.fp_tv_location);

        try {
            receivedIntent = getIntent();

            String receivedTitle = receivedIntent.getExtras().getString("title");
            if(receivedTitle!=null) {
                tv_title.setText(receivedTitle);
            }

            String reqLocation = receivedIntent.getExtras().getString("location");
            if(reqLocation!=null){
                File requestedFolder = new File(reqLocation);
                if( requestedFolder.exists() )
                    location = reqLocation;
            }

            pickFiles = receivedIntent.getExtras().getBoolean("pickFiles");
            if(pickFiles){
                findViewById(R.id.fp_btn_select).setVisibility(View.GONE);
                findViewById(R.id.fp_btn_new).setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadLists(location);

    }


    public void loadLists(String location) {
        try {

            File folder = new File(location);
            tv_location.setText( "Location : "+ folder.getAbsolutePath() );
            File[] files = folder.listFiles();

            foldersList = new ArrayList<>();
            filesList = new ArrayList<>();

            for ( File currentFile : files ) {
                if (currentFile.isDirectory()) {
                    foldersList.add(currentFile.getName());
                } else {
                    filesList.add(currentFile.getName());
                }
            }

            // sort & add to final List
            Collections.sort(foldersList);
            namesList = new ArrayList<>();
            namesList.addAll(foldersList);

            // add types
            typesList = new ArrayList<>();

            for (int i = 0; i < foldersList.size(); i++)
                typesList.add("folder");

            if (pickFiles) {
                Collections.sort(filesList);

                namesList.addAll(filesList);

                for (int i = 0; i < filesList.size(); i++)
                    typesList.add("file");
            }

            showList();

        } catch (Exception e) {
            e.printStackTrace();
        }

    } // load List

    public void showList() {

        try {
            simpleadapter sa = new simpleadapter(this, namesList, typesList);
            ListView lv = (ListView) findViewById(R.id.fp_listView);
            lv.setAdapter(sa);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


    public void listClick(int position) {

        if ( pickFiles && typesList.get(position).equals("file")) {
            String data = location + File.separator + namesList.get(position);
            receivedIntent.putExtra("data", data);
            setResult(RESULT_OK, receivedIntent);
            finish();
        } else {
            location = location + File.separator + namesList.get(position);
            loadLists(location);
        }

    }

    public void goBack(View v) {

        int start = location.lastIndexOf('/');
        String newLocation = location.substring(0, start);
        location = newLocation;
        loadLists(location);

    }

    public void newFolder(String filename) {
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
                        newFolder(et.getText().toString());
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
        finish();
    }


} // class
