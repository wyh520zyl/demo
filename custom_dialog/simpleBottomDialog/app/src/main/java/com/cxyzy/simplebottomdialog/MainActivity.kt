package com.cxyzy.simplebottomdialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1.setOnClickListener { BottomDialog(this).show() }
        button2.setOnClickListener { BottomButtonDialog(this).show() }
        button3.setOnClickListener { BottomDialogWithAnim(this).show() }
        button4.setOnClickListener { BottomDialogWithAnim1(this).show() }
    }
}
