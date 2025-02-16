package ru.komarov.player.di

import androidx.lifecycle.ViewModel
import dagger.Component
import ru.komarov.player.PlayerService
import ru.komarov.player.domain.PlayerRepository
import ru.komarov.player.presentation.PlayerFragment
import javax.inject.Scope

// Player component with its dependencies
@PlayerScope
@Component(dependencies = [PlayerMusicDeps::class])
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

// Receive deps from activity
object PlayerMusicDepsStore {
    lateinit var deps: PlayerMusicDeps
}


// Viewmodel for save playerComponent on all lifecycle and to inject fragment
internal class PlayerComponentViewModel : ViewModel() {
    val playerComponent =
        DaggerPlayerComponent.builder().deps(PlayerMusicDepsStore.deps).build()
}


@Scope
annotation class PlayerScope