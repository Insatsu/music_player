package ru.komarov.musicslist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.komarov.musicslist.di.DaggerMusicListComponent
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.musicslist.domain.MusicRepository

class MusicListViewModel(
    private val musicRepository: MusicRepository
) : ViewModel() {
    private val mutableRep = MutableLiveData(musicRepository)
    val rep: LiveData<MusicRepository> = mutableRep

    fun updateMusic(){

    }

    private val musics = MutableLiveData(musicRepository.getMusic())
    val musics2: LiveData<ArrayList<MusicListItemModel>> = musics


    fun test(){
        musics.value = musicRepository.getMusic()
    }


}