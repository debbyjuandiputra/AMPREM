package com.amprem.securevideo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VideoAdapter(
    private val videos: List<Video>,
    private val onClick: (Video) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtStepNumber: TextView = itemView.findViewById(R.id.txtStepNumber)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtDescription: TextView = itemView.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.txtStepNumber.text = video.step.toString()
        holder.txtTitle.text = video.title
        holder.txtDescription.text = video.description
        holder.itemView.setOnClickListener { onClick(video) }
    }

    override fun getItemCount(): Int = videos.size
}
