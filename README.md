# android-folder-picker-library
[![Demo](https://img.shields.io/badge/Demo%20APK-2.2-blue.svg)](https://github.com/kashifo/android-folder-picker-library/releases/download/v2.2/FolderPickerDemo_v2.2.apk)
[![Bintray](https://img.shields.io/badge/Bintray-2.2-blue.svg)](https://bintray.com/kashifo/android-folder-picker-library/android-folder-picker-library/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-android--folder--picker--library-blue.svg?style=flat)](https://android-arsenal.com/details/1/5837)

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

    Copyright 2017 Kashif Anwaar.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

