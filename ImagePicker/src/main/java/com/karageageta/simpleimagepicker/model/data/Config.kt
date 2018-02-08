package com.karageageta.simpleimagepicker.model.data

import android.graphics.drawable.Drawable
import java.io.Serializable

data class Config(
        var pickerAllItemTitle: String = "All",
        var maxCount: Int = 5,
        var noImage: Drawable? = null,
        var noPermission: Drawable? = null,
        var noPermissionText: String? = null,
        var packageName: String? = null
) : Serializable
