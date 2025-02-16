package ru.komarov.musicslist.domain

import ru.komarov.musicslist.di.MusicListDeps

// Interface for parent of MusicListFragment. Through it musicList get its data as music list
interface MusicListFragmentParent {
    val musicListDeps: MusicListDeps
}