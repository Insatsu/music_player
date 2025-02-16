package ru.komarov.localmusic.di

import androidx.lifecycle.ViewModel
import dagger.Component
import ru.komarov.localmusic.presentation.LocalMusicFragment
import javax.inject.Scope

// Local music component with its module
@LocalMusicScope
@Component(modules = [DataModule::class])
internal interface LocalMusicComponent {

    fun inject(fragment: LocalMusicFragment)

    @Component.Builder
    interface Builder {
        fun build(): LocalMusicComponent
    }
}


// Viewmodel for save localMusicComponent on all lifecycle and to inject fragment
internal class LocalMusicComponentViewModel : ViewModel() {
    val localMusicComponent =
//        DaggerLocalMusicComponent.builder().deps(LocalMusicDepsStore.deps).build()
        DaggerLocalMusicComponent.builder().build()

}

@Scope
annotation class LocalMusicScope