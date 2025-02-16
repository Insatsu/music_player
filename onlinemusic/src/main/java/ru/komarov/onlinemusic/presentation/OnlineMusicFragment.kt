package ru.komarov.onlinemusic.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.komarov.musicslist.di.MusicListDeps
import ru.komarov.musicslist.domain.BundlesKey
import ru.komarov.musicslist.domain.MusicListFragmentParent
import ru.komarov.musicslist.domain.RequestKey
import ru.komarov.musicslist.domain.SuccessResultMessage
import ru.komarov.onlinemusic.databinding.FragmentOnlineMusicBinding
import ru.komarov.onlinemusic.di.OnlineMusicComponentViewModel
import javax.inject.Inject


class OnlineMusicFragment : Fragment(), MusicListFragmentParent {
    @Inject
    override lateinit var musicListDeps: MusicListDeps

    @Inject
    lateinit var vm: OnlineMusicViewModel

    private var _binding: FragmentOnlineMusicBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<OnlineMusicComponentViewModel>().onlineMusicComponent.inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnlineMusicBinding.inflate(inflater, container, false)

        initMusicList()

        return binding.root
    }

    // Init get music list from api
    private fun initMusicList() {
        vm.setNavigateToPlayer {
            findNavController().navigate(Uri.parse(getString(ru.komarov.api.R.string.nav_to_player)))
        }
        CoroutineScope(Dispatchers.IO).launch {
            vm.loadMusicFromApi()
            withContext(Dispatchers.Main) {
                childFragmentManager.setFragmentResult(
                    RequestKey,
                    bundleOf(BundlesKey to SuccessResultMessage)
                )
            }
        }
    }

}