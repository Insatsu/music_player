package ru.komarov.onlinemusic.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import coil3.load
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.komarov.musicslist.di.MusicListDeps
import ru.komarov.musicslist.domain.MusicListFragmentParent
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.onlinemusic.databinding.FragmentOnlineMusicBinding
import ru.komarov.onlinemusic.di.OnlineMusicComponentViewModel
import javax.inject.Inject


class OnlineMusicFragment : Fragment(), MusicListFragmentParent {
    @Inject
    override lateinit var musicListDeps: MusicListDeps


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


        return binding.root
    }

}