package com.karageageta.simpleimagepicker.model.data

data class SelectableImage(
        val image: Image = Image(),
        var isSelected: Boolean = false
)