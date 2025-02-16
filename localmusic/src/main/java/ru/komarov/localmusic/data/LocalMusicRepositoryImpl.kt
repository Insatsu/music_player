package ru.komarov.localmusic.data

import android.os.Environment
import ru.komarov.api.MusicModel
import ru.komarov.localmusic.domain.LocalMusicRepository
import ru.komarov.musicslist.domain.MusicListItemModel
import javax.inject.Inject

class LocalMusicRepositoryImpl @Inject constructor() : LocalMusicRepository {
    val musicsList: ArrayList<MusicListItemModel> = ArrayList()
    override fun getLocalFolderPath() = Environment.getExternalStorageDirectory().path + "/Download"

    override fun getMusicsModelList(): ArrayList<MusicModel> {
        val musicModelList: ArrayList<MusicModel> = ArrayList(musicsList.map { musicListItemModel ->
            MusicModel(
                title = musicListItemModel.title,
                author = musicListItemModel.author,
                album = musicListItemModel.album,
                icon = musicListItemModel.icon,
                musicPath = musicListItemModel.path
            )
        }.toList())

        return musicModelList
    }


    override fun loadMusic(musicList: ArrayList<MusicListItemModel>): Boolean {
        musicsList.clear()
        musicsList.addAll(musicList)
        return true
    }

    override fun getMusic(filter: String?): ArrayList<MusicListItemModel> {
        return when (filter) {
            null -> musicsList
            else -> ArrayList(musicsList.filter {
                it.title.contains(
                    Regex(
                        "$filter",
                        RegexOption.IGNORE_CASE
                    )
                )
            })

        }
    }


}