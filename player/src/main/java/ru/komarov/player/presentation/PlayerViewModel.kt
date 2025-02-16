package ru.komarov.player.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.komarov.player.domain.PlayerRepository
import javax.inject.Inject
import kotlin.math.floor

class PlayerViewModel @Inject constructor(val playerRepository: PlayerRepository) :
    ViewModel() {
    private val coroutineScope = viewModelScope
    var currentPosSbJob: Job? = null

    var maxDuration = MutableStateFlow(0)
    var curDuration = MutableStateFlow(0)
    var curMusicId = MutableStateFlow(0)
    var isPlay = MutableStateFlow(false)

    // Set current duration in mediaPlayer
    fun setCurDuration(newCurDurSec: Int) {
        val milisec = newCurDurSec * 1000
        playerRepository.setCurrentPlayerDuration(milisec)
    }
    // Converter from sec to "mm:ss"
    fun getMmSsFromS(allSec: Int): String {
        val allMm = allSec / 60f
        val mm: Int = floor(allMm).toInt()
        val sec: Int = floor((allMm - mm) * 60).toInt()

        return "$mm:${if(sec<10) "0" else ""}$sec"
    }

    // Waiting for playableStateFlow and currentDurationStateFlow to initialize and listen last then
    fun flowUpdateSbCurrent() {
        coroutineScope.launch {
            while (playerRepository.getCurrentDurationStateFlow() == null) {
                delay(200)
            }
            while (playerRepository.getPlayableStateFlow() == null) {
                delay(200)
            }

            playerRepository.getPlayableStateFlow()?.collect(
                collector = {
                    Log.d("sb", "playable: ${it}")
                    if (it == false) {
                        currentPosSbJob?.cancelAndJoin()
                        return@collect
                    }
                    Log.d("sb", "playable2: == ${it}")
                    currentPosSbJob = getJobForCurrentSbPos()
                }
            )
        }
    }

    // Listen current music id and update [isPlay]
    fun flowUpdateFragmentOnSwitch() {
        coroutineScope.launch {
            playerRepository.getCurrentMusicIdFlow().collect(
                collector = { musicId ->
                    isPlay.update {
                        playerRepository.isPlaying()
                    }
                    curMusicId.update {
                        musicId as Int
                    }
                }
            )
        }
    }

    // Waiting for playableStateFlow to initialize and listen it then
    fun flowUpdateMaxSb() {
        coroutineScope.launch {
            while (playerRepository.getMaxDurationStateFlow() == null) {
                delay(200)
            }
            playerRepository.getMaxDurationStateFlow()?.collect(
                collector = { maxDur ->
                    if (maxDur != null) {
                        maxDuration.update {
                            maxDur / 1000
                        }
                    }
                }
            )
        }
    }

    // Waiting for playableStateFlow to initialize and listen it then
    fun flowUpdateBtnPlayPause() {
        coroutineScope.launch {
            while (playerRepository.getPlayableStateFlow() == null) {
                delay(200)
            }
            playerRepository.getPlayableStateFlow()?.collect(
                collector = {
                    isPlay.update {
                        playerRepository.isPlaying()
                    }
                }
            )
        }
    }

    // Job that update music current duration every second
    private fun getJobForCurrentSbPos(): Job {
        return coroutineScope.launch {
            while (true) {
                // If current duration equals to max duration (music is end) stop player
                if (curDuration.value == maxDuration.value && maxDuration.value != 0) {
                    playerRepository.playMusic()
                    currentPosSbJob?.cancelAndJoin()
                }
                delay(1000)
                if (playerRepository.getCurrentDurationStateFlow() != null)
                    curDuration.update { playerRepository.getCurrentDurationStateFlow()!! / 1000 }

            }
        }
    }

    override fun onCleared() {
        coroutineScope.launch {
            currentPosSbJob?.cancelAndJoin()
        }
        super.onCleared()
    }


}