package ru.komarov.localmusic.domain

import ru.komarov.musicslist.di.MusicListDeps
import ru.komarov.musicslist.domain.MusicRepository
import javax.inject.Inject

class LocalMusicListDeps @Inject constructor(override val musicRepository: MusicRepository) :
    MusicListDeps