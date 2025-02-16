package ru.komarov.localmusic.data

import android.os.Environment
import ru.komarov.api.MusicModel
import ru.komarov.localmusic.domain.LocalMusicRepository
import ru.komarov.musicslist.domain.MusicListItemModel
import javax.inject.Inject

class LocalMusicRepositoryImpl @Inject constructor() : LocalMusicRepository {
    private val musicsList: ArrayList<MusicListItemModel> = ArrayList()

    private val filteredMusicsIdsList: ArrayList<MusicListItemModel> = ArrayList()
    override fun getLocalFolderPath() = Environment.getExternalStorageDirectory().path + "/Download"

    override fun getMusicsModelList(): ArrayList<MusicModel> {
        val neededList = if (filteredMusicsIdsList.isEmpty()) musicsList else filteredMusicsIdsList

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
        if (filter == null)
            filteredMusicsIdsList.clear()
        else
            musicsList.forEach {
                if (it.title.contains(
                        Regex(
                            "$filter",
                            RegexOption.IGNORE_CASE
                        )
                    )
                ) {
                    filteredMusicsIdsList.add(it)
                }
            }

        return when (filter) {
            null -> musicsList
            else -> filteredMusicsIdsList
        }
    }


}