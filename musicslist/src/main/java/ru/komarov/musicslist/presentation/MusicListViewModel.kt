package ru.komarov.musicslist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.komarov.musicslist.domain.MusicListItemModel

class MusicListViewModel : ViewModel() {
    private val mutableMusicList = MutableLiveData(ArrayList<MusicListItemModel>())
    val musicList: LiveData<ArrayList<MusicListItemModel>> get() = mutableMusicList



}