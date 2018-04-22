Android Simple Image picker
===
Simple image picker for Android, written in Kotlin.

Target API Level
---
- minSdkVersion : 19
- targetSdkVersion : 27

Screenshots
---
<img src="/docs/arts/001.png" width="30%"> <img src="/docs/arts/002.png" width="30%"> <img src="/docs/arts/003.png" width="30%">

Usage
---

See [Sample](https://github.com/KarageAgeta/android-image-picker/tree/master/Sample) for example usage.

### Code

#### For Simplest Use

```kotlin
Activity?.let {
  ImagePicker
      .Builder(it)
      // Your App's package name (Used for "Open Setting" button when permission denied)
      .packageName(context?.packageName ?: "")
      .start()
}
```

#### For More Options

```kotlin
Activity?.let {
  ImagePicker
      .Builder(it)
      // Title for "All Images" (Default : "All")
      .pickerAllItemTitle("すべて")
      // Your App's name
      .packageName(context?.packageName ?: "")
      // Max number of the images (Default : 5)
      .maxCount(3)
      // Set "No Image" drawable
      .noImage(ResourcesCompat.getDrawable(resources, R.drawable.no_image, null))
      // Set "No permission" drawable
      .noImage(ResourcesCompat.getDrawable(resources, R.drawable.no_permission, null))
      // Set description text for "No permission"
      .noPermissionText("Please allow permissions from \"Setting\"")
      .start()
}
```

Upcoming Changes
---
- Add "Start Camera (take new pictures)" feature
- Enable to pick movies
- Simple filters for images

License
---

```
Copyright 2018 Yoko Karasaki

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

Pictures for [Screenshots](#screenshots) are from [フリー素材ぱくたそ](https://www.pakutaso.com/) .

See [ぱくたそのご利用規約・ガイド | ぱくたそフリー素材](https://www.pakutaso.com/userpolicy.html) for each license information.
