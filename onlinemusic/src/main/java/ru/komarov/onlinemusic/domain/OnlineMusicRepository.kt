package ru.komarov.onlinemusic.domain

import kotlinx.coroutines.flow.StateFlow
import ru.komarov.api.MusicModel
import ru.komarov.musicslist.domain.MusicRepository


interface OnlineMusicRepository : MusicRepository {
    val musicGetListener: StateFlow<String?>

    fun getMusicsModelList(): ArrayList<MusicModel>
}