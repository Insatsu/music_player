package ru.komarov.onlinemusic.data

import ru.komarov.api.MusicModel
import ru.komarov.api.RemoteMusicsService
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.onlinemusic.domain.OnlineMusicRepository
import javax.inject.Inject

class OnlineMusicRepositoryImpl @Inject constructor(remoteMusicsService: RemoteMusicsService) :
    OnlineMusicRepository {
    val musicsList: ArrayList<MusicListItemModel> = ArrayList()


    override fun getMusicsModelList(): ArrayList<MusicModel> {
        val musicModelList: ArrayList<MusicModel> = ArrayList(musicsList.map { musicListItemModel ->
            MusicModel(
                title = musicListItemModel.title,
                author = musicListItemModel.author,
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