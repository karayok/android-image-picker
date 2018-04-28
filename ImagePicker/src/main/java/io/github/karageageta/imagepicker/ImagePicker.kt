package io.github.karageageta.imagepicker

import android.app.Activity
import io.github.karageageta.imagepicker.ui.picker.ImagePickerActivity
import android.content.Intent
import android.graphics.drawable.Drawable
import io.github.karageageta.imagepicker.helper.ExtraName
import io.github.karageageta.imagepicker.helper.RequestCode
import io.github.karageageta.imagepicker.model.data.Config
import java.io.Serializable

class ImagePicker {
    class Builder(private var activity: Activity) {
        private val config = Config()

        fun pickerAllItemTitle(title: String): Builder {
            config.pickerAllItemTitle = title
            return this
        }

        fun maxCount(count: Int): Builder {
            config.maxCount = count
            return this
        }

        fun noImage(image: Drawable?): Builder {
            config.noImage = image
            return this
        }

        fun noPermissionImage(image: Drawable?): Builder {
            config.noPermission = image
            return this
        }

        fun noPermissionText(text: String): Builder {
            config.noPermissionText = text
            return this
        }

        fun packageName(name: String): Builder {
            config.packageName = name
            return this
        }

        fun start() {
            Intent(activity, ImagePickerActivity::class.java)
                    .apply { putExtra(ExtraName.CONFIG.name, config as Serializable) }
                    .let { activity.startActivityForResult(it, RequestCode.PICK_IMAGE.rawValue) }
        }
    }
}
