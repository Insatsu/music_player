package ru.komarov.musicslist.presentation

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.komarov.musicslist.R
import ru.komarov.musicslist.domain.MusicListItemModel
import ru.komarov.musicslist.databinding.MusicListItemBinding

class MusicListAdapter : RecyclerView.Adapter<MusicListAdapter.MusicListViewHolder>() {
    private val musicsList = ArrayList<MusicListItemModel>()

    class MusicListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = MusicListItemBinding.bind(itemView)
        fun bind(musicItem: MusicListItemModel) = with(binding) {
            musicItem.icon?.let { it(musicListItemIv) }
            musicListItemTvTitle.text = musicItem.title
            musicListItemTvAuthor.text = musicItem.author
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.music_list_item, parent, false)
        return MusicListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return musicsList.size
    }

    override fun onBindViewHolder(holder: MusicListViewHolder, position: Int) {
        holder.bind(musicsList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMusics(musicsList: ArrayList<MusicListItemModel>) {
        this.musicsList.clear()
        this.musicsList.addAll(musicsList)
        notifyDataSetChanged()
    }
}