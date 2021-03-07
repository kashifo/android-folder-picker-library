package lib.folderpicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
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

    Comparator<FilePojo> comparatorAscending = new Comparator<FilePojo>() {
        @Override
        public int compare(FilePojo f1, FilePojo f2) {
            return f1.getName().compareTo(f2.getName());
        }
    };

    public static final String EXTRA_DATA = "data";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DESCRIPTION = "desc";
    public static final String EXTRA_LOCATION = "location";
    public static final String EXTRA_PICK_FILES = "pickFiles";
    public static final String EXTRA_EMPTY_FOLDER = "emptyFolder";
    //Folders and Files have separate lists because we show all folders first then files
    ArrayList<FilePojo> mFolderAndFileList;
    ArrayList<FilePojo> mFoldersList;
    ArrayList<FilePojo> mFilesList;

    TextView mTvLocation;

    String mLocation;
    boolean mPickFiles;
    Intent mReceivedIntent;
    boolean mEmptyFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fp_main_layout);

        if (!isExternalStorageReadable()) {
            Toast.makeText(this, getString(R.string.no_access_to_storage), Toast.LENGTH_LONG).show();
            finish();
        }

        String location = Environment.getExternalStorageDirectory().getAbsolutePath();

        mTvLocation = findViewById(R.id.fp_tv_location);

        try {
            mReceivedIntent = getIntent();

            if (mReceivedIntent.hasExtra(EXTRA_TITLE)) {
                String title = mReceivedIntent.getStringExtra(EXTRA_TITLE);
                if (title != null) {
                    ((TextView)findViewById(R.id.fp_tv_title)).setText(title);
                }
            }

            if (mReceivedIntent.hasExtra(EXTRA_DESCRIPTION)) {
                String desc = mReceivedIntent.getStringExtra(EXTRA_DESCRIPTION);
                if (desc != null) {
                    TextView textView = findViewById(R.id.fp_tv_desc);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(desc);
                }
            }

            if (mReceivedIntent.hasExtra(EXTRA_LOCATION)) {
                String newLocation = mReceivedIntent.getStringExtra(EXTRA_LOCATION);
                if (newLocation != null) {
                    File folder = new File(newLocation);
                    if (folder.exists())
                        location = newLocation;
                }
            }

            if (mReceivedIntent.hasExtra(EXTRA_PICK_FILES)) {
                mPickFiles = mReceivedIntent.getBooleanExtra(EXTRA_PICK_FILES, false);
                if (mPickFiles) {
                    findViewById(R.id.fp_btn_select).setVisibility(View.GONE);
                    findViewById(R.id.fp_btn_new).setVisibility(View.GONE);
                }
            }

            if (mReceivedIntent.hasExtra(EXTRA_EMPTY_FOLDER)) {
                mEmptyFolder = mReceivedIntent.getBooleanExtra(EXTRA_EMPTY_FOLDER, false);
                if (mEmptyFolder) {
                    findViewById(R.id.fp_tv_empty_dir).setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        checkAndLoadLists(location);
    }

    /**
     * Checks if external storage is available to at least read
     */
    boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    boolean checkAndLoadLists(String location, boolean showToast) {
        if (checkLocation(location, showToast)) {
            mLocation = location;
            loadLists();
            return true;
        }
        return false;
    }

    boolean checkAndLoadLists(String location) {
        return checkAndLoadLists(location, true);
    }

    /**
     * Check location and load lists if location is correct.
     * @param location
     * @param showToast
     * @return
     */
    private boolean checkLocation(String location, boolean showToast) {
        File folder = new File(location);

        if (!folder.exists()) {
            if (showToast) {
                Toast.makeText(this, R.string.dir_is_not_exist, Toast.LENGTH_LONG).show();
            }
            return false;
        }

        if (!folder.isDirectory()) {
            if (showToast) {
                Toast.makeText(this, R.string.is_not_dir, Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Load lists and show.
     */
    void loadLists() {
        try {
            File folder = new File(mLocation);

            mTvLocation.setText(String.format(getString(R.string.location_mask), folder.getAbsolutePath()));
            File[] files = folder.listFiles();

            mFoldersList = new ArrayList<>();
            mFilesList = new ArrayList<>();

            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    FilePojo filePojo = new FilePojo(currentFile.getName(), true);
                    mFoldersList.add(filePojo);
                } else {
                    FilePojo filePojo = new FilePojo(currentFile.getName(), false);
                    mFilesList.add(filePojo);
                }
            }

            // sort & add to final List - as we show folders first add folders first to the final list
            Collections.sort(mFoldersList, comparatorAscending);
            mFolderAndFileList = new ArrayList<>();
            mFolderAndFileList.addAll(mFoldersList);

            //if we have to show files, then add files also to the final list
            if (mPickFiles) {
                Collections.sort(mFilesList, comparatorAscending );
                mFolderAndFileList.addAll(mFilesList);
            }

            showList();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Show list of folders and files.
     */
    void showList() {
        try {
            FolderAdapter FolderAdapter = new FolderAdapter(this, mFolderAndFileList);
            ListView listView = findViewById(R.id.fp_listView);
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

    /**
     * Click on the list item.
     * @param position
     */
    void listClick(int position) {
        if (mPickFiles && !mFolderAndFileList.get(position).isFolder()) {
            String data = mLocation + File.separator + mFolderAndFileList.get(position).getName();
            mReceivedIntent.putExtra(EXTRA_DATA, data);
            setResult(RESULT_OK, mReceivedIntent);
            finish();
        } else {
            String location = mLocation + File.separator + mFolderAndFileList.get(position).getName();
            checkAndLoadLists(location);
//            checkAndloadLists(mLocation);
        }
    }

    public void home(View v) {
        String location = Environment.getExternalStorageDirectory().getAbsolutePath();
        checkAndLoadLists(location);
    }

    /**
     * Create new folder.
     * @param filename
     */
    void createNewFolder(String filename) {
        try {
            File file = new File(mLocation + File.separator + filename);
            file.mkdirs();
            checkAndLoadLists(mLocation);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, String.format(getString(R.string.error_string_mask), e.toString()), Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Show dialog fom enter new folder name;
     * @param v
     */
    public void newFolderDialog(View v) {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = inflater.inflate(R.layout.dialog_folder_name, null);
        builder.setView(view);
        builder.setTitle(getString(R.string.enter_folder_name));

        final EditText et = view.findViewById(R.id.edit_text);

        final AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.create),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        createNewFolder(et.getText().toString());
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (DialogInterface.OnClickListener)null);

        dialog.show();
    }

    /**
     * Select the destination folder or file.
     * @param v
     */
    public void select(View v) {

        if (mPickFiles) {
            Toast.makeText(this, getString(R.string.select_file), Toast.LENGTH_LONG).show();
        } else if (mReceivedIntent != null) {
            if (mEmptyFolder && !isDirEmpty(mLocation)) {
                Toast.makeText(this, getString(R.string.select_empty_folder), Toast.LENGTH_LONG).show();
                return;
            }
            mReceivedIntent.putExtra(EXTRA_DATA, mLocation);
            setResult(RESULT_OK, mReceivedIntent);
            finish();
        }
    }

    /**
     * Edit path manually.
     * @param v
     */
    public void edit(View v) {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = inflater.inflate(R.layout.dialog_edit_location, null);
        builder.setView(view);
        builder.setTitle(getString(R.string.edit_location));

        final EditText et = view.findViewById(R.id.edit_text);
        if (mLocation != null) {
            et.setText(mLocation);
            et.setSelection(mLocation.length());
        }

        final AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String location = et.getText().toString();
//                        loadLists(mLocation);
                        checkAndLoadLists(location);
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (DialogInterface.OnClickListener)null);

        dialog.show();
    }

    /**
     * Check if folder is empty.
     * @param path
     * @return
     */
    boolean isDirEmpty(String path) {
        File dir = new File(path);
        File[] childs = dir.listFiles();
        return (childs == null || childs.length == 0);
    }

    @Override
    public void onBackPressed(){
        goBack(null);
    }

    /**
     * Load upper level path or exit.
     * @param v
     */
    public void goBack(View v) {
        if (mLocation != null && !mLocation.equals("") && !mLocation.equals("/")) {
            int start = mLocation.lastIndexOf('/');
            String newLocation = mLocation.substring(0, start);
//            mLocation = newLocation;
//            loadLists(newLocation);
            if (!checkAndLoadLists(newLocation, false)) {
                exit();
            }
        } else {
            exit();
        }
    }

    public void cancel(View v) {
        exit();
    }

    /**
     * Set result and finish activity.
     */
    void exit() {
        setResult(RESULT_CANCELED, mReceivedIntent);
        finish();
    }
}
