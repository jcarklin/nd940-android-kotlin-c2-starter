# Project Title

Asteroid Radar

## Getting Started

Asteroid Radar is an app to view the asteroids detected by NASA that pass near Earth, you can view all the detected asteroids in a period of time, their data (Size, velocity, distance to Earth) and if they are potentially hazardous.

The app is consists of two screens: A Main screen with a list of all the detected asteroids and a Details screen that is going to display the data of that asteroid once it´s selected in the Main screen list. The main screen will also show the NASA image of the day



### Dependencies

```
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'androidx.core:core-ktx:1.3.2'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.0-alpha2'

    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.3.0"
    implementation 'androidx.fragment:fragment-ktx:1.3.0'

    implementation "androidx.navigation:navigation-fragment-ktx:2.3.3"
    implementation "androidx.navigation:navigation-ui-ktx:2.3.3"

    implementation "com.squareup.moshi:moshi:1.11.0"
    implementation "com.squareup.moshi:moshi-kotlin:1.11.0"

    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-moshi:2.9.0'
    implementation 'com.squareup.retrofit2:converter-scalars:2.9.0'

    implementation "androidx.recyclerview:recyclerview:1.1.0"

    implementation 'com.squareup.picasso:picasso:2.71828'

    implementation 'androidx.room:room-runtime:2.2.6'
    kapt 'androidx.room:room-compiler:2.2.6'
    implementation "androidx.room:room-ktx:2.2.6"

    implementation "androidx.work:work-runtime-ktx:2.5.0"
    implementation 'com.squareup.picasso:picasso:2.71828'

    implementation "com.jakewharton.timber:timber:4.7.1"

    testImplementation 'junit:junit:4.13.1'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'

```

### Installation

Open the project in Android Studio and click the Run ‘app’ button, check that it runs correctly and you can see the app in your device or emulator.

### Prerequisites

An API key from NASA is required (https://api.nasa.gov/)

Add your API key to the gradle.properties file in your home .gradle directory:

Add the following line
```
NASA_KEY="1234567890"
```
to ~/.gradle/gradle.properties in Linux
or C:\Users\\[Username]\\.gradle in Windows (for example)
where _"1234567890"_ is replaced with your actual API Key

You will not be able to build or use the app until this is done. 


## Built With

To build this project you are going to use the NASA NeoWS (Near Earth Object Web Service) API, which you can find here.
https://api.nasa.gov/

You will need an API Key which is provided for you in that same link, just fill the fields in the form and click Signup.
