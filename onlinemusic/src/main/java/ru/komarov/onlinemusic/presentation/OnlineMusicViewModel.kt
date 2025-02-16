package ru.komarov.onlinemusic.presentation

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.load
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.komarov.api.CurrentMusicsList
import ru.komarov.api.RemoteMusicsService
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.onlinemusic.domain.OnlineMusicRepository
import javax.inject.Inject

class OnlineMusicViewModel @Inject constructor(
    private val onlineMusicRepository: OnlineMusicRepository,
    private val remoteMusicsService: RemoteMusicsService,
) : ViewModel() {
    private var navigateToPlayer: (() -> Unit)? = null

    private val searchListener = onlineMusicRepository.musicGetListener
    private val coroutine = viewModelScope

    val isLoaded = MutableStateFlow(false)

    fun loadMusicFromApi() {
        // Init load music from chart
        loadMusicChart()
        // Listen to search and load result. If empty load chart
        loadMusicSearch()
    }

    private fun loadMusicSearch() {
        coroutine.launch {
            searchListener.collect { search ->
                val trimSearch = search?.trim()
                if (trimSearch.isNullOrEmpty()) {
                    loadMusicChart()
                    return@collect
                }

                val musics: ArrayList<MusicListItemModel> = ArrayList()
                val remoteMusics = remoteMusicsService.getMusicBySearch(query = trimSearch)

                remoteMusics.data.forEach { remoteMusic ->
                    musics.add(
                        getMusicListItemModel(
                            title = remoteMusic.title,
                            author = remoteMusic.artist.name,
                            album = remoteMusic.album.title,
                            icon = { imageView ->
                                imageView.load(remoteMusic.album.cover)
                            },
                            link = remoteMusic.preview,
                        )
                    )
                }
                onlineMusicRepository.loadMusic(musics)
            }
        }
    }

    private fun loadMusicChart() {
        coroutine.launch {
            val musics: ArrayList<MusicListItemModel> = ArrayList()
            val remoteMusics = remoteMusicsService.getChart()

            remoteMusics.tracks.data.forEach { remoteMusic ->
                musics.add(
                    getMusicListItemModel(
                        title = remoteMusic.title,
                        author = remoteMusic.artist.name,
                        album = remoteMusic.album.title,
                        icon = { imageView ->
                            imageView.load(remoteMusic.album.cover)
                        },
                        link = remoteMusic.preview,
                    )
                )
            }

            onlineMusicRepository.loadMusic(musics)

            isLoaded.update { true }
        }
    }


    // Converter from musicItemModel to MusicModel from api
    private fun getMusicListItemModel(
        title: String,
        author: String,
        album: String?,
        icon: (ImageView) -> Unit,
        link: String,
    ): MusicListItemModel {
        return MusicListItemModel(
            title = title,
            author = author,
            album = album,
            icon = icon,
            onClickListener = {
                CurrentMusicsList.musicsList = onlineMusicRepository.getMusicsModelList()
                CurrentMusicsList.currentMusicId =
                    CurrentMusicsList.musicsList!!.indexOfFirst { curMusic ->
                        curMusic.title == title &&
                                curMusic.author == author &&
                                curMusic.album == album &&
                                curMusic.musicPath == link
                    }
                this.navigateToPlayer!!()
            },
            path = link
        )
    }


    fun setNavigateToPlayer(navigateToPlayer: () -> Unit) {
        this.navigateToPlayer = navigateToPlayer
    }
}