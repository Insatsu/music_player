package ru.komarov.localmusic.di

import androidx.lifecycle.ViewModel
import dagger.Component
import ru.komarov.localmusic.presentation.LocalMusicFragment
import javax.inject.Scope


@LocalMusicScope
@Component(modules = [DataModule::class], dependencies = [LocalMusicDeps::class])
internal interface LocalMusicComponent {

    fun inject(fragment: LocalMusicFragment)

    @Component.Builder
    interface Builder {

        fun deps(localMusicDeps: LocalMusicDeps): Builder

        fun build(): LocalMusicComponent
    }


}


interface LocalMusicDeps {
}


object LocalMusicDepsStore {
    lateinit var deps: LocalMusicDeps
}


internal class LocalMusicComponentViewModel : ViewModel() {
    val localMusicComponent =
        DaggerLocalMusicComponent.builder().deps(LocalMusicDepsStore.deps).build()

}

@Scope
annotation class LocalMusicScope