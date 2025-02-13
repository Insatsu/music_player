package ru.komarov.musicslist.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.komarov.musicslist.databinding.FragmentMusicListBinding
import ru.komarov.musicslist.di.MusicListComponentViewModel
import ru.komarov.musicslist.domain.BundlesKey
import ru.komarov.musicslist.domain.MusicListFragmentParent
import ru.komarov.musicslist.domain.RequestKey
import ru.komarov.musicslist.domain.SuccessResultMessage
import javax.inject.Inject

// Родительский фрагмент должен реализовывать интерфейс [MusicListFragmentParent] для успешного их взаимодействия
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
//        

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicListBinding.inflate(inflater, container, false)

        vmInit()
        adapterInit()
        updateListener()

        return binding.root
    }

    private fun updateListener() {
        parentFragmentManager.setFragmentResultListener(
            RequestKey,
            viewLifecycleOwner
        ) { key, bundle ->
            val res = bundle.getString(
                BundlesKey
            )
            when (res) {
                SuccessResultMessage -> updateAdapter()
            }
        }
    }

    private fun updateAdapter() {
        adapter?.setMusics(vm.getMusic())
    }

    private fun adapterInit() {
        adapter = MusicListAdapter()

        val rv_list = binding.musicListFragmentRv
        rv_list.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        rv_list.adapter = adapter
    }

    private fun vmInit() {
        vm = ViewModelProvider(
            this,
            musicListViewModelFactory.get()
        ).get(MusicListViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
}