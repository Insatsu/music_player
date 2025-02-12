package ru.komarov.onlinemusic.domain

import android.util.Log
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.musicslist.domain.MusicRepository
import javax.inject.Inject


class OnlineMusicRepository @Inject constructor() : MusicRepository {
    val musics: ArrayList<MusicListItemModel> = ArrayList()


    override fun loadMusic(musicList: ArrayList<MusicListItemModel>): Boolean {
        musics.addAll(musicList)
        return true
    }

    override fun getMusic(): ArrayList<MusicListItemModel> {
        return musics
    }
}