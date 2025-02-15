package ru.komarov.musicslist.di

import dagger.Module
import dagger.Provides
import ru.komarov.musicslist.domain.MusicRepository
import ru.komarov.musicslist.presentation.MusicListViewModelFactory


@Module
class FragmentModule {
    @Provides
    @MusicListScope
    fun provideMusicViewModelFactory(musicRepository: MusicRepository): MusicListViewModelFactory {
        return MusicListViewModelFactory(musicRepository = musicRepository)
    }
}