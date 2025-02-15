package ru.komarov.musicplayer

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import ru.komarov.api.RemoteMusicsService
import ru.komarov.localmusic.di.LocalMusicDeps
import ru.komarov.localmusic.di.LocalMusicDepsStore
import ru.komarov.musicplayer.di.AppComponent
import ru.komarov.musicplayer.di.DaggerAppComponent
import ru.komarov.onlinemusic.di.OnlineMusicDeps
import ru.komarov.onlinemusic.di.OnlineMusicDepsStore
import ru.komarov.player.di.PlayerMusicDeps
import ru.komarov.player.di.PlayerMusicDepsStore
import ru.komarov.player.domain.PlayerRepository
import javax.inject.Scope


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .context(this)
            .build()

        LocalMusicDepsStore.deps = LocalMusicDepsImpl()
        OnlineMusicDepsStore.deps = OnlineMusicDepsImpl(appComponent.musicService)
        PlayerMusicDepsStore.deps = PlayerMusicDepsImpl(appComponent.playerRepository)


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val channel =
                NotificationChannel(
                    "komarov_music_channel",
                    "Music channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
            channel.setSound(null, null)
            val notManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notManager.createNotificationChannel(channel)
        }
    }

    class LocalMusicDepsImpl : LocalMusicDeps {
    }

    class OnlineMusicDepsImpl(override val musicService: RemoteMusicsService) : OnlineMusicDeps

    class PlayerMusicDepsImpl(override val playerRepository: PlayerRepository) : PlayerMusicDeps
}

@Scope
annotation class AppScope