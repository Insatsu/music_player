package ru.komarov.musicplayer.data

import android.util.Log
import coil3.size.Dimension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.komarov.api.MusicModel
import ru.komarov.musicplayer.AppScope
import ru.komarov.musicplayer.domain.MusicController
import ru.komarov.musicplayer.domain.PlayerServiceController
import ru.komarov.player.PlayerService
import ru.komarov.player.PlayerService_MembersInjector
import ru.komarov.player.domain.PlayerRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@AppScope
class PlayerRepositoryImpl @Inject constructor(private val playerServiceController: PlayerServiceController) :
    PlayerRepository, MusicController {
    private var musicArrayList: ArrayList<MusicModel> = ArrayList()
    private var currentMusicId = MutableStateFlow(0)
    private var curDur = 0

    override fun getCurrentMusicIdFlow(): Flow<Any> {
        return currentMusicId as MutableStateFlow<Any>
    }

    override fun getPlayableStateFlow(): Flow<Boolean?>? {
        return playerServiceController.isPlaying

    }

    override fun getMaxDurationStateFlow(): Flow<Int?>? {
        return playerServiceController.maxDuration
    }

    override fun setCurrentPlayerDuration(duration: Int) {
        curDur = duration
        playerServiceController.sendActionToService(PlayerService.ACTION_SET_CUR_DUR)
    }

    override fun getCurrentPlayerDuration(): Int {
        return curDur
    }


    override fun getCurrentDurationStateFlow(): Int? {
        return playerServiceController.currentDuration
    }

    override fun getPlayerMusic(): MusicModel {
        Log.d("PlayerRep", "getMusics")

        return musicArrayList[currentMusicId.value]
    }

    override fun updatePlayerMusicByStep(step: Int): Boolean {
        if (step == 1) {
            if (currentMusicId.value + 1 >= musicArrayList.size)
                return false

            currentMusicId.update {
                it + 1
            }
            return true
        } else if (step == -1) {
            if (currentMusicId.value == 0)
                return false

            currentMusicId.update {
                it - 1
            }
            return true
        }
        return false
    }

    override fun playMusic() {
        Log.d("PlayerRep", "playMusic")
        playerServiceController.sendActionToService(PlayerService.ACTION_PLAY_PAUSE)

    }

    override fun nextPlayMusic() {
        Log.d("PlayerRep", "nextPlayMusic")

        playerServiceController.sendActionToService(PlayerService.ACTION_NEXT)
    }

    override fun previousPlayMusic() {
        Log.d("PlayerRep", "previousPlayMusic")

        playerServiceController.sendActionToService(PlayerService.ACTION_PREVIOUS)
    }

    override fun isPlaying(): Boolean {
        Log.d("PlayerRep", "isPlaying")

        return playerServiceController.isPlaying?.value ?: false
    }

    override fun isStarted(): Boolean {
        Log.d("PlayerRep", "isStarted")

        return playerServiceController.isBounded
    }

    override fun setMusicList(musicList: ArrayList<MusicModel>, id: Int) {
        Log.d("PlayerRep", "setMusics")

        this.musicArrayList.clear()
        this.currentMusicId.update { id }
        this.musicArrayList.addAll(musicList)
    }

    override fun startService() {
        Log.d("PlayerRep", "startService")

        if (!isStarted()) {
            playerServiceController.bindService()
            return
        }
        playerServiceController.sendActionToService(PlayerService.ACTION_UPDATE)
    }

    override fun stopService() {
        Log.d("PlayerRep", "stopService")

        playerServiceController.unbindService()

    }
}