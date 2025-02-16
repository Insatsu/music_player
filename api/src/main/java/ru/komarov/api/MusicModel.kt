package ru.komarov.api

import android.widget.ImageView

data class MusicModel(
    val title: String,
    val author: String,
    val album: String?,
    val icon: ((ImageView) -> Unit)? = null,
    val musicPath: String
)
