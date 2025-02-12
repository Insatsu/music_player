package ru.komarov.localmusic.di

import dagger.Binds
import dagger.Module
import ru.komarov.localmusic.domain.LocalMusicListDeps
import ru.komarov.localmusic.domain.LocalMusicRepository
import ru.komarov.musicslist.di.MusicListDeps
import ru.komarov.musicslist.domain.MusicRepository


@Module(includes = [DataBindModule::class])
class DataModule

@Module
interface DataBindModule {
    @Binds
    @LocalMusicScope
    fun bindMusicListDeps(localMusicDeps: LocalMusicListDeps): MusicListDeps

    @Binds
    @LocalMusicScope
    fun bindMusicListRepository(localMusicRepository: LocalMusicRepository): MusicRepository
}