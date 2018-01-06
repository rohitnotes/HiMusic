package com.example.wenhai.listenall.ext

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes strId: Int) {
    showToast(getString(strId))
}
