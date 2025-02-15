package ru.komarov.musicplayer.domain

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.komarov.musicplayer.App
import ru.komarov.player.PlayerService
import javax.inject.Inject

interface PlayerServiceController {

    val maxDuration: MutableStateFlow<Int?>?
//    val currentDuration: MutableStateFlow<Int?>?
    val currentDuration: Int?
    val isPlaying : MutableStateFlow<Boolean?>?
    val isBounded : Boolean


    fun bindService()

    fun unbindService()
    fun sendActionToService(action: String)


}