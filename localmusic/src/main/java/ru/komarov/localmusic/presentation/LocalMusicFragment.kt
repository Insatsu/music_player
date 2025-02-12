package ru.komarov.localmusic.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.komarov.localmusic.R
import ru.komarov.localmusic.databinding.FragmentLocalMusicBinding
import ru.komarov.localmusic.di.LocalMusicComponentViewModel
import ru.komarov.musicslist.di.MusicListDeps
import ru.komarov.musicslist.domain.MusicListFragmentParent
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.musicslist.presentation.MusicListFragment
import javax.inject.Inject


class LocalMusicFragment : Fragment(), MusicListFragmentParent {

    @Inject
    override lateinit var musicListDeps: MusicListDeps

    private var _binding: FragmentLocalMusicBinding? = null
    private val binding get() = _binding!!


    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<LocalMusicComponentViewModel>().localMusicComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocalMusicBinding.inflate(inflater, container, false)

        binding.localMusicFragmentFcv.getFragment<MusicListFragment>()


        return binding.root
    }

}