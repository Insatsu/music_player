package ru.komarov.musicplayer.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.komarov.api.RemoteMusicsService
import ru.komarov.musicplayer.AppScope
import ru.komarov.musicplayer.data.PlayerRepositoryImpl
import ru.komarov.musicplayer.data.PlayerServiceControllerImpl
import ru.komarov.musicplayer.domain.MusicController
import ru.komarov.musicplayer.domain.PlayerServiceController
import ru.komarov.musicplayer.presentation.MainActivity
import ru.komarov.player.PlayerService
import ru.komarov.player.domain.PlayerRepository


@[AppScope Component(modules = [AppModule::class])]
interface AppComponent {

    val musicService: RemoteMusicsService

    val playerRepository: PlayerRepository

    fun inject(playerService: PlayerService)

    fun inject(activity: MainActivity)


    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }
}

@Module(includes = [AppBindModule::class])
class AppModule {
    @[Provides AppScope]
    fun provideMusicService(): RemoteMusicsService = RemoteMusicsService()


}

@Module
interface AppBindModule {
    @[AppScope Binds]
    fun bindPlayerMusicRepository(playerRepositoryImpl: PlayerRepositoryImpl): PlayerRepository

    @[AppScope Binds]
    fun bindMusicState(playerRepositoryImpl: PlayerRepositoryImpl): MusicController

    @[AppScope Binds]
    fun bindPlayerServiceController(playerServiceControllerImpl: PlayerServiceControllerImpl): PlayerServiceController
}
