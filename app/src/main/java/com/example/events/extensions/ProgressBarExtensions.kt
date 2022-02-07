package com.example.events.extensions

import android.app.ProgressDialog
import com.example.events.R

fun ProgressDialog.showDialog() {
    this.show()
    this.setContentView(R.layout.loading)
    this.window?.setBackgroundDrawableResource(android.R.color.transparent)
}

fun ProgressDialog.hideDialog() {
    this.dismiss()
}