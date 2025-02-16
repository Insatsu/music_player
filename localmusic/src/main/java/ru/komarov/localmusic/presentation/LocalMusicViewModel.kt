package ru.komarov.localmusic.presentation

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import ru.komarov.api.CurrentMusicsList
import ru.komarov.localmusic.R
import ru.komarov.localmusic.domain.LocalMusicRepository
import ru.komarov.musicslist.domain.MusicListItemModel
import java.io.File
import java.io.FileFilter
import javax.inject.Inject

class LocalMusicViewModel @Inject constructor(
    private val localMusicRepository: LocalMusicRepository
) :
    ViewModel() {

    private var navigateToPlayer: (() -> Unit)? = null

    // Get musics data from its file path by MediaMetadataRetriever
    private fun musicMetaDataLoadInDeps(musics: ArrayList<String>): ArrayList<MusicListItemModel> {
        val mmr = MediaMetadataRetriever()
        val musicModels: ArrayList<MusicListItemModel> = ArrayList()

        musics.forEach { musicPath ->
            val id = musics.indexOf(musicPath)
            mmr.setDataSource(musicPath)

            val artBytes = mmr.embeddedPicture
            val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "Unknown"

            val author =
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)
                    ?: mmr.extractMetadata(
                        MediaMetadataRetriever.METADATA_KEY_ARTIST
                    ) ?: "Unknown"

            val album = mmr.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_ALBUM
            )
            // If music has image set it else set default
            if (artBytes != null) {
                val albumArt = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
                musicModels.add(
                    getMusicListItemModel(
                        id = id,
                        title = title, author = author,
                        album = album,
                        icon = { imageView ->
                            imageView.setImageBitmap(albumArt)
                        },
                        filePath = musicPath
                    )
                )

            } else {
                musicModels.add(
                    getMusicListItemModel(
                        id = id,
                        title = title, author = author,
                        album = album,
                        icon = { imageView ->
                            imageView.setImageResource(R.drawable.baseline_auto_awesome_24)
                        },
                        filePath = musicPath
                    )

                )
            }
        }
        mmr.close()
        mmr.release()

        return musicModels
    }


    fun loadMusicFromDownload() {
        val directory = File(localMusicRepository.getLocalFolderPath())

        val musics: ArrayList<String> = ArrayList()

        directory.listFiles(FileFilter { it.extension == "mp3" })?.forEach {
            musics.add(it.path)
        }

        val musicsModels = musicMetaDataLoadInDeps(musics)

        localMusicRepository.loadMusic(musicsModels)

    }


    private fun getMusicListItemModel(
        id: Int,
        title: String,
        author: String,
        album: String?,
        icon: (ImageView) -> Unit,
        filePath: String
    ): MusicListItemModel {
        return MusicListItemModel(
            title = title,
            author = author,
            album = album,
            icon = icon,
            onClickListener = {
                CurrentMusicsList.musicsList = localMusicRepository.getMusicsModelList()
                CurrentMusicsList.currentMusicId = id
                    this.navigateToPlayer!!()
            },
            path = filePath
        )
    }


    fun setNavigateToPlayer(navigateToPlayer: () -> Unit) {
        this.navigateToPlayer = navigateToPlayer
    }
}