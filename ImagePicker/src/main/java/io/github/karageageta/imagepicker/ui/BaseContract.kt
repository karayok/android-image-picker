package io.github.karageageta.imagepicker.ui

open class BaseContract {
    interface View

    interface Presenter<out V : View> {
        val view: V?
    }
}
