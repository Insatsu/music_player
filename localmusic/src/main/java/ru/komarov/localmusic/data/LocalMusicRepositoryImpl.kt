package ru.komarov.localmusic.data

import ru.komarov.localmusic.domain.LocalMusicRepository
import ru.komarov.musicslist.domain.MusicListItemModel
import javax.inject.Inject

class LocalMusicRepositoryImpl @Inject constructor() : LocalMusicRepository {
    val musicsList: ArrayList<MusicListItemModel> = ArrayList()

    override fun loadMusic(musicList: ArrayList<MusicListItemModel>): Boolean {
        musicsList.addAll(musicList)
        return true
    }

    override fun getMusic(filter: String?): ArrayList<MusicListItemModel> {
        return when (filter) {
            null -> musicsList
            else -> ArrayList( musicsList.filter { it.title.contains(Regex("$filter", RegexOption.IGNORE_CASE)) })

        }
    }


}