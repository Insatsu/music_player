package ru.komarov.musicslist.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.musicslist.databinding.FragmentMusicListBinding


class MusicListFragment : Fragment() {
    private var _binding: FragmentMusicListBinding? = null
    private val binding get() = _binding!!

    private var adapter: MusicListAdapter? = null
    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicListBinding.inflate(inflater, container, false)

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