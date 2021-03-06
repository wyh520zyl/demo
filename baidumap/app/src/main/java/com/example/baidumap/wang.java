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


    //?????????BMapManager
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
                jingdu.setText("??????:"+longitude);
                weidu.setText("??????:"+latitude);
            }
        });
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(latitude, longitude)));

    }
    private void getView() {
        mPanoramaView = (PanoramaView) findViewById(R.id.panorama);


        //????????????????????????????????????????????????????????????
        mPanoramaView.setShowTopoLink(true);

        //???????????????????????????
        mPanoramaView.setPanoramaPitch(90);

        //?????????????????????????????????
        //??????????????????????????????????????????[-15, 90], ?????????[-25, 90],
        //90????????????????????????0???????????????
        mPanoramaView.getPanoramaPitch();

        //???????????????????????????
        mPanoramaView.setPanoramaHeading(60);

        //?????????????????????????????????
        mPanoramaView.getPanoramaHeading();

        //??????????????????????????????
        //level??????1-5???
        mPanoramaView.setPanoramaLevel(1);

        //????????????????????????????????????
        mPanoramaView.getPanoramaLevel();

        //???????????????????????????setPanorama??????????????????????????????????????????
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

    // ??????????????????????????????????????????????????????????????????
//	private void testPanoramaRequest() {
//		new Thread() {
//			@Override
//			public void run() {
//				PanoramaRequest panoramaRequest = PanoramaRequest
//						.getInstance(context);
//				 // ????????????????????????????????????????????????????????????????????????????????????????????????PID??????????????????
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
