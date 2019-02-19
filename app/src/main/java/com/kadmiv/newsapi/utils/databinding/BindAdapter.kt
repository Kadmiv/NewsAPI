package com.kadmiv.newsapi.utils.databinding

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kadmiv.newsapi.R
import java.io.File
import java.net.URL

@BindingAdapter("imageUrl")
fun bindImage(view: ImageView, path: String?) {
    try {
        val options = RequestOptions()
            .centerCrop()
            .error(R.drawable.ic_error)
            .diskCacheStrategy(DiskCacheStrategy.DATA)

        Glide.with(view)
            .load(Uri.parse(path))
            .thumbnail(Glide.with(view.context).load(R.raw.waiting_icon))
            .apply(options)
            .into(view);
    } catch (ex: Exception) {
    }
}

@BindingAdapter("time")
fun convertTime(view: TextView, timeString: String) {
    var newString = timeString.replace("T", " ")
    newString = newString.replace("Z", " ")
    view.text = newString
}


//@BindingAdapter("whatBackground")
//fun isVisible(view: ImageView, @IdRes id: Int) {
//    var checker = view.rootView.findViewById<CheckBox>(id)
//    checker.setOnClickListener {
//        view.background = if (checker.isChecked)
//            view.context.resources.getDrawable(R.color.itemNotEnable)
//        else
//            view.context.resources.getDrawable(R.color.itemEnable)
//    }
//}
