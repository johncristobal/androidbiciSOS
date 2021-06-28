package com.bicisos.i7.bicisos.Adapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bicisos.i7.bicisos.Api.ApiUrls
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("errorText")
fun setErrorMessage(view: TextInputLayout, value: String?) {
    view.requestFocus()
    view.error = value
}

@BindingAdapter("src")
fun loadImage(view: ImageView, value: String?) {
    Glide.with(view.context).load(value).into(view)
}

@BindingAdapter("lateralimg")
fun loadImagelateral(view: ImageView, value: String?) {
    val urlImg = ApiUrls.urlApi+"/"+value+"/lateral.png"

    Glide.with(view.context)
        .load(urlImg)
        .circleCrop()
        .signature(ObjectKey(System.currentTimeMillis()))
        .into(view)
}

@BindingAdapter("visible")
fun bindVisible(view: View, visible: Boolean){
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("formatText")
fun bindFormatText(view: TextView, data: String){
    //2021-06-15T15:28:35.601Z
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val format_output = SimpleDateFormat("dd/MM/yyyy")

    try {
        var dt = format.parse(data)
        val c: Calendar = Calendar.getInstance()
        c.time = dt!!
        c.add(Calendar.YEAR, 1)
        dt = c.time

        view.text = format_output.format(dt!!)

    }catch (e: Exception){
        view.text = data
    }
}