package com.cxyzy.safedialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        showDialogTv.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
//                withContext(Dispatchers.IO) { Thread.sleep(2000) }
                val dialog = DemoDialog(this@SecondActivity)
                dialog.show()
            }
        }
    }
}
