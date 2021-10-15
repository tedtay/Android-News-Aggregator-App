package com.example.useraccount

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.koushikdutta.ion.Ion.with
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view.view.*
import java.lang.System.`in`
import java.lang.System.load

/**
 * @author Ted Taylor 976335
 * @param articleLength to specify how many articles are being passed
 * @param MutableList x 7 to pass json data
 * @param headline, editor, source, date, image, description
 * @return RecyclerView.Adapter for the news feed
 */
class RecyclerAdapter( var articleLength: Int , var headlineList: MutableList<String>, var editorList: MutableList<String>,var sourceList: MutableList<String>,
                       var dateList: MutableList<String>,var urlToImageList: MutableList<String>,var descriptionList: MutableList<String>,
                       var urlList: MutableList<String>, var headline: Int,var editor: Int, var source: Int, var date: Int,var image: Int,
                       var description: Int) : RecyclerView.Adapter<RecyclerAdapter.NewsViewHolder>(){

    /**
     * when RecyclerAdapter is created the recycler layout is inflated and the NewsViewHolder
     * inner class is called upon
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view, parent, false)
        return NewsViewHolder(view,headline,editor,source,date,image,description)

    }

    /**
     * @param holder for each news item
     * @param position current news position
     *
     * Binds TextView's and ImageView to data we collected from json
     */
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        try {
            for (i in headlineList) {
                holder.headlineText.text = headlineList[position]
            }
            for (i in editorList) {
                if (holder.editorText.text.isEmpty()) {
                    holder.editorText.text = "No editor available"
                } else {
                    holder.editorText.text = editorList[position]
                }
            }
            for (i in sourceList) {
                holder.sourceText.text = sourceList[position]
            }
            for (i in dateList) {
                holder.dateText.text = dateList[position]
            }
            for (i in urlToImageList) {
                //used https://www.youtube.com/watch?v=japhFMXAJZw for loading image
                Picasso.get().load(urlToImageList[position]).into(holder.imageImageView)
            }
            for (i in descriptionList) {
                if (holder.descriptionText.text.isEmpty()) {
                    holder.descriptionText.text = "No description available"
                } else {
                    holder.descriptionText.text = descriptionList[position]
                }
            }
        }catch(ex: ConcurrentModificationException ){
            println("Exception on Recycler Adapter onBindViewHolder() = $ex")
        }

    }


    /**
     * returns current item count to populate recycler
     *
     */
    override fun getItemCount(): Int {
            return articleLength
    }

    /**
     * @param item,headline,editor,source,date,image,description
     *        to handle each individual news item
     * @return RecyclerView.ViewHolder to return the reycler view for
     *         this item
     * used to handle each item's data and listen for clicks
     * listener, adapted from https://www.youtube.com/watch?v=ai9rSGcDhyQ https://www.youtube.com/watch?v=ihPM8Qvz0XY
     */
    inner class  NewsViewHolder(item: View,headline: Int,editor: Int,source: Int,date: Int,image: Int,description: Int) : RecyclerView.ViewHolder(item) {
        var headlineText: TextView = item.findViewById(headline) as TextView
        var editorText: TextView = item.findViewById(editor) as TextView
        var sourceText: TextView = item.findViewById(source) as TextView
        var dateText: TextView = item.findViewById(date) as TextView
        var imageImageView: ImageView = item.findViewById(image) as ImageView
        var descriptionText: TextView = item.findViewById(description) as TextView

        init {
            item.setOnClickListener {
               item: View ->
                run {
                    val position = adapterPosition
                    val url = urlList[adapterPosition]
                    Toast.makeText(item.context, "url at position $position : $url ", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(url))
                    startActivity(item.context,intent, null )
                }
            }
        }

    }

}

