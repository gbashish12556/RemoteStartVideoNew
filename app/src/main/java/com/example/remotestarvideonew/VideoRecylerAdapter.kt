package com.example.remotestarvideonew


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.video_recycler_view_list_row.view.*
import java.util.ArrayList


class ContactsRecyclerViewAdapter(private val data: ArrayList<Video>, private val activity: MainActivity) : RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder>() {

    var constraintLayoutList:MutableList<ConstraintLayout> = mutableListOf();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.video_recycler_view_list_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var itemData = data[position]
        var imageUrl = Constants.IMAGE_BASE_URL+itemData.poster_path

        try {

            Glide.with(activity)
                .load(imageUrl)
                .thumbnail(0.1f)
                .into(holder.icon);

        } catch (e: RuntimeException) {

        }


        if(itemData.original_title != null) {
            holder.title.text = itemData.original_title
        }else if(itemData.original_name != null){
            holder.title.text = itemData.original_name
        }

        if(itemData.release_date != null) {
            holder.releaseDate.text = itemData.release_date
        }else if(itemData.first_air_date != null){
            holder.releaseDate.text = itemData.first_air_date
        }

        holder.description.text = itemData.overview


    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val icon: ImageView
        val title: TextView
        val releaseDate: TextView
        val description: TextView
        val holder: ConstraintLayout

        init {

            icon = itemView.findViewById(R.id.icon)
            title = itemView.findViewById(R.id.title)
            releaseDate = itemView.findViewById(R.id.releaseDate)
            description = itemView.findViewById(R.id.description)
            holder = itemView.findViewById(R.id.holder)
            holder.setOnClickListener{
                clearViews(holder!!)
            }

        }
    }

    fun clearViews(holder:ConstraintLayout){

        if(holder.description!!.isVisible!!) {
            constraintLayoutList.remove(holder)
            holder.description.visibility = View.GONE
        }else{
            holder.description.visibility = View.VISIBLE
        }

        for(x in constraintLayoutList){
            if(x.description.isVisible){
                x.description.visibility = View.GONE
                constraintLayoutList.remove(x)
            }
        }

        constraintLayoutList.add(holder)
    }
    fun setData(data: ArrayList<Video>) {

        this.data.addAll(data)
        notifyDataSetChanged()

    }
}