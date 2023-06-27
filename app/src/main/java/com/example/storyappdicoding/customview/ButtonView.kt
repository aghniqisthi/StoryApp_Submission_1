package com.example.storyappdicoding.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.storyappdicoding.R

class ButtonView: AppCompatButton {

    private val enableBg = ContextCompat.getDrawable(context, R.color.blue) as Drawable
    private val disableBg = ContextCompat.getDrawable(context, R.color.blue_off) as Drawable

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
        background = if(isEnabled) enableBg else disableBg
        text = if(isEnabled) resources.getString(R.string.submit) else resources.getString(R.string.fill_all_data)
    }

    private fun init() {
        setTextColor(resources.getColor(R.color.white))
    }
}