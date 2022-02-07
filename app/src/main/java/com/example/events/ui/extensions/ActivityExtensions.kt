package com.example.events.ui.extensions

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import com.example.events.ui.DetailsActivity
import com.example.events.constants.Constants.ID_EVENT

fun Activity.startDetail(id: Int) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        startActivity(Intent(this, DetailsActivity::class.java).apply {
            putExtra(ID_EVENT, id)
        }, bundle)
        return
    }
    startActivity(
        Intent(this, DetailsActivity::class.java).apply {
            putExtra(ID_EVENT, id)
        }
    )

}