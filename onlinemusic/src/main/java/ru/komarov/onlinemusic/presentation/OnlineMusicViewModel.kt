package ru.komarov.onlinemusic.presentation

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import coil3.load
import ru.komarov.api.CurrentMusicsList
import ru.komarov.api.RemoteMusicsService
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.onlinemusic.domain.OnlineMusicRepository
import java.io.File
import java.io.FileFilter
import javax.inject.Inject

class OnlineMusicViewModel @Inject constructor(
    private val onlineMusicRepository: OnlineMusicRepository,
    private val remoteMusicsService: RemoteMusicsService
) : ViewModel() {
    private var navigateToPlayer: (() -> Unit)? = null


    suspend fun loadMusicFromDownload() {
        val musics: ArrayList<MusicListItemModel> = ArrayList()

        val remoteMusics = remoteMusicsService.getChart()


        var i = 0
        remoteMusics.tracks.data.forEach { remoteMusic ->
            musics.add(
                getMusicListItemModel(
                    title = remoteMusic.title,
                    author = remoteMusic.artist.name,
                    album = remoteMusic.album.title,
                    icon = { imageView ->
                        imageView.load(remoteMusic.album.cover)
                    },
//                    link = remoteMusic.link,
                    link = remoteMusic.preview,
                    id = i
                )
            )
            i++
        }


        onlineMusicRepository.loadMusic(musics)
    }


    private fun getMusicListItemModel(
        id: Int,
        title: String,
        author: String,
        album: String?,
        icon: (ImageView) -> Unit,
        link: String
    ): MusicListItemModel {
        return MusicListItemModel(
            title = title,
            author = author,
            album = album,
            icon = icon,
            onClickListener = {
                CurrentMusicsList.musicsList = onlineMusicRepository.getMusicsModelList()
                CurrentMusicsList.currentMusicId = id
                this.navigateToPlayer!!()
            },
            path = link
        )
    }


    fun setNavigateToPlayer(navigateToPlayer: () -> Unit) {
        this.navigateToPlayer = navigateToPlayer
    }
}