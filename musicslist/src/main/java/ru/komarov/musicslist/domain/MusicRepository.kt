package ru.komarov.musicslist.domain

// Implemented in parent view
interface MusicRepository {

    fun loadMusic(musicList: ArrayList<MusicListItemModel>): Boolean

    fun getMusic(filter: String? = null): ArrayList<MusicListItemModel>

}