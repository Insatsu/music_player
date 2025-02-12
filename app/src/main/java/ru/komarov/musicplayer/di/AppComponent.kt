package ru.komarov.musicplayer.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.komarov.api.MusicsService
import ru.komarov.localmusic.di.LocalMusicDeps
import ru.komarov.musicplayer.AppScope
import ru.komarov.onlinemusic.di.OnlineMusicDeps


@[AppScope Component(modules = [AppModule::class])]
interface AppComponent : LocalMusicDeps, OnlineMusicDeps {

    override val musicService: MusicsService


    @Component.Builder
    interface Builder {

        fun build(): AppComponent
    }
}

@Module
class AppModule {
    @[Provides AppScope]
    fun provideMusicService(): MusicsService = MusicsService()
}

