package ru.komarov.onlinemusic.di

import android.provider.ContactsContract.Data
import dagger.Binds
import dagger.Module
import ru.komarov.musicslist.di.MusicListDeps
import ru.komarov.musicslist.domain.MusicRepository
import ru.komarov.onlinemusic.domain.OnlineMusicListDeps
import ru.komarov.onlinemusic.domain.OnlineMusicRepository


@Module(includes = [DataBindModule::class])
class DataModule


@Module
interface DataBindModule {
    @Binds
    @OnlineMusicScope
    fun bindMusicListDeps(onlineMusicListDeps: OnlineMusicListDeps): MusicListDeps


    @Binds
    @OnlineMusicScope
    fun bindOnlineMusicRepository(musicRepository: OnlineMusicRepository): MusicRepository
}