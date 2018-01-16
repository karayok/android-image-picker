package com.karageageta.simpleimagepicker.model.data

import android.graphics.drawable.Drawable
import java.io.Serializable

data class Config(
        var pickerAllItemTitle: String = "All",
        var minCount: Int = 1,
        var maxCount: Int = 5,
        var noImage: Drawable? = null,
        var noPermission: Drawable? = null,
        var noPermissionText: String? = null,
        var disableNoPermissionText: Boolean = false
) : Serializable
