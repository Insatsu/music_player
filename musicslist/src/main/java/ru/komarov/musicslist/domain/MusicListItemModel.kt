package ru.komarov.musicslist.domain

import android.widget.ImageView

data class MusicListItemModel(
    val title: String,
    val author: String,
    val icon: ((ImageView) -> Unit)? = null,
    val onClickListener: () -> Unit,
    val path: String
)