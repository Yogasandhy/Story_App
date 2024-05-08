package com.example.subintermediate.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.subintermediate.R

class MyPassword : AppCompatEditText, View.OnTouchListener {

    private var buttonEnabler: (() -> Unit)? = null

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
        hint = "Masukkan password Anda"

        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 8) {
                    error = context.getString(R.string.password_tidak_boleh_kurang_dari_8_karakter)
                }
                buttonEnabler?.invoke()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    fun setButtonEnabler(enabler: () -> Unit) {
        buttonEnabler = enabler
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }
}
