# android-folder-picker-library
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[ ![Download](https://api.bintray.com/packages/kashifo/android-folder-picker-library/android-folder-picker-library/images/download.svg) ](https://bintray.com/kashifo/android-folder-picker-library/android-folder-picker-library/_latestVersion)

A light-weight android library that can be quickly integrated into any app to let users choose folder, also files (but esp built for folders).

# Example Uses
- To let users choose folder for saving files
- To let users choose backup folder
- To let users pick files to upload
etc...

# Screenshots

|![Preview](https://github.com/kashifo/android-folder-picker-library/raw/master/screenshots/folderpicker-scr1.png) | ![Preview](https://github.com/kashifo/android-folder-picker-library/raw/master/screenshots/folderpicker-scr2.png) | ![Preview](https://github.com/kashifo/android-folder-picker-library/raw/master/screenshots/folderpicker-scr3.png) |
|:-------------------:|:------------------------:|:-----------------:|
| Can pick folders | Can create folder | Can also pick file |


# Installation

For your convenience, it is available on jCenter, So just add this in your app dependencies:
```gradle
    compile 'lib.kashif:folderpicker:2.2'
```
  
# Usage

**To pick folder**

```java

        Intent intent = new Intent(this, FolderPicker.class);
        startActivityForResult(intent, FILEPICKER_CODE);
        
        protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
          if (requestCode == FILEPICKER_CODE && resultCode == Activity.RESULT_OK) {

              String folderLocation = intent.getExtras().getString("data");
              Log.i( "folderLocation", folderLocation );
            
          }
        }
        
 ```

**Options**


 ```java
 
        //To show a custom title
        intent.putExtra("title", "Select file to upload");
        
        //To begin from a selected folder instead of sd card's root folder. Example : Pictures directory
        intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        
        //To pick files
        intent.putExtra("pickFiles", true);
        
  ```
