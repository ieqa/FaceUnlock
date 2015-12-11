Android-Bootstrap
=================
Android Bootstrap is a library which provides several custom views styled according to the
 [Twitter Bootstrap Specification](http://getbootstrap.com/). This allows you to spend more time
  on development rather than trying to get a consistent theme across your app, especially if you are already familiar with the Bootstrap Framework.


Getting Started
=============

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.beardedhen/androidbootstrap/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.beardedhen/androidbootstrap)
### Gradle
Add the following to your build.gradle:

```java
dependencies {
   compile 'com.beardedhen:androidbootstrap:+'
}
```

### Library Project
Alternatively you can download the source and link to it as a library project in Eclipse, or import as a module in Android Studio.

### Sample Code
Please checkout the [sample project](https://github.com/Bearded-Hen/AndroidBootstrapSample), as it contains example code for most usecases.

### Usage

1. Paste the following XML into a layout file to create a BootstrapButton:
   
   ```xml
<!-- basic button -->
<com.beardedhen.androidbootstrap.BootstrapButton
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="10dp"
    android:text="Success"
    bootstrap:bb_icon_right="fa-android"
    bootstrap:bb_type="success"
/>
```

2. Add the bootstrap namespace to the root view of your layout:
   
   ```xml
xmlns:bootstrap="http://schemas.android.com/apk/res-auto"
```

3. Build and run the app.

## More Information

Feel free to inspect the library source or look at the [wiki] for further information(https://github.com/Bearded-Hen/Android-Bootstrap/wiki):
* [bootstrap buttons](https://github.com/Bearded-Hen/Android-Bootstrap/wiki/Bootstrap-Button)
* [font awesome texts](https://github.com/Bearded-Hen/Android-Bootstrap/wiki/Font-Awesome-Text)
* [bootstrap edit texts](https://github.com/Bearded-Hen/Android-Bootstrap/wiki/Bootstrap-Edit-Text)
* [boostrap thumbnails](https://github.com/Bearded-Hen/Android-Bootstrap/wiki/Bootstrap-Thumbnail)


If you have any questions, issues, or just want to let us know where you're using Android Bootstrap tweet us at [@BeardedHen](https://twitter.com/beardedhen), email support@beardedhen.com, or head over to our [website](http://beardedhen.com/) to see more of our creations.

#Apps Using Android-Bootstrap

- [Forms on Mobile - Lite](https://play.google.com/store/apps/details?id=com.formsonmobile.lite.contactsdetails)
- [BHFileBrowser](https://github.com/Bearded-Hen/BHFileBrowser)


#Features
* Uses min SDK 7 which is Android 2.1 (Tested on a device running Android 2.2)
* Bootstrap buttons as per Bootstrap v3
* Rounded buttons
* Disabled buttons
* Various sized buttons (large to extra small)
* Just text buttons
* Left, right, left and right, or just icon button
* Font Awesome text as per Font Awesome v4.3
* Animations to Font Awesome Text items
* EditText backgrounds and states

Examples
============

###BootstrapButton
A button that supports Glyph icons, and is themeable using Bootstrap Brands.
   ```xml
<com.beardedhen.androidbootstrap.BootstrapButton
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="BootstrapButton"
    app:bootstrapBrand="success"
    app:bootstrapSize="lg"
    app:buttonMode="regular"
    app:showOutline="false"
    app:roundedCorners="true"
    />
```
<img src="https://raw.github.com/Bearded-Hen/Android-Bootstrap/master/images/bootstrap_button.png" width="450" alt="BootstrapButton">

###BootstrapButtonGroup
Allows BootstrapButtons to be grouped together and their attributes controlled en masse.
   ```xml
<com.beardedhen.androidbootstrap.BootstrapButtonGroup
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="BootstrapButtonGroup"
    android:orientation="vertical"
    app:bootstrapBrand="success"
    app:bootstrapSize="lg"
    app:roundedCorners="true"
    >
    <com.beardedhen.androidbootstrap.BootstrapButton
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:text="BootstrapButton 1"
       />
    <com.beardedhen.androidbootstrap.BootstrapButton
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:text="BootstrapButton 2"
       />
</com.beardedhen.androidbootstrap.BootstrapButtonGroup>
```
<img src="https://raw.github.com/Bearded-Hen/Android-Bootstrap/master/images/bootstrap_button_group.png" width="450" alt="BootstrapButtonGroup">


###AwesomeTextView
A text widget that displays Glyph icons, and is themeable using Bootstrap Brands.
   ```xml
<com.beardedhen.androidbootstrap.AwesomeTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:bootstrapBrand="success"
    app:fontAwesomeIcon="fa_android"
    />
```
<img src="https://raw.github.com/Bearded-Hen/Android-Bootstrap/master/images/awesome_text_view.png" width="450" alt="AwesomeTextView">

###BootstrapProgressBar
Displays progress in a bar from 0-100, and animates updates to the current progress.
   ```xml
<com.beardedhen.androidbootstrap.BootstrapProgressBar
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:animated="true"
    app:bootstrapBrand="warning"
    app:progress="78"
    app:striped="true"
    />
```
<img src="https://raw.github.com/Bearded-Hen/Android-Bootstrap/master/images/bootstrap_progress_bar.png" width="450" alt="BootstrapProgressBar">

###BootstrapLabel
Displays non-clickable text in a widget similar to the BootstrapButton, sizable using H1-H6 elements.
   ```xml
<com.beardedhen.androidbootstrap.BootstrapLabel
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:bootstrapBrand="primary"
    app:bootstrapHeading="h3"
    app:roundedCorners="true"
    android:text="Bootstrap Label"
    />
```
<img src="https://raw.github.com/Bearded-Hen/Android-Bootstrap/master/images/bootstrap_label.png" width="450" alt="BootstrapLabel">

###BootstrapEditText
Allows editing of text in a widget themed using BootstrapBrand.
   ```xml
<com.beardedhen.androidbootstrap.BootstrapEditText
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:bootstrapSize="md"
    app:bootstrapBrand="info"
    />
```
<img src="https://raw.github.com/Bearded-Hen/Android-Bootstrap/master/images/bootstrap_edit_text.png" width="450" alt="BootstrapEditText">

###BootstrapCircleThumbnail
Displays images in a center-cropped Circular View, themed with BootstrapBrand.
   ```xml
<com.beardedhen.androidbootstrap.BootstrapCircleThumbnail
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/my_drawable"
    app:bootstrapBrand="danger"
    app:hasBorder="true"
    />
```
<img src="https://raw.github.com/Bearded-Hen/Android-Bootstrap/master/images/bootstrap_circle_thumbnail.png" width="450" alt="BootstrapCircleThumbnail">

###BootstrapThumbnail
Displays images in a rectangular View, themed with BootstrapBrand.
   ```xml
<com.beardedhen.androidbootstrap.BootstrapThumbnail
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/my_drawable"
    app:bootstrapBrand="info"
    app:hasBorder="true"
    />
```
<img src="https://raw.github.com/Bearded-Hen/Android-Bootstrap/master/images/bootstrap_thumbnail.png" width="450" alt="BootstrapThumbnail">
 Custom Styles
============
Custom styles can be applied to any of the views in this library by creating a class which implements
BootstrapBrand, and setting it on the View. Please see the sample code of BootstrapButton for more detail.

 ```java

     class CustomBootstrapStyle implements BootstrapBrand {
         // specify desired colors here
     }

     BootstrapButton btn = new BootstrapButton(context);
     btn.setBootstrapBrand(new CustomBootstrapStyle(this);
 ```
