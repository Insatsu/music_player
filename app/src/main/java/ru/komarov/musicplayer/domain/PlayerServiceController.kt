package ru.komarov.musicplayer.domain

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log
import ru.komarov.musicplayer.App
import ru.komarov.player.PlayerService
import javax.inject.Inject

class PlayerServiceController @Inject constructor(
    private val context: Context,
) {
    private var musicService: PlayerService? = null
    private var isBound = false

    val maxDuration get() = musicService?.maxDuration
    val currentDuration get() = musicService?.currentDuration
    val isPlaying get() = musicService?.isPlaying
    val isBounded get() = isBound

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            Log.d("ServiceConnection", "connected")

            musicService = (binder as PlayerService.MusicBinder).getService()
            (context.applicationContext as App).appComponent.inject(musicService!!)
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d("ServiceConnection", "disconnected")
        }
    }

    fun bindService() {
        Log.d("ServiceConnection", "bind")
        val intent = Intent(context, PlayerService::class.java)
        isBound = context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        Log.d("ServiceConnection", "end bind: $isBound")
    }

    fun unbindService() {
        Log.d("ServiceConnection", "unbind")
        if (isBound) {
            sendActionToService(PlayerService.ACTION_STOP)
            context.unbindService(connection)
            isBound = false
            musicService = null

        }
    }

    fun sendActionToService(action: String) {
        val intent = Intent(context, PlayerService::class.java).also {
            it.action = action
        }
        context.startService(intent)
    }


}