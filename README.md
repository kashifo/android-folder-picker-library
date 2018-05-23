# android-folder-picker-library
[![Demo](https://img.shields.io/badge/Demo%20APK-2.4-blue.svg)](https://github.com/kashifo/android-folder-picker-library/releases/download/v2.4/FolderPickerDemo_v2.4.apk)
[![Bintray](https://img.shields.io/badge/Bintray-2.4-blue.svg)](https://bintray.com/kashifo/android-folder-picker-library/android-folder-picker-library/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-android--folder--picker--library-blue.svg?style=flat)](https://android-arsenal.com/details/1/5837)

A light-weight android library to let user's pick folder / files from storage.

# Some Example Usages
- Ask users to select folder for saving your app's files.
- Ask users to point a folder to import / export data.
- Ask users to choose file from phone storage for viewing, editing or any process.

# Screenshots

|![Preview](https://github.com/kashifo/android-folder-picker-library/raw/master/screenshots/folderpicker-scr1.png) | ![Preview](https://github.com/kashifo/android-folder-picker-library/raw/master/screenshots/folderpicker-scr2.png) | ![Preview](https://github.com/kashifo/android-folder-picker-library/raw/master/screenshots/folderpicker-scr3.png) |
|:-------------------:|:------------------------:|:-----------------:|
| Can pick folders | Can create folder | Can also pick file |


# Installation

For your convenience, it is available on jCenter, So just add this in your app dependencies:
```gradle
    compile 'lib.kashif:folderpicker:2.4'
```
  
# Usage

**To pick folder**

Just start FolderPicker activity from your app
```java
        Intent intent = new Intent(this, FolderPicker.class);
        startActivityForResult(intent, FOLDERPICKER_CODE);        
```

If the user selects folder/file, the name of folder/file will be returned to you on onActivityResult method with the variable name 'data'.

```java
        
        protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
          if (requestCode == FOLDERPICKER_CODE && resultCode == Activity.RESULT_OK) {

              String folderLocation = intent.getExtras().getString("data");
              Log.i( "folderLocation", folderLocation );
            
          }
        }
        
 ```

**Options**

Cusstomization options can be passed as extras to FolderPicker activity.

 ```java
 
        //To show a custom title
        intent.putExtra("title", "Select file to upload");
        
        //To begin from a selected folder instead of sd card's root folder. Example : Pictures directory
        intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        
        //To pick files
        intent.putExtra("pickFiles", true);
        
  ```
  
[Click here to see an example](https://github.com/kashifo/android-folder-picker-library/blob/master/app/src/main/java/lib/folderpicker/demo/MainActivity.java)


## License

    Copyleft 2017 Kashif Anwaar.
    
    Licensed under Apache 2.0
    
    Do whatever you want with this library.


