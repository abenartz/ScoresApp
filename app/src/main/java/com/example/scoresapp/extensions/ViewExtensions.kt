package com.example.scoresapp.extensions

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.media3.common.MediaItem
import com.bumptech.glide.Glide
import com.example.scoresapp.R
import com.example.scoresapp.model.Page
import com.google.android.material.snackbar.Snackbar


fun View.showView(isShow: Boolean = true) {
    if (isShow) this.visibility = View.VISIBLE else this.visibility = View.GONE
}

fun Activity.displayToast(message:String) {
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}

fun View.displaySnackBar(message:String) {
    Snackbar.make(this,message, Snackbar.LENGTH_SHORT).show()
}

fun ImageView.load(imageUrl: String?) {
    Glide
        .with(this)
        .load(imageUrl)
        .centerCrop()
        .placeholder(R.drawable.ic_launcher_background)
        .into(this)
}
