package ru.komarov.musicplayer

import android.app.Application
import ru.komarov.localmusic.di.LocalMusicDepsStore
import ru.komarov.musicplayer.di.AppComponent
import ru.komarov.musicplayer.di.DaggerAppComponent
import ru.komarov.onlinemusic.di.OnlineMusicDepsStore
import javax.inject.Scope


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
        LocalMusicDepsStore.deps = appComponent
        OnlineMusicDepsStore.deps = appComponent

    }

}

@Scope
annotation class AppScope