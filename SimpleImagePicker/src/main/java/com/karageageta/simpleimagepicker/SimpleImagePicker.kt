package com.karageageta.simpleimagepicker

import android.app.Activity
import android.os.Bundle
import com.karageageta.simpleimagepicker.ui.main.SimpleImagePickerActivity
import android.content.Intent
import com.karageageta.simpleimagepicker.helper.ExtraName
import com.karageageta.simpleimagepicker.helper.Key
import com.karageageta.simpleimagepicker.helper.RequestCode

class SimpleImagePicker {
    // TODO : action bar color , HomeAsUpIndicator
    class Builder(private var activity: Activity) {
        private val config = Bundle()

        fun pickerAllItemTitle(title: String): Builder {
            config.putString(Key.PICKER_ALL_ITEM_NAME.name, title)
            return this
        }

        fun minCount(count: Int): Builder {
            config.putInt(Key.MIN_COUNT.name, count)
            return this
        }

        fun maxCount(count: Int): Builder {
            config.putInt(Key.MAX_COUNT.name, count)
            return this
        }

        fun start() {
            val intent = Intent(activity, SimpleImagePickerActivity::class.java)
            intent.putExtra(ExtraName.CONFIG.name, config)
            activity.startActivityForResult(intent, RequestCode.PICK_IMAGE.rawValue)
        }
    }
}
