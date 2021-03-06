Stater
=======
[![Build status](https://travis-ci.org/AlexeyPanchenko/stater.svg?branch=master)](https://travis-ci.org/AlexeyPanchenko/stater)
[![Download-plugin](https://api.bintray.com/packages/alexeypanchenko/maven/stater-plugin/images/download.svg) ](https://bintray.com/alexeypanchenko/maven/stater-plugin/_latestVersion)

Lightweight library to save state in your Activity/Fragment.
Stater also fine works with Kotlin code as it uses bytecode transformation.

Download
--------
In root `build.gradle` file:
```groovy
buildscript {
    ext.stater_version = '1.2'
    repositories {
        jcenter()
    }
    dependencies {
        ...
        classpath "ru.alexpanchenko:stater-plugin:$stater_version"
    }
}

allprojects {
    repositories {
        jcenter()
    }
}

```
In your app `build.gradle` file add plugin:
```groovy
apply plugin: 'com.android.application'
...
apply plugin: 'stater-plugin'
```

Usage
--------
Activity
```java
import ru.alexpanchenko.stater.State;

public class MainActivity extends AppCompatActivity {
  @State
  private int yourVar = 0;
  
  @State
  private Bundle bundleVar;
}
```
Fragment
```java
import ru.alexpanchenko.stater.State;

public class MainFragment extends Fragment {
  @State
  private int yourVar = 0;
  
  @State
  private Bundle bundleVar;
}
```
It's all!

If you need to save custom fields just add a setting in your `build.gradle` file:
```java
apply plugin: 'stater-plugin'

android {
  ...
}

stater {
  customSerializerEnabled = true
}
```
And then use:
```java
import ru.alexpanchenko.stater.State;

public class MainActivity extends AppCompatActivity {
  @State
  private CustomClass customClass;
  // or
  @State
  private List<CustomClass> customClassList;
  // or
  @State
  private Container<CustomClass> customClassContainer;
}
```

Supported types
--------
| Supported Types  |
|:-------|
| boolean/Boolean/boolean[ ]/Boolean[ ]|
| byte/Byte/byte[ ]/Byte[ ]|
| char/Character/char[ ]/Character[ ]|
| short/Short/short[ ]/Short[ ]|
| int/Integer/int[ ]/Integer[ ]  List/ArrayList\<Integer\> |
| float/Float/float[ ]/Float[ ] |
| long/Long/long[ ]/Long[ ] |
| double/Double/double[ ]/Double[ ] |
| String/String[ ]  List/ArrayList\<String\> |
| CharSequence/CharSequence[ ]  List/ArrayList\<CharSequence\> |
| Serializable |
| Parcelable/Parcelable[ ] List/ArrayList\<Parcelable\> |
| Bundle |
| IBinder |
| Custom types (optional) |

How it works
--------
Stater plugin transform classes that inherit Activity/Fragment in compile time (see `transformClassesWithStaterTransformFor...` task).
It save state in `onSaveInstanceState` and restore it in `onCreate`.
```java
protected void onCreate(@Nullable Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      this.yourVar = savedInstanceState.getInt("your/class/path_yourVar");
      this.bundleVar = savedInstanceState.bundleVar("your/class/path_bundleVar");
    }
    super.onCreate(savedInstanceState);
}

protected void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putInt("your/class/path_yourVar", this.yourVar);
    outState.putBundle("your/class/path_bundleVar", this.bundleVar);
    super.onSaveInstanceState(outState);
}
```
Transformed classes you can find in `build/intermediates/transforms/StaterTransform/yourPackage`.

License
-------

    Copyright 2019 Alexey Panchenko

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.