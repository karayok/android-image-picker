package com.karageageta.sample

open class BaseContract {
    interface View

    interface Presenter<out V : BaseContract.View> {
        val view: V?
    }
}
