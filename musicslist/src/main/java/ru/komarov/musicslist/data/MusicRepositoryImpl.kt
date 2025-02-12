package ru.komarov.musicslist.data

import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.musicslist.domain.MusicRepository

class MusicRepositoryImpl: MusicRepository {
    private val musicList = ArrayList<MusicListItemModel>()

    override fun loadMusic(musicList: ArrayList<MusicListItemModel>): Boolean {
        this.musicList.clear()
        this.musicList.addAll(musicList)
        return true
    }

    override fun getMusic(): ArrayList<MusicListItemModel> {
        return musicList
    }
}