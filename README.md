<snippet>
[ ![Download](https://api.bintray.com/packages/michaelprimez/maven/RevealEditText/images/download.svg) ](https://bintray.com/michaelprimez/maven/RevealEditText/_latestVersion) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ReavelEditText-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5059)
#RevealEditText
![Demo RevelEditText](https://github.com/michaelprimez/reveledittext/blob/master/RevealEditText.gif) 
## Usage

Add the dependency to your build.gradle.
```xml
compile 'gr.escsoft.michaelprimez.revealedittext:RevealEditText:1.0.1'
```

Usage on layout
```xml
<gr.escsoft.michaelprimez.revealedittext.RevealEditText
        android:id="@+id/RevealEditText3"
        android:layout_width="match_parent"
        android:layout_height="56dp"
        android:layout_marginTop="24dp"
        android:layout_marginLeft="24dp"
        android:layout_marginRight="24dp"
        android:focusable="true"
        android:focusableInTouchMode="true"
        app:RevealViewBackgroundColor="@android:color/holo_orange_light"
        app:RevealViewTextColor="@android:color/white"
        app:StartEditTintColor="@android:color/white"
        app:EditViewBackgroundColor="@android:color/holo_red_light"
        app:EditViewTextColor="@android:color/white"
        app:DoneEditTintColor="@android:color/white"
        app:AnimDuration="300"
        app:Text="Reveal EditText 3"/>
```

or setup with Biulder

```java
mRevealEditTextWithBuilder = 
        new RevealEditText.Builder(this)
        .setTextIfEmpty("Touch to insert ...")
        .setAnimDuration(350)
        .setEditViewBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_purple))
        .setEditViewTextColor(ContextCompat.getColor(this, android.R.color.white))
        .setStartEditTintColor(ContextCompat.getColor(this, android.R.color.white))
        .setRevealViewBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark))
        .setRevealViewTextColor(ContextCompat.getColor(this, android.R.color.white))
        .setDoneEditTintColor(ContextCompat.getColor(this, android.R.color.white))
        .setShowIcons(true).build();
       
       FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UITools.dpToPx(this, 56));
        layoutParams.leftMargin = UITools.dpToPx(this, 24);
        layoutParams.rightMargin = UITools.dpToPx(this, 24);
        mRevealEditTextWithBuilder.setLayoutParams(layoutParams);
        linearLayout.addView(mRevealEditTextWithBuilder);
```
## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D
## History

# LICENSE 

```
Copyright (C) 2016 Michael Keskinidis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
</snippet>
