package wuge.social.com.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

//滚动的文字
class MarqueTextView : AppCompatTextView {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    )

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?) : super(context!!)

    override fun isFocused(): Boolean {
        //就是把这里返回true即可
        return true
    }
}