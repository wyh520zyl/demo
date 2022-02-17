package com.example.myapplication;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private List<List<Image>> imagesList;

    private String[][] images=new String[][]{{
             "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa3.att.hudong.com%2F35%2F34%2F19300001295750130986345801104.jpg&refer=http%3A%2F%2Fa3.att.hudong.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614763799&t=4631861f541b5ca63f95a606ab469d3f","640","960"}
            ,{"https://ss0.baidu.com/7Po3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3de9ba8ee1194eef01f3a2979a8.jpg","250","250"}
            ,{"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi2.w.yun.hjfile.cn%2Fdoc%2F201303%2Fd5547c74-d9ad-4625-bd93-41c2817f1dff_03.jpg&refer=http%3A%2F%2Fi2.w.yun.hjfile.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614763799&t=295b69bdb480758292f68e9684e586f9","250","250"}
            ,{"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fs9.sinaimg.cn%2Fbmiddle%2F5ceba31bg5d6503750788&refer=http%3A%2F%2Fs9.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614763799&t=8eb9ac3bd848fcac428c512772fffaf3","250","250"}
            ,{"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.aiimg.com%2Fuploads%2Fuserup%2F0909%2F2Z64022L38.jpg&refer=http%3A%2F%2Fimg.aiimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614763799&t=f9ff97600749cbdb10eff5e01f1c53ff","250","250"}
            ,{"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa4.att.hudong.com%2F63%2F70%2F06300000046969120433706514748.jpg&refer=http%3A%2F%2Fa4.att.hudong.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614763799&t=3f946235067b2388f76409072b451ee0","250","250"}
            ,{"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa3.att.hudong.com%2F02%2F38%2F01300000237560123245382609951.jpg&refer=http%3A%2F%2Fa3.att.hudong.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614763799&t=47d59c3aac571b05d661e8ec22c4f9ca","250","250"}
            ,{"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa1.att.hudong.com%2F23%2F44%2F01300000165476121464441385347.jpg&refer=http%3A%2F%2Fa1.att.hudong.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614763799&t=8e6279ea5dfdfcdf0c764919682c9b07","250","250"}
            ,{"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa2.att.hudong.com%2F42%2F31%2F01300001320894132989315766618.jpg&refer=http%3A%2F%2Fa2.att.hudong.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614763799&t=3430843f360d50b0fc76783945175c30","1280","800"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView= (ListView) findViewById(R.id.lv_main);
        initData();
        listView.setAdapter(new MainAdapter(MainActivity.this,imagesList));

    }

    private void initData() {
        imagesList=new ArrayList<>();
       //这里单独添加一条单条的测试数据，用来测试单张的时候横竖图片的效果
        ArrayList<Image> singleList=new ArrayList<>();
        singleList.add(new Image(images[8][0], Integer.parseInt(images[8][1]), Integer.parseInt(images[8][2])));
        imagesList.add(singleList);
        //从一到9生成9条朋友圈内容，分别是1~9张图片
        for(int i=0;i<9;i++){
            ArrayList<Image> itemList=new ArrayList<>();
             for(int j=0;j<=i;j++){
                 itemList.add(new Image(images[j][0], Integer.parseInt(images[j][1]), Integer.parseInt(images[j][2])));
             }
            imagesList.add(itemList);
        }
    }

}
