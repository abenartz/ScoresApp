package com.example.scoresapp.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.scoresapp.R

fun ImageView.load(imageUrl: String?) {
    Glide
        .with(this)
        .load(imageUrl)
        .centerCrop()
        .placeholder(R.drawable.ic_launcher_foreground)
        .into(this)
}