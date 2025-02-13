package ru.komarov.localmusic.presentation

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ru.komarov.localmusic.R
import ru.komarov.musicslist.di.MusicListDeps
import ru.komarov.musicslist.domain.MusicListItemModel
import java.io.File
import java.io.FileFilter
import javax.inject.Inject

class LocalMusicViewModel @Inject constructor(private val musicListDeps: MusicListDeps) :
    ViewModel() {

    private fun musicMetaDataLoadInDeps(musics: ArrayList<String>): ArrayList<MusicListItemModel> {
        val mmr = MediaMetadataRetriever()
        val musicModels: ArrayList<MusicListItemModel> = ArrayList()

        musics.forEach {
            mmr.setDataSource(it)

            val artBytes = mmr.embeddedPicture
            val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "Unknown"

            val author =
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)
                    ?: mmr.extractMetadata(
                        MediaMetadataRetriever.METADATA_KEY_ARTIST
                    ) ?: "Unknown"
            if (artBytes != null) {
                val albumArt = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
                musicModels.add(
                    MusicListItemModel(title = title, author = author, icon = { imageView ->
                        imageView.setImageBitmap(albumArt)
                    })
                )

            } else {
                musicModels.add(
                    MusicListItemModel(title = title, author = author, icon = { imageView ->
                        imageView.setImageResource(R.drawable.baseline_auto_awesome_24)
                    })
                )
            }
        }
        mmr.close()
        mmr.release()

        return musicModels
    }


    fun loadMusicFromDownload() {
        val downloadPath: String = Environment.getExternalStorageDirectory().path + "/Download"
        val directory = File(downloadPath)

        val musics: ArrayList<String> = ArrayList()

        directory.listFiles(FileFilter { it.extension == "mp3" })?.forEach {
            Log.d("test", "${it.path} ")
            musics.add(it.path)
        }

        val musicsModels = musicMetaDataLoadInDeps(musics)
        musicListDeps.musicRepository.loadMusic(musicsModels)


    }
}