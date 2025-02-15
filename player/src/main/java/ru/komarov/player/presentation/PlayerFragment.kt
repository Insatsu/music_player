package ru.komarov.player.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.komarov.player.R
import ru.komarov.player.databinding.FragmentPlayerBinding
import ru.komarov.player.di.PlayerComponentViewModel
import ru.komarov.player.di.PlayerMusicDeps
import ru.komarov.player.domain.PlayerRepository
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class PlayerFragment : Fragment() {
    @Inject
    lateinit var deps: PlayerMusicDeps

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val playerRepository: PlayerRepository get() = deps.playerRepository

    private val coroutineScope = lifecycleScope


    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<PlayerComponentViewModel>().playerComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)

        updatePlayPauseBtn()

        flowUpdateFragmentOnSwitch()
        flowUpdateBtnPlayPause()
        flowUpdateSb()

        coroutineScope.launch {
            while (playerRepository.getCurrentDurationStateFlow() == null) {
                delay(200)
            }
            while (playerRepository.getPlayableStateFlow() == null) {
                delay(200)
            }


            playerRepository.getPlayableStateFlow()?.collect(
                collector = {
//                  TODO: this not works. fix
                    if (it == false)
                        return@collect
                    while (isVisible) {
                        Log.d("sb", "true")
                        delay(1000)
                        if (playerRepository.getCurrentDurationStateFlow() != null)
                            binding.playerFragmentSb.progress =
                                playerRepository.getCurrentDurationStateFlow()!! / 1000
                    }
                }
            )
        }

        binding.playerFragmentBtnPause.setOnClickListener {
            playerRepository.playMusic()
            updatePlayPauseBtn()
        }

        binding.playerFragmentBtnNext.setOnClickListener {
            playerRepository.nextPlayMusic()
            updatePlayPauseBtn()

        }

        binding.playerFragmentBtnPrevious.setOnClickListener {
            playerRepository.previousPlayMusic()
            updatePlayPauseBtn()

        }



        return binding.root
    }

    private fun flowUpdateSb() {
        coroutineScope.launch {
            while (playerRepository.getMaxDurationStateFlow() == null) {
                delay(200)
            }
            playerRepository.getMaxDurationStateFlow()?.collect(
                collector = {
                    withContext(Dispatchers.Main) {
                        Log.d("sb", "max: $it")
                        if (it != null)
                            binding.playerFragmentSb.max = it / 1000
                    }
                }
            )
        }
    }

    private fun flowUpdateFragmentOnSwitch() {
        coroutineScope.launch {
            playerRepository.getCurrentMusicIdFlow().collect(
                collector = {
                    withContext(Dispatchers.Main) {
                        Log.d("btn", "collect шв:}")

                        playerRepository.getPlayerMusic().icon?.let { it(binding.playerFragmentIv) }
                        binding.playerFragmentTvMusicTitle.text =
                            playerRepository.getPlayerMusic().title
                        binding.playerFragmentTvMusicAuthor.text =
                            playerRepository.getPlayerMusic().author
                    }

                    flowUpdateBtnPlayPause()
                    flowUpdateSb()
                }
            )
        }
    }

    private fun flowUpdateBtnPlayPause() {
        coroutineScope.launch {
            while (playerRepository.getPlayableStateFlow() == null) {
                delay(200)
            }
            playerRepository.getPlayableStateFlow()?.collect(
                collector = {
                    withContext(Dispatchers.Main) {
                        Log.d("btn", "collect: ${playerRepository.isPlaying()}")
                        updatePlayPauseBtn()
                    }
                }
            )
        }
    }

    private fun updatePlayPauseBtn() {
        binding.playerFragmentBtnPause.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                if (playerRepository.isPlaying()) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24,
                null
            ),
        )
    }

}