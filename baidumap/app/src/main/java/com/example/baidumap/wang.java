package com.example.baidumap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import static com.example.baidumap.MainActivity.mainActivity;

public class wang extends Activity {
    private PanoramaView mPanoramaView;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    public TextView dizhi,jingdu,weidu;
    public static wang wang;
    public LatLng point;
    public ImageButton soushuo;
    public RelativeLayout quanjing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wang);
        wang=this;
        mapView=findViewById(R.id.baiduMap);
        mBaiduMap=mapView.getMap();
        UiSettings settings1=mBaiduMap.getUiSettings();
        settings1.setAllGesturesEnabled(false);
        point=new LatLng(mainActivity.lat,mainActivity.lng);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
        dizhi=findViewById(R.id.dizhi);
        quanjing=findViewById(R.id.quanjing);
        jingdu=findViewById(R.id.jingdu);
        weidu=findViewById(R.id.weidu);
        soushuo= findViewById(R.id.soushuo);
        initBMapManager();
        getView();
        site(mainActivity.lat,mainActivity.lng);
        soushuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //初始化BMapManager
    private void initBMapManager() {
        PanorApplication app = (PanorApplication) this.getApplication();
        if (app.getmBMapManager() == null) {
            app.setmBMapManager(new BMapManager(app));
            app.getmBMapManager().init(new PanorApplication.MyGeneralListener());
        }

    }
    private void site(double latitude,double longitude){
        GeoCoder mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                dizhi.setText(reverseGeoCodeResult.getAddress());
                jingdu.setText("经度:"+longitude);
                weidu.setText("纬度:"+latitude);
            }
        });
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(latitude, longitude)));

    }
    private void getView() {
        mPanoramaView = (PanoramaView) findViewById(R.id.panorama);


        //是否显示邻接街景箭头（有邻接全景的时候）
        mPanoramaView.setShowTopoLink(true);

        //设置全景图的俯仰角
        mPanoramaView.setPanoramaPitch(90);

        //获取当前全景图的俯仰角
        //更新俯仰角的取值范围：室外景[-15, 90], 室内景[-25, 90],
        //90为垂直朝上方向，0为水平方向
        mPanoramaView.getPanoramaPitch();

        //设置全景图的偏航角
        mPanoramaView.setPanoramaHeading(60);

        //获取当前全景图的偏航角
        mPanoramaView.getPanoramaHeading();

        //设置全景图的缩放级别
        //level分为1-5级
        mPanoramaView.setPanoramaLevel(1);

        //获取当前全景图的缩放级别
        mPanoramaView.getPanoramaLevel();

        //全景的事件监听要在setPanorama之前使用，否者可能会引发异常
        mPanoramaView.setPanoramaViewListener(new PanoramaViewListener() {

            @Override
            public void onMoveStart() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMoveEnd() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMessage(String arg0, int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadPanoramaError(String arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadPanoramaEnd(String arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadPanoramaBegin() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onDescriptionLoadEnd(String arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onCustomMarkerClick(String arg0) {
                // TODO Auto-generated method stub

            }
        });
        mPanoramaView
                .setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
        mPanoramaView.setPanorama(mainActivity.lng, mainActivity.lat, PanoramaView.COORDTYPE_BD09LL);
    }

    // 测试获取内景的相册描述信息和服务推荐描述信息
//	private void testPanoramaRequest() {
//		new Thread() {
//			@Override
//			public void run() {
//				PanoramaRequest panoramaRequest = PanoramaRequest
//						.getInstance(context);
//				 // 通过百度经纬度坐标获取当前位置相关全景信息，包括是否有外景，外景PID，外景名称等
//				BaiduPanoData mPanDataWithLaton = panoramaRequest
//						.getPanoramaInfoByLatLon(lng, lat);
//
//			}
//		}.start();
//	}

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        mPanoramaView.destroy();
        super.onDestroy();
    }
}
