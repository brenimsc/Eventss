package com.example.events.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.events.R
import com.example.events.extensions.animateView

class DialogSucessError(context: Context) : Dialog(context) {

    private var sucess: Boolean? = null
    private var description: TextView? = null
    private var title: TextView? = null
    private var img: ImageView? = null
    private var button: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_sucess_error_checkin)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        title = findViewById(R.id.txt_title_dialog)
        description = findViewById(R.id.txt_description_dialog)
        img = findViewById(R.id.img_dialog)
        button = findViewById(R.id.button_ok_dialog)
        setupDialog()

    }

    fun setSucess(sucess: Boolean): DialogSucessError {
        this.sucess = sucess
        return this
    }


    private fun setupDialog() {
        if (sucess == true) {
            setupSucess()
        } else {
            setupError()
        }

        button?.setOnClickListener {
            this.dismiss()
        }

        img?.animateView(200, 200)
    }

    private fun setupError() {
        title?.text = context.getString(R.string.oops)
        description?.text = context.getString(R.string.algp_deu_errado)
        img?.setImageResource(R.drawable.ic_error_black_24dp)
        button?.setTextColor(ContextCompat.getColor(context, R.color.red))
    }

    private fun setupSucess() {
        title?.text = context.getString(R.string.sucesso)
        description?.text = context.getString(R.string.checkin_realizado)
        img?.setImageResource(R.drawable.ic_check_circle)
        button?.setTextColor(ContextCompat.getColor(context, R.color.green))
    }

}