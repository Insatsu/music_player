package ru.komarov.musicslist.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.komarov.musicslist.databinding.FragmentMusicListBinding
import ru.komarov.musicslist.di.MusicListComponentViewModel
import ru.komarov.musicslist.domain.MusicListFragmentParent
import javax.inject.Inject


class MusicListFragment : Fragment() {
    @Inject
    lateinit var musicListViewModelFactory: Lazy<MusicListViewModelFactory>

    private var _binding: FragmentMusicListBinding? = null
    private val binding get() = _binding!!

    private var adapter: MusicListAdapter? = null

    private lateinit var vm: MusicListViewModel

    override fun onAttach(context: Context) {
        val depsProvider = parentFragment as MusicListFragmentParent
        ViewModelProvider(
            this,
            MusicListComponentViewModel.Factory(deps = depsProvider.musicListDeps)
        ).get(
            MusicListComponentViewModel::class.java
        ).musicListComponent.inject(this)

        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicListBinding.inflate(inflater, container, false)

        vm = ViewModelProvider(
            this,
            musicListViewModelFactory.get()
        ).get(MusicListViewModel::class.java)
        
        adapter = MusicListAdapter()

        val rv_list = binding.musicListFragmentRv
        rv_list.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        rv_list.adapter = adapter



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
}