package ru.komarov.player

import android.app.Activity
import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.webkit.URLUtil
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.MessagingStyle
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.komarov.api.MusicModel
import ru.komarov.player.domain.PlayerRepository
import java.io.File
import javax.inject.Inject
import kotlin.io.path.Path


class PlayerService : Service() {
    @Inject
    lateinit var playerRepository: PlayerRepository

    private val binder = MusicBinder()

    private var musicPlayer: MediaPlayer? = null
        set(value) {
            field?.release()
            field = value
        }


    private val channelId = "komarov_music_channel"
    private val notificationId = 1

    private val currentMusic = MutableStateFlow<MusicModel?>(null)

    val maxDuration = MutableStateFlow(musicPlayer?.duration)

    //    val currentDuration = MutableStateFlow(musicPlayer?.currentPosition)
    val currentDuration get() = musicPlayer?.currentPosition
    val isPlaying = MutableStateFlow(musicPlayer?.isPlaying)


    inner class MusicBinder : Binder() {
        fun getService() = this@PlayerService
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("Service", "onBind")

        return binder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (intent.action) {
                ACTION_PREVIOUS -> {
                    previous()
                }

                ACTION_PLAY_PAUSE -> {
                    if (currentMusic.value == null) {
                        setupMusicPlayer()

                    }
                    playPause()
                }

                ACTION_NEXT -> {
                    next()
                }

                ACTION_STOP -> {
                    musicPlayer = null

                    stopSelf()
                    stopForeground(STOP_FOREGROUND_DETACH)
                }

                ACTION_UPDATE -> {
                    currentMusic.update { playerRepository.getPlayerMusic() }
                    if (currentMusic.value != null)
                        play(currentMusic.value!!)
                }

                else -> {
                    currentMusic.update { playerRepository.getPlayerMusic() }
                    if (currentMusic.value != null)
                        play(currentMusic.value!!)
                }
            }
        }
        updateMaxDuration()
        return START_STICKY
    }


    private fun sendNotification(music: MusicModel) {
        if (musicPlayer == null) {
            setupMusicPlayer()
        }

        val play_pause_drawable =
            if (musicPlayer?.isPlaying!!) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24
        val style =
            androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2)


        val notification = NotificationCompat.Builder(this, channelId)
            .setStyle(style)
            .setSmallIcon(R.drawable.va_placeholder_50)
            .setShowWhen(false)
            .setContentTitle(music.title)
            .setContentText(music.author)
            .addAction(
                R.drawable.baseline_skip_previous_24,
                "Previous",
                createPreviousPendingIntent()
            )
            .addAction(play_pause_drawable, "Play", createPlayPausePendingIntent())
            .addAction(R.drawable.baseline_skip_next_24, "Next", createNextPendingIntent())
            .setSound(null)
            .build()

        Log.d("notif", "start")
        startForeground(notificationId, notification)
    }

    fun createPreviousPendingIntent(): PendingIntent {
        val intent = Intent(this, PlayerService::class.java).apply {
            action = ACTION_PREVIOUS
        }

        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun createNextPendingIntent(): PendingIntent {
        val intent = Intent(this, PlayerService::class.java).apply {
            action = ACTION_NEXT
        }

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    fun createPlayPausePendingIntent(): PendingIntent {
        val intent = Intent(this, PlayerService::class.java).apply {
            action = ACTION_PLAY_PAUSE
        }

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }


    fun next() {
        val isNextExist = playerRepository.updatePlayerMusicByStep(1)
        if (!isNextExist)
            return

        setupMusicPlayer()
    }

    fun previous() {
        val isPreviousExist = playerRepository.updatePlayerMusicByStep(-1)
        if (!isPreviousExist)
            return

        setupMusicPlayer()
    }


    fun playPause() {
        Log.d("Service", "playPause: ${musicPlayer}")

        if (musicPlayer == null) {
            setupMusicPlayer()
        }

        if (musicPlayer?.isPlaying!!)
            musicPlayer?.pause()
        else
            musicPlayer?.start()

        isPlaying.update { musicPlayer?.isPlaying }

//        updateCurrentDuration()



        sendNotification(currentMusic.value!!)
    }


    fun play(music: MusicModel) {
        musicPlayer = MediaPlayer()
        updateMusicPlayer(music)

//        updateMaxDuration()
//        updateCurrentDuration()
    }

    override fun onDestroy() {
        Log.d("Service", "destroy")

        musicPlayer?.release()
        super.onDestroy()
    }

    private fun setupMusicPlayer() {
        musicPlayer = MediaPlayer()
        currentMusic.update { playerRepository.getPlayerMusic() }
        if (currentMusic.value != null)
            play(currentMusic.value!!)

//        updateMaxDuration()
    }

//    private fun updateCurrentDuration() {
////        currentDuration.update {
////            musicPlayer?.currentPosition
////        }
//    }

    private fun updateMaxDuration() {
        Log.d("Service", "max dur: ${musicPlayer?.duration}")
        maxDuration.update { musicPlayer?.duration }
    }

    private fun updateMusicPlayer(music: MusicModel) {

        if (!URLUtil.isValidUrl(music.musicPath))
            musicPlayer?.setDataSource(this, Uri.fromFile(File(music.musicPath)))
        else if (URLUtil.isNetworkUrl(music.musicPath))
            musicPlayer?.setDataSource(this, Uri.parse(music.musicPath))

        musicPlayer?.prepareAsync()
        musicPlayer?.setOnPreparedListener {
            musicPlayer?.start()
            sendNotification(music)
//            updateDuration()
        }
    }

    companion object {
        const val ACTION_PLAY_PAUSE = "action_play_pause"
        const val ACTION_NEXT = "action_next"
        const val ACTION_PREVIOUS = "action_previous"
        const val ACTION_STOP = "action_stop"
        const val ACTION_UPDATE = "action_update"
    }
}