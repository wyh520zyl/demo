package wuge.social.com.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;


import wuge.social.com.R;


/**
 * Created by Pan_ on 2015/2/2.
 */
public class CustomImageView extends androidx.appcompat.widget.AppCompatImageView {
    private String url;
    private boolean isAttachedToWindow;

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context) {
        super(context);
    }


    @Override
    public void onAttachedToWindow() {
        isAttachedToWindow = true;
        setImageUrl(url);
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
       // Picasso.with(getContext()).cancelRequest(this);
        isAttachedToWindow = false;
        setImageBitmap(null);
        super.onDetachedFromWindow();
    }


    public void setImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            if (isAttachedToWindow) {
                //朋友圈加载图片
               // Glide.with(getContext()).load(url).into(this);
//                Glide.with(getContext())
//                        .load(url)
//                        .thumbnail(Glide.with(getContext()).load(R.mipmap.discover_dynamic_default))
//                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
//         .into(this);
            //Picasso.with(getContext()).load(url).placeholder(R.mipmap.discover_dynamic_default).into(this);
            }
        }
    }
}
