package wuge.social.com.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import wuge.social.com.R

class WebActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        init()
    }
    @SuppressLint("SetJavaScriptEnabled")
    fun init(){
        val webView = findViewById<WebView>(R.id.webview)
        val returnImage = findViewById<ImageView>(R.id.fanhui)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        //获得控件
        //访问网页
        //获得控件
        //访问网页
        val url = intent.getStringExtra("url")!!
        webView.loadUrl(url)
        //  webView.loadUrl("http://gobang.wgzy69.com/?userid="+CommUtil.user.getUser_id() +"&token="+CommUtil.token);
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        //  webView.loadUrl("http://gobang.wgzy69.com/?userid="+CommUtil.user.getUser_id() +"&token="+CommUtil.token);
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                //使用WebView加载显示url
                webView.loadUrl(url)
                //返回true
                return true
            }
        }
        returnImage.setOnClickListener(View.OnClickListener {
            println("点击了返回按钮！")
            if (webView != null) {
                webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
                webView.clearHistory()
                (webView.parent as ViewGroup).removeView(webView)
                webView.destroy()
            }
            finish()
        })
    }
}