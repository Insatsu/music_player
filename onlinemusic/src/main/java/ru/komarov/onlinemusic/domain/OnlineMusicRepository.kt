package ru.komarov.onlinemusic.domain

import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.musicslist.domain.MusicRepository
import javax.inject.Inject


class OnlineMusicRepository @Inject constructor() : MusicRepository {
    val musicsList: ArrayList<MusicListItemModel> = ArrayList()


    override fun loadMusic(musicList: ArrayList<MusicListItemModel>): Boolean {
        musicsList.addAll(musicList)
        return true
    }

    override fun getMusic(filter: String? ): ArrayList<MusicListItemModel> {
        return musicsList
    }
}