package ru.komarov.musicplayer.localmusic.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.komarov.musicplayer.databinding.FragmentLocalMusicBinding
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.musicslist.presentation.MusicListFragment

class LocalMusicFragment : Fragment() {
    private var _binding: FragmentLocalMusicBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocalMusicBinding.inflate(inflater, container, false)

        binding.localMusicFragmentFcv.getFragment<MusicListFragment>()


        return binding.root
    }

}