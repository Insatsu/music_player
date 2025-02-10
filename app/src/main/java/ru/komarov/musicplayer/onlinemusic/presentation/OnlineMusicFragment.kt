package ru.komarov.musicplayer.onlinemusic.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.komarov.musicplayer.databinding.FragmentOnlineMusicBinding


class OnlineMusicFragment : Fragment() {
    private var _binding: FragmentOnlineMusicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnlineMusicBinding.inflate(inflater, container, false)


        return binding.root
    }

}