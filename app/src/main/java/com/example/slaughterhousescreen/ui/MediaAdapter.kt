// MediaAdapter.kt
package com.example.slaughterhousescreen.ui

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.slaughterhousescreen.data.FileURL
import com.example.slaughterhousescreen.databinding.ItemMediaBinding

class MediaAdapter(
    private val context: Context,
    private val mediaList: List<FileURL>
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    inner class MediaViewHolder(val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val file = mediaList[position]
        if (file.fileName != null) {
            if (isVideo(file.fileName!!)) {
                holder.binding.videoView.apply {
                    visibility = View.VISIBLE
                    setVideoURI(Uri.parse(file.fileName))
                    setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.isLooping = false
                        start()
                    }
                }
                holder.binding.imageView.visibility = View.GONE
            } else {
                holder.binding.imageView.apply {
                    visibility = View.VISIBLE
                    Glide.with(context)
                        .load(file.fileName)
                        .into(this)
                }
                holder.binding.videoView.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = mediaList.size

    private fun isVideo(fileName: String): Boolean {
        val videoExtensions = listOf(".mp4", ".mov", ".avi", ".mkv")
        return videoExtensions.any { fileName.endsWith(it, ignoreCase = true) }
    }
}
