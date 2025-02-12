package ru.komarov.onlinemusic.domain

import ru.komarov.musicslist.di.MusicListDeps
import ru.komarov.musicslist.domain.MusicRepository
import javax.inject.Inject

class OnlineMusicListDeps @Inject constructor(override val musicRepository: MusicRepository) :
    MusicListDeps