package ru.komarov.onlinemusic.di

import androidx.lifecycle.ViewModel
import dagger.Component
import ru.komarov.api.RemoteMusicsService
import ru.komarov.onlinemusic.presentation.OnlineMusicFragment
import javax.inject.Scope

@OnlineMusicScope
@Component(modules = [DataModule::class], dependencies = [OnlineMusicDeps::class])
interface OnlineMusicComponent {

    fun inject(onlineMusicFragment: OnlineMusicFragment)

    @Component.Builder
    interface Builder {

        fun deps(onlineMusicDeps: OnlineMusicDeps): Builder

        fun build(): OnlineMusicComponent
    }
}


interface OnlineMusicDeps {
    val musicService: RemoteMusicsService
}

object OnlineMusicDepsStore {
    lateinit var deps: OnlineMusicDeps
}


internal class OnlineMusicComponentViewModel() : ViewModel() {
    val onlineMusicComponent = DaggerOnlineMusicComponent.builder().deps(OnlineMusicDepsStore.deps).build()
}

@Scope
annotation class OnlineMusicScope