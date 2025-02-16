package ru.komarov.player.domain

import kotlinx.coroutines.flow.Flow
import ru.komarov.api.MusicModel

// Manage player service
interface PlayerRepository {

    fun getCurrentMusicIdFlow(): Flow<Any>

    fun getPlayableStateFlow(): Flow<Boolean?>?

    fun getMaxDurationStateFlow(): Flow<Int?>?

    fun setCurrentPlayerDuration(duration: Int)

    fun getCurrentPlayerDuration(): Int

    fun getCurrentDurationStateFlow(): Int?

    fun getPlayerMusic(): MusicModel

    fun updatePlayerMusicByStep(step: Int): Boolean

    fun playMusic()

    fun nextPlayMusic()

    fun previousPlayMusic()

    fun isPlaying(): Boolean



}