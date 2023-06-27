package com.example.storyappdicoding.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyappdicoding.R

class PasswordEditTextView : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(char: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(char: CharSequence, start: Int, before: Int, count: Int) {
                if (char.toString().isNotEmpty()) {
                    if (char.length < 8)
                        this@PasswordEditTextView.setError(resources.getString(R.string.pass_min_8), null)
                    else if (!char.matches(".*[A-Z].*".toRegex()))
                        this@PasswordEditTextView.setError(resources.getString(R.string.pass_min_1_up), null)
                    else if (!char.matches(".*[a-z].*".toRegex()))
                        this@PasswordEditTextView.setError(resources.getString(R.string.pass_min_1_low), null)
                    else if (!char.matches(".*[#@%*&_-].*".toRegex()))
                        this@PasswordEditTextView.setError(resources.getString(R.string.pass_min_1_special_char), null)
                }
                if(char.toString().isBlank())
                    this@PasswordEditTextView.error = resources.getString(R.string.required_data)
            }

            override fun afterTextChanged(char: Editable) {
                // Do nothing.
            }
        })
    }
}