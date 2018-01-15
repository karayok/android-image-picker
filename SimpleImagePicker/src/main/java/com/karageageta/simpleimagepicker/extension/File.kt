package com.karageageta.simpleimagepicker.extension

import java.io.File

fun File.createValidFile(): File? {
    if (path.isEmpty()) {
        return null
    }
    return try {
        File(path)
    } catch (e: Exception) {
        null
    }
}