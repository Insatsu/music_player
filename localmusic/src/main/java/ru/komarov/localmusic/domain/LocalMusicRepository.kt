package ru.komarov.localmusic.domain

import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.musicslist.domain.MusicRepository
import javax.inject.Inject

class LocalMusicRepository @Inject constructor(): MusicRepository {
    val musics: ArrayList<MusicListItemModel> = ArrayList()

    override fun loadMusic(musicList: ArrayList<MusicListItemModel>): Boolean {
        musics.addAll(musicList)
        return true
    }

    override fun getMusic(): ArrayList<MusicListItemModel> {
        return musics
    }
}