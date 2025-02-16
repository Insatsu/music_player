package ru.komarov.onlinemusic.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.komarov.api.MusicModel
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.onlinemusic.domain.OnlineMusicRepository
import javax.inject.Inject

class OnlineMusicRepositoryImpl @Inject constructor() : OnlineMusicRepository {
    private val _musicGetListener: MutableStateFlow<String?> = MutableStateFlow(null)
    override val musicGetListener: StateFlow<String?> = _musicGetListener.asStateFlow()

    private val isLoaded = MutableStateFlow(false)

    private val musicsList: ArrayList<MusicListItemModel> = ArrayList()

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
        isLoaded.update { true }
        return true
    }

    // Get music. Wait until musics loaded and then return it
    override fun getMusic(filter: String?): ArrayList<MusicListItemModel> {
        _musicGetListener.update {
            filter
        }

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                isLoaded.asStateFlow().filter { it }.collectLatest {
                    this.coroutineContext.cancel()
                }
            }
        }

        isLoaded.update { false }

        return musicsList
    }

}