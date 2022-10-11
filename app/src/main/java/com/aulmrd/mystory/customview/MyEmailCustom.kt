package com.aulmrd.mystory.customview

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns

class MyEmailCustom : MyEditText {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs:AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init()
    }

    private fun init() {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, star: Int, before: Int, count: Int) {
                if (!isValidEmail(text.toString())){
                    error = "format email is invalid!"
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }


    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}