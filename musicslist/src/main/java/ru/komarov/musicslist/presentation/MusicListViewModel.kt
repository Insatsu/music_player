package ru.komarov.musicslist.presentation

import androidx.lifecycle.ViewModel
import ru.komarov.musicslist.domain.MusicRepository

class MusicListViewModel(
    private val musicRepository: MusicRepository
) : ViewModel() {

    fun getMusic(filter: String? = null) = musicRepository.getMusic(filter)

}