package wuge.social.com.homepage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import filedownloader.MainActivity
import wuge.social.com.R
import wuge.social.com.activity.PersonalActivity1

class SettingsFragment :Fragment() {
    lateinit var v:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v=inflater.inflate(R.layout.home_tab_my, container, false)
        init()
        return v
    }

    private fun init() {
        v.findViewById<TextView>(R.id.textView2).setOnClickListener {
            val intent = Intent(v.context, MainActivity::class.java)
            startActivity(intent)
        }
    }


}