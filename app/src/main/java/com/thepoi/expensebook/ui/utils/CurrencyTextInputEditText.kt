package com.thepoi.expensebook.ui.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View.OnFocusChangeListener
import com.google.android.material.textfield.TextInputEditText

class CurrencyTextInputEditTextClass(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {

    init {
        onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) hint = "" else  hint = "000.000,00"
        }

        addTextChangedListener(object : TextWatcher {
            var isUpdating = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed
                if (isUpdating) {
                    isUpdating = false
                    return
                }
                if(s == null) return
                if(s.toString().isBlank()) return

                var sCleaned = s.toString().replace("[,.]".toRegex(), "").toInt().toString()
                if(sCleaned.isNotEmpty()){
                    if(sCleaned.length >= 6){
                        sCleaned = sCleaned.substring(0, sCleaned.length - 5) + "." + sCleaned.substring(sCleaned.length - 5)
                    }
                    if(sCleaned.length >= 3){
                        sCleaned = sCleaned.substring(0, sCleaned.length - 2) + "," + sCleaned.substring(sCleaned.length - 2)
                    }
                    if(sCleaned.length == 2){
                        sCleaned = "0," + sCleaned
                    }
                    if(sCleaned.length == 1){
                        sCleaned = "0,0" + sCleaned
                    }
                    isUpdating = true
                    setText(sCleaned)
                    setSelection(text?.length ?: 0)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text is changed
            }
        })
    }
}