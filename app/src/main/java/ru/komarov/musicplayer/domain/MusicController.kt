package ru.komarov.musicplayer.domain

import ru.komarov.api.MusicModel

interface MusicController {

    fun isStarted(): Boolean

    fun setMusicList(musicList: ArrayList<MusicModel>, id: Int)

    fun startService()

    fun stopService()
}