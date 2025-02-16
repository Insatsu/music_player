package ru.komarov.onlinemusic.data

import ru.komarov.api.MusicModel
import ru.komarov.api.RemoteMusicsService
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.onlinemusic.domain.OnlineMusicRepository
import javax.inject.Inject

class OnlineMusicRepositoryImpl :
    OnlineMusicRepository {
    val musicsList: ArrayList<MusicListItemModel> = ArrayList()
    private val filteredMusicsList: ArrayList<MusicListItemModel> = ArrayList()

    override fun getMusicsModelList(): ArrayList<MusicModel> {
        val neededList = if (filteredMusicsList.isEmpty()) musicsList else filteredMusicsList
        val musicModelList: ArrayList<MusicModel> = ArrayList(neededList.map { musicListItemModel ->
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
        filteredMusicsList.clear()
        if (filter != null)
            musicsList.forEach {
                if (it.title.contains(
                        Regex(
                            "$filter",
                            RegexOption.IGNORE_CASE
                        )
                    )
                ) {
                    filteredMusicsList.add(it)
                }
            }

        return when (filter) {
            null -> musicsList
            else -> filteredMusicsList

        }
    }
}