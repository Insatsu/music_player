package ru.komarov.musicplayer.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import ru.komarov.musicplayer.App
import ru.komarov.musicplayer.domain.PlayerServiceController
import ru.komarov.player.PlayerService
import javax.inject.Inject

class PlayerServiceControllerImpl @Inject constructor(
    private val context: Context,
) : PlayerServiceController {
    private var musicService: PlayerService? = null
    private var isBound = false

    override val maxDuration get() = musicService?.maxDuration
    override val currentDuration get() = musicService?.currentDuration
    override val isPlaying get() = musicService?.isPlaying
    override val isBounded get() = isBound

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

    override fun bindService() {
        Log.d("ServiceConnection", "bind")
        val intent = Intent(context, PlayerService::class.java)
        isBound = context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        Log.d("ServiceConnection", "end bind: $isBound")
    }

    override fun unbindService() {
        Log.d("ServiceConnection", "unbind")
        if (isBound) {
            sendActionToService(PlayerService.ACTION_STOP)
            context.unbindService(connection)
            isBound = false
            musicService = null

        }
    }

    override fun sendActionToService(action: String) {
        val intent = Intent(context, PlayerService::class.java).also {
            it.action = action
        }
        context.startService(intent)
    }


}