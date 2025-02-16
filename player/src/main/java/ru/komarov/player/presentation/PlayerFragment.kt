package ru.komarov.player.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.komarov.player.R
import ru.komarov.player.databinding.FragmentPlayerBinding
import ru.komarov.player.di.PlayerComponentViewModel
import ru.komarov.player.di.PlayerMusicDeps
import ru.komarov.player.domain.PlayerRepository
import javax.inject.Inject

class PlayerFragment : Fragment() {
    @Inject
    lateinit var deps: PlayerMusicDeps

    @Inject
    lateinit var vm: PlayerViewModel

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val coroutineScope = lifecycleScope


    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<PlayerComponentViewModel>().playerComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)


        flowUpdateFragmentOnSwitch()
        flowUpdateBtnPlayPause()
        flowUpdateSb()

        flowUpdateSbCurrent()

        binding.playerFragmentSb.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                vm.setCurDuration(p1)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.playerFragmentBtnPause.setOnClickListener {
            vm.playerRepository.playMusic()
            updatePlayPauseBtn(vm.isPlay.value)
        }

        binding.playerFragmentBtnNext.setOnClickListener {
            vm.playerRepository.nextPlayMusic()
            updatePlayPauseBtn(vm.isPlay.value)

        }

        binding.playerFragmentBtnPrevious.setOnClickListener {
            vm.playerRepository.previousPlayMusic()
            updatePlayPauseBtn(vm.isPlay.value)

        }


        return binding.root
    }

    private fun flowUpdateSbCurrent() {
        vm.flowUpdateSbCurrent()
        coroutineScope.launch {
            vm.curDuration.asStateFlow().collect(
                collector = {
                    binding.playerFragmentSb.progress = it
                    binding.playerFragmentTvCurDuration.text = vm.getMmSsFromS(it)
                }
            )
        }
    }

    private fun flowUpdateSb() {
        vm.flowUpdateMaxSb()
        coroutineScope.launch {
            vm.maxDuration.asStateFlow().collect(
                collector = {
                    binding.playerFragmentSb.max = it
                    binding.playerFragmentTvMaxDuration.text = vm.getMmSsFromS(it)
                }
            )
        }
    }


    private fun flowUpdateFragmentOnSwitch() {
        vm.flowUpdateFragmentOnSwitch()
        coroutineScope.launch {
            vm.curMusicId.asStateFlow().collect(
                collector = { curMusicId ->
                    vm.playerRepository.getPlayerMusic().icon?.let { it(binding.playerFragmentIv) }
                    binding.playerFragmentTvMusicTitle.text =
                        vm.playerRepository.getPlayerMusic().title
                    binding.playerFragmentTvMusicAuthor.text =
                        vm.playerRepository.getPlayerMusic().author

                    updatePlayPauseBtn()
                }
            )
        }
    }

    private fun flowUpdateBtnPlayPause() {
        vm.flowUpdateBtnPlayPause()
        coroutineScope.launch {
            vm.isPlay.asStateFlow().collect(
                collector = { isPlay ->
                    updatePlayPauseBtn(isPlay)
                }
            )
        }
    }

    private fun updatePlayPauseBtn(isPlay: Boolean = vm.isPlay.value) {
        binding.playerFragmentBtnPause.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                if (isPlay) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24,
                null
            ),
        )
    }

}