package ru.komarov.musicslist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.komarov.musicslist.domain.MusicRepository
import ru.komarov.musicslist.presentation.MusicListFragment
import javax.inject.Scope


@MusicListScope
@Component(
    modules = [FragmentModule::class],
    dependencies = [MusicListDeps::class]
)
internal interface MusicListComponent {

    fun inject(musicListFragment: MusicListFragment)

    @Component.Builder
    interface Builder {

        fun deps(deps: MusicListDeps): Builder

        fun build(): MusicListComponent
    }

}

interface MusicListDeps {
    val musicRepository: MusicRepository
}


internal class MusicListComponentViewModel(private val deps: MusicListDeps) : ViewModel() {
    val musicListComponent =
        DaggerMusicListComponent.builder().deps(deps).build()

        @Suppress("UNCHECKED_CAST")
        class Factory(private val deps: MusicListDeps) : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MusicListComponentViewModel(deps = deps) as T
            }
        }


}


@Scope
annotation class MusicListScope