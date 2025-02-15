package ru.komarov.localmusic.domain

import ru.komarov.api.MusicModel
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.musicslist.domain.MusicRepository
import javax.inject.Inject

interface LocalMusicRepository: MusicRepository{

    fun getMusicsModelList(): ArrayList<MusicModel>

    fun getLocalFolderPath(): String
}