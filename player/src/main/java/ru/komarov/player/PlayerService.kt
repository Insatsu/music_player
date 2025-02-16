package ru.komarov.player

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.webkit.URLUtil
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.komarov.api.MusicModel
import ru.komarov.player.domain.PlayerRepository
import ru.komarov.player.domain.channelId
import java.io.File
import javax.inject.Inject


class PlayerService : Service() {
    @Inject
    lateinit var playerRepository: PlayerRepository

    private val binder = MusicBinder()

    private var musicPlayer: MediaPlayer? = null
        set(value) {
            field?.release()
            field = value
        }


    private val currentMusic = MutableStateFlow<MusicModel?>(null)

    //  Music player fields
    val maxDuration = MutableStateFlow(musicPlayer?.duration)
    val currentDuration get() = musicPlayer?.currentPosition
    val isPlaying = MutableStateFlow(musicPlayer?.isPlaying)

    private lateinit var mediaSession: MediaSessionCompat

    inner class MusicBinder : Binder() {
        fun getService() = this@PlayerService
    }

    override fun onBind(intent: Intent): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "Komarov music")
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
                    setupMusicPlayer()
                }

                ACTION_SET_CUR_DUR -> {
                    setCurrentDurationMediaPlayer(playerRepository.getCurrentPlayerDuration())
                }

                else -> {
                    setupMusicPlayer()
                }
            }
        }
        updateMaxDuration()
        return START_STICKY
    }


    // Create Notification with buttons for manage music player
    private fun sendNotification(music: MusicModel) {
        if (musicPlayer == null) {
            setupMusicPlayer()
        }

        val notificationId = 1

        val play_pause_drawable =
            if (musicPlayer?.isPlaying!!) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24

        val style =
            androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2)
                .setMediaSession(mediaSession.sessionToken)

        val notification = NotificationCompat.Builder(this, channelId)
            .setStyle(style)
            .setSmallIcon(R.drawable.baseline_queue_music_24)
            .setSound(null)
            .setOnlyAlertOnce(true)
            .setContentTitle(music.title)
            .setContentText(music.author)
            .addAction(
                R.drawable.baseline_skip_previous_24,
                "Previous",
                createPreviousPendingIntent()
            )
            .addAction(play_pause_drawable, "Play", createPlayPausePendingIntent())
            .addAction(R.drawable.baseline_skip_next_24, "Next", createNextPendingIntent())
            .build()

        // Add seekbar if android version is Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mediaSession.setMetadata(
                MediaMetadataCompat.Builder().putLong(
                    MediaMetadataCompat.METADATA_KEY_DURATION, musicPlayer!!.duration.toLong()
                ).build()
            )
            mediaSession.setPlaybackState(getPlayBackState())
            mediaSession.setCallback(object : MediaSessionCompat.Callback() {

                override fun onPlay() {
                    super.onPlay()
                    playPause()
                }

                override fun onPause() {
                    super.onPause()
                    playPause()
                }

                override fun onSkipToNext() {
                    super.onSkipToNext()
                    next()
                }

                override fun onSkipToPrevious() {
                    super.onSkipToPrevious()
                    previous()
                }

                override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
                    playPause()
                    return super.onMediaButtonEvent(mediaButtonEvent)
                }

                override fun onSeekTo(pos: Long) {
                    super.onSeekTo(pos)
                    setCurrentDurationMediaPlayer(pos.toInt())

                    mediaSession.setPlaybackState(getPlayBackState())
                }
            })
        }

        startForeground(notificationId, notification)
    }

    //    test
    fun getPlayBackState(): PlaybackStateCompat {
        val playbackSpeed = if (isPlaying.value == true) 1F else 0F

        return PlaybackStateCompat.Builder().setState(
            if (musicPlayer?.isPlaying == true) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
            musicPlayer!!.currentPosition.toLong(), playbackSpeed
        )
            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_SEEK_TO or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
            .build()
    }

    private fun next() {
        val isNextExist = playerRepository.updatePlayerMusicByStep(1)
        if (!isNextExist)
            return
        setupMusicPlayer()

        mediaSession.setPlaybackState(getPlayBackState())
    }

    private fun previous() {
        val isPreviousExist = playerRepository.updatePlayerMusicByStep(-1)
        if (!isPreviousExist)
            return

        setupMusicPlayer()
        mediaSession.setPlaybackState(getPlayBackState())

    }

    private fun playPause() {
        if (musicPlayer == null) {
            setupMusicPlayer()
        }

        if (musicPlayer?.isPlaying!!)
            musicPlayer?.pause()
        else
            musicPlayer?.start()

        isPlaying.update { musicPlayer?.isPlaying }

        sendNotification(currentMusic.value!!)
        mediaSession.setPlaybackState(getPlayBackState())
    }

    private fun setCurrentDurationMediaPlayer(dur: Int) {
        musicPlayer?.seekTo(dur)
    }

    private fun updateMaxDuration() {
        maxDuration.update { musicPlayer?.duration }
    }

    private fun setupMusicPlayer() {
        musicPlayer = MediaPlayer()
        currentMusic.update { playerRepository.getPlayerMusic() }
        if (currentMusic.value != null)
            updateMusicPlayer(currentMusic.value!!)
    }

    // Update music player. Defines what type of music source is: network or file
    // and then uses needed way to get music
    private fun updateMusicPlayer(music: MusicModel) {
        if (!URLUtil.isValidUrl(music.musicPath))
            musicPlayer?.setDataSource(this, Uri.fromFile(File(music.musicPath)))
        else if (URLUtil.isNetworkUrl(music.musicPath))
            musicPlayer?.setDataSource(this, Uri.parse(music.musicPath))

        musicPlayer?.prepareAsync()
        musicPlayer?.setOnPreparedListener {
            musicPlayer?.start()
            sendNotification(music)
        }
    }


    // Create pending intents
    private fun createPreviousPendingIntent(): PendingIntent {
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

    private fun createNextPendingIntent(): PendingIntent {
        val intent = Intent(this, PlayerService::class.java).apply {
            action = ACTION_NEXT
        }

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun createPlayPausePendingIntent(): PendingIntent {
        val intent = Intent(this, PlayerService::class.java).apply {
            action = ACTION_PLAY_PAUSE
        }

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onDestroy() {
        Log.d("Service", "destroy")

        musicPlayer?.release()
        super.onDestroy()
    }

    companion object {
        const val ACTION_PLAY_PAUSE = "action_play_pause"
        const val ACTION_NEXT = "action_next"
        const val ACTION_PREVIOUS = "action_previous"
        const val ACTION_STOP = "action_stop"
        const val ACTION_UPDATE = "action_update"
        const val ACTION_SET_CUR_DUR = "action_set_cur_dur"
    }
}