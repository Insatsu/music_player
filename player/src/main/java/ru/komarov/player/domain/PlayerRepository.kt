package ru.komarov.player.domain

import kotlinx.coroutines.flow.Flow
import ru.komarov.api.MusicModel

interface PlayerRepository {

    fun getCurrentMusicIdFlow(): Flow<Any>
    fun getPlayableStateFlow(): Flow<Boolean?>?

    fun getPlayerMusic(): MusicModel

    fun updatePlayerMusicByStep(step: Int): Boolean

    fun playMusic()

    fun nextPlayMusic()

    fun previousPlayMusic()

    fun isPlaying(): Boolean



}