package com.aulmrd.mystory.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet

class MyPassCustom : MyEditText {
    constructor(context : Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr : Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, count: Int, before: Int) {
                if (text?.length in 1..5){
                    error = "Minimal Length {6} characters"
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }
}