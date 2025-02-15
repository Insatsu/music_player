package ru.komarov.player.di

import androidx.lifecycle.ViewModel
import dagger.Component
import ru.komarov.player.PlayerService
import ru.komarov.player.domain.PlayerRepository
import ru.komarov.player.presentation.PlayerFragment
import javax.inject.Scope

@PlayerScope
@Component(modules = [DataModule::class], dependencies = [PlayerMusicDeps::class])
interface PlayerComponent {

    fun inject(fragment: PlayerFragment)

    @Component.Builder
    interface Builder {

        fun deps(playerMusicDeps: PlayerMusicDeps): Builder

        fun build(): PlayerComponent
    }
}


interface PlayerMusicDeps {
    val playerRepository: PlayerRepository
}

object PlayerMusicDepsStore {
    lateinit var deps: PlayerMusicDeps
}


internal class PlayerComponentViewModel : ViewModel() {
    val playerComponent =
        DaggerPlayerComponent.builder().deps(PlayerMusicDepsStore.deps).build()
}


@Scope
annotation class PlayerScope