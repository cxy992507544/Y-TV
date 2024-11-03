package com.example.myapplication

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.example.myapplication.model.MovieDetails

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {
    override fun onBindDescription(viewHolder: ViewHolder, item: Any) {
        if (item is MovieDetails) {
            viewHolder.title.text = item.title
            viewHolder.subtitle.text = item.actors
            viewHolder.body.text = item.description
        }
    }
}
