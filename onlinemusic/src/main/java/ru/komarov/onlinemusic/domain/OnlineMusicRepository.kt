package ru.komarov.onlinemusic.domain

import ru.komarov.api.MusicModel
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.musicslist.domain.MusicRepository
import javax.inject.Inject


interface OnlineMusicRepository : MusicRepository {

    fun getMusicsModelList(): ArrayList<MusicModel>
}