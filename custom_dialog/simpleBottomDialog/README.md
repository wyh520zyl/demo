底部占满屏幕的dialog样例：

![](https://upload-images.jianshu.io/upload_images/6169789-8a357516fb0cd06b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


关键代码：
```
/**
 * 设置dialog居下占满屏幕
 */
private fun changeDialogStyle() {
    window?.let {
        val params = it.attributes
        if (params != null) {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.gravity = Gravity.BOTTOM
            it.attributes = params
        }
    }
}
```