package com.bicisos.i7.bicisos.Adapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorText")
fun setErrorMessage(view: TextInputLayout, value: String?) {
    view.requestFocus()
    view.error = value
}

@BindingAdapter("src")
fun loadImage(view: ImageView, value: String?) {
    Glide.with(view.context).load(value).into(view)
}

@BindingAdapter("visible")
fun bindVisible(view: View, visible: Boolean){
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}