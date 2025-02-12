package ru.komarov.musicslist.presentation

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.komarov.musicslist.data.MusicRepositoryImpl
import ru.komarov.musicslist.domain.MusicRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MusicListViewModelFactory @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MusicListViewModel(
            musicRepository = musicRepository
        ) as T

    }

}