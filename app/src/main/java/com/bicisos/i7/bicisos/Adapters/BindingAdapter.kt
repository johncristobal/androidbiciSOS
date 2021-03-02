package com.bicisos.i7.bicisos.Adapters

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorText")
fun setErrorMessage(view: TextInputLayout, value: String?) {
    view.requestFocus()
    view.error = value
}
