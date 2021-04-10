package com.zippyflash82.downloaddemo.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.danimahardhika.cafebar.CafeBar

fun Activity.showToast(msg: String) {
    this.runOnUiThread { Toast.makeText(this, msg, Toast.LENGTH_LONG).show() }
}

fun showSnackbar(context: Context,msg:String){
    CafeBar.builder(context)
        .content(msg)
        .floating(true)
        .show()
}