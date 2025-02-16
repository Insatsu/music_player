package ru.komarov.localmusic.domain

import ru.komarov.api.MusicModel
import ru.komarov.musicslist.domain.MusicRepository

interface LocalMusicRepository: MusicRepository{
    // Get music in api model
    fun getMusicsModelList(): ArrayList<MusicModel>

    fun getLocalFolderPath(): String
}