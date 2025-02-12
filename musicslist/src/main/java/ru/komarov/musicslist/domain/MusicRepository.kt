package ru.komarov.musicslist.domain

interface MusicRepository {

    fun loadMusic(musicList: ArrayList<MusicListItemModel>): Boolean

    fun getMusic(): ArrayList<MusicListItemModel>

}