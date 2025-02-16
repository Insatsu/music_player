package ru.komarov.onlinemusic.domain

import ru.komarov.api.MusicModel
import ru.komarov.musicslist.domain.MusicRepository


interface OnlineMusicRepository : MusicRepository {

    fun getMusicsModelList(): ArrayList<MusicModel>
}