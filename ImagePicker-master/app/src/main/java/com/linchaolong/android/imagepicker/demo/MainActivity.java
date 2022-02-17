package com.linchaolong.android.imagepicker.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private ImagePicker imagePicker = new ImagePicker();
  private SimpleDraweeView draweeView;
  private ImageView asfsada;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // 设置标题
    imagePicker.setTitle("设置头像");
    // 设置是否裁剪图片
    imagePicker.setCropImage(true);

    findViewById(R.id.fragmentTest).setOnClickListener(this);
    draweeView = findViewById(R.id.draweeView);
    asfsada=findViewById(R.id.asfsada);
    draweeView.setOnClickListener(this);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    imagePicker.onActivityResult(this, requestCode, resultCode, data);
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
  }

  private void startChooser() {
    // 启动图片选择器
    imagePicker.startChooser(this, new ImagePicker.Callback() {
      // 选择图片回调
      @Override public void onPickImage(Uri imageUri) {

      }

      // 裁剪图片回调
      @Override public void onCropImage(Uri imageUri) {
        draweeView.setImageURI(imageUri);
        draweeView.getHierarchy().setRoundingParams(RoundingParams.asCircle());
        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getEncodedPath());//从路径加载出图片bitmap
      //  bitmap = rotateBimap(MainActivity.this, 0, bitmap);//旋转图片-90°
        asfsada.setImageBitmap(bitmap);//ImageView显示图片
      }
/**
        * 旋转Bitmap图片
*
        * @param context
* @param degree 旋转的角度
* @param srcBitmap 需要旋转的图片的Bitmap
* @return
        */
      private Bitmap rotateBimap(Context context, float degree, Bitmap srcBitmap) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight()
                , matrix, true);
        return bitmap;
      }
      // 自定义裁剪配置
      @Override public void cropConfig(CropImage.ActivityBuilder builder) {
        builder
            // 是否启动多点触摸
            .setMultiTouchEnabled(false)
            // 设置网格显示模式
            .setGuidelines(CropImageView.Guidelines.OFF)
            // 圆形/矩形
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            // 调整裁剪后的图片最终大小
            .setRequestedSize(960, 540)
            // 宽高比
            .setAspectRatio(1, 1);
      }

      // 用户拒绝授权回调
      @Override public void onPermissionDenied(int requestCode, String[] permissions,
          int[] grantResults) {
      }
    });
  }

  @Override public void onClick(View v) {
    if (v.getId() == R.id.draweeView) {
      startChooser();
    } else if (v.getId() == R.id.fragmentTest) {
      startActivity(new Intent(this, FragmentTestActivity.class));
    }
  }
}

