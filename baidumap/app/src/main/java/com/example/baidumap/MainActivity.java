package com.example.baidumap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private MapView mapView;
    // 地图相关
    private BaiduMap mBaiduMap;
    // UI相关
    private SeekBar mWidthBar;
    private SeekBar mColorBar;
    private SeekBar mFillAlphaBar;
    public static MainActivity mainActivity=null;
    private Polygon mPolygon;
    private Polygon mPolygonTwo;
    private int mStrokeWidth = 5;
    private int mColor = 180;
    private int mFillAlpha = 0;
    public LatLng latLng=new LatLng(30.709603,104.103591);
    private PanoramaView mPanoramaView;
    public double lat = 30.709805;
    public double lng = 104.103591;
    List<LatLng> latLngList1 = new ArrayList<LatLng>();
    List<LatLng> latLngList2 = new ArrayList<LatLng>();
    List<LatLng> latLngList3 = new ArrayList<LatLng>();
    List<LatLng> latLngList4 = new ArrayList<LatLng>();
    List<LatLng> latLngList5 = new ArrayList<LatLng>();
    List<LatLng> latLngList6 = new ArrayList<LatLng>();
    List<LatLng> latLngList7 = new ArrayList<LatLng>();
    List<LatLng> latLngList8 = new ArrayList<LatLng>();
    List<List> list=new ArrayList<>();
    boolean istrue=true;
    BitmapDescriptor bitmap;
    Marker marker;
    OverlayOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("百度onCreate");
        super.onCreate(savedInstanceState);
        this.mainActivity=this;
        try {
            SDKInitializer.initialize(getApplicationContext());
        }catch(Exception e){
                Log.e("Error", e.getMessage());
            }
        SDKInitializer.initialize(this);
        setContentView(R.layout.activity_main);
            mapView = findViewById(R.id.baiduMapView);
            mBaiduMap = mapView.getMap();
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
            UiSettings settings=mBaiduMap.getUiSettings();
            settings.setOverlookingGesturesEnabled(false);//屏蔽双指下拉时变成3D地图
            //settings.showZoomControls(false);
            mapView.showZoomControls(false);
            mapView.showScaleControl(false);
            mapView.setLogoPosition(LogoPosition.logoPostionRightBottom);
            //设置地图模式为卫星地图
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            BaiduMap.OnMapClickListener listener = new BaiduMap.OnMapClickListener() {
                /**
                 * 地图单击事件回调函数
                 *
                 * @param point 点击的地理坐标
                 */
                @Override
                public void onMapClick(LatLng point) {
                System.out.println("点击了地图"+point);
                    if(!false){
                        for(List<LatLng> la:list){
                           boolean is= PtInPolygon(point,la);
                           if(is){
                               System.out.println("是格子内！");
                               break;
                           }
                        }
                    }
                }

                /**
                 * 地图内 Poi 单击事件回调函数
                 *
                 * @param mapPoi 点击的 poi 信息
                 */
                @Override
                public void onMapPoiClick(MapPoi mapPoi) {
                    System.out.println("点击了地图poi信息:"+mapPoi);
                }
            };
            //设置地图单击事件监听
            mBaiduMap.setOnMapClickListener(listener);

            BaiduMap.OnMapStatusChangeListener listener3 = new BaiduMap.OnMapStatusChangeListener() {
                /**
                 * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
                 *
                 * @param status 地图状态改变开始时的地图状态
                 */
                @Override
                public void onMapStatusChangeStart(MapStatus status) {

                }

                /**
                 * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
                 *
                 * @param status 地图状态改变开始时的地图状态
                 *
                 * @param reason 地图状态改变的原因
                 */

                //用户手势触发导致的地图状态改变,比如双击、拖拽、滑动底图
                //int REASON_GESTURE = 1;
                //SDK导致的地图状态改变, 比如点击缩放控件、指南针图标
                //int REASON_API_ANIMATION = 2;
                //开发者调用,导致的地图状态改变
                //int REASON_DEVELOPER_ANIMATION = 3;
                @Override
                public void onMapStatusChangeStart(MapStatus status, int reason) {

                }

                /**
                 * 地图状态变化中
                 *
                 * @param status 当前地图状态
                 */
                @Override
                public void onMapStatusChange(MapStatus status) {
                    System.out.println("地图当前状态："+status);
                    float zoom = mBaiduMap.getMapStatus().zoom;
                    Integer i1=(int)(zoom*10);
                    System.out.println("缩放级别："+zoom);
                    if (zoom>=19.0f&&istrue){
                        System.out.println("加载网格："+i1);
                        for(int i=0;i<10;i++){
                            list.clear();
                            latLngList1.clear();
                            initPolygon();
                        //    drawPoiOnMap(i1);
                        }
                        for(int i=0;i<10;i++){
                            list.clear();
                            latLngList1.clear();
                            initPolygon1();
                               drawPoiOnMap(i1);
                        }
                        istrue=false;
                    }else if(!istrue&&zoom<19.0f){
//                        System.out.println("删除物格："+zoom);
//                        mBaiduMap.clear();
//                        marker.remove();
//                        istrue=true;
                    }
                }

                /**
                 * 地图状态改变结束
                 *
                 * @param status 地图状态改变结束后的地图状态
                 */
                @Override
                public void onMapStatusChangeFinish(MapStatus status) {
                    System.out.println("地图状态改变结束");
                }
            };
            //设置地图状态监听
            mBaiduMap.setOnMapStatusChangeListener(listener3);

        BaiduMap.OnMarkerClickListener listenera = new BaiduMap.OnMarkerClickListener() {
            /**
             * 地图 Marker 覆盖物点击事件监听函数
             * @param marker 被点击的 marker
             */
            public boolean onMarkerClick(Marker marker){
                System.out.println("点击了marker:"+marker.getPeriod());
                return true;
            }
        };
        //设置marker点击事件监听
        mBaiduMap.setOnMarkerClickListener(listenera);

            BaiduMap.OnMapLongClickListener listener1 = new BaiduMap.OnMapLongClickListener() {
                /**
                 * 地图长按事件监听回调函数
                 *
                 * @param point 长按的地理坐标
                 */
                @Override
                public void onMapLongClick(LatLng point) {
                System.out.println("长按地图时的经纬度："+point);
                    lat=point.latitude;
                    lng=point.longitude;
                    Intent intent=new Intent(MainActivity.this, wang.class);//跳转到主界面
                    startActivity(intent);
                }
            };
            mBaiduMap.setOnMapLongClickListener(listener1);
            System.out.println("手势操作地图Q" +mBaiduMap.getMapStatus().zoom);
        }

    /*
     **把自定义的布局文件转成Bitmap
     */
    private Bitmap changeView2Drawble(Integer integer) {
        View view = LayoutInflater.from(this).inflate(R.layout.yun, null);
//        //显示数字，如小区人数
//        TextView textView = view.findViewById(R.id.pop_num);
//        //显示文字，如小区名称
//        TextView pop_name = view.findViewById(R.id.pop_name);
//        textView.setText(num);
//        pop_name.setText(name);//文字过长可以做处理
        view.setDrawingCacheEnabled(true);
        view.measure(
                View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.UNSPECIFIED));
        view.layout(10, 10,
                integer,
                integer);
        view.buildDrawingCache();
        //获取到图片，这样就可以添加到Map上
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        return bitmap;
    }
    /**
     * @作者
     * @时间
     * @描述 绘制数据到地图上，Poi类是自己写的实体类
     */
    private void drawPoiOnMap(Integer integer) {

        bitmap = BitmapDescriptorFactory.fromBitmap(changeView2Drawble(integer));
        //设置marker
        options = new MarkerOptions()
                .position(new LatLng(30.709642, 104.103452))//设置位置
                .icon(bitmap)//设置图标样式
                .zIndex(9) // 设置marker所在层级
                .draggable(true)// 设置手势拖拽;
                .period(100); //可以作为一个标识
        //添加marker
        marker = (Marker) mBaiduMap.addOverlay(options);
        Bundle bundle = new Bundle();
        //marker中添加数据，点击的时候可以获取到做对应的处理。
        bundle.putString("name", "物格庄园");
        marker.setExtraInfo(bundle);
    }
    float j=30.709942f,w=104.103452f;
    LatLng latLngA,latLngB,latLngC,latLngD;
    public void initPolygon(){
             latLngA = new LatLng(j,w);
             latLngB = new LatLng(j,w+0.0001f);//104.103182,30.709817
             latLngC = new LatLng(j+0.0001f, w+0.0001f);//104.103313,30.710042
             latLngD = new LatLng(j+0.0001f, w);//104.103578,30.709987
             j+=0.0001f;
            latLngList1.add(latLngA);
            latLngList1.add(latLngB);
            latLngList1.add(latLngC);
            latLngList1.add(latLngD);
            list.add(latLngList1);

            for(List<LatLng> latLng:list){
                OverlayOptions ooPolygon = new PolygonOptions()
                        .points(latLng)// 设置多边形坐标点列表
                        .stroke(new Stroke(mStrokeWidth,  Color.argb(255, 255, 255, 255)))// 设置多边形边框信息
                        .fillColor(Color.argb(mFillAlpha, 255, 255, 0));// 设置多边形填充颜色
                // 添加覆盖物
                mPolygon = (Polygon) mBaiduMap.addOverlay(ooPolygon);
            }
        }
    float j1=30.709942f,w1=104.103552f;
    public void initPolygon1(){
        latLngA = new LatLng(j1,w1);
        latLngB = new LatLng(j1,w1+0.0001f);//104.103182,30.709817
        latLngC = new LatLng(j1+0.0001f, w1+0.0001f);//104.103313,30.710042
        latLngD = new LatLng(j1+0.0001f, w1);//104.103578,30.709987
        j1+=0.0001f;
        latLngList1.add(latLngA);
        latLngList1.add(latLngB);
        latLngList1.add(latLngC);
        latLngList1.add(latLngD);
        list.add(latLngList1);

        for(List<LatLng> latLng:list){
            OverlayOptions ooPolygon = new PolygonOptions()
                    .points(latLng)// 设置多边形坐标点列表
                    .stroke(new Stroke(mStrokeWidth,  Color.argb(255, 255, 255, 255)))// 设置多边形边框信息
                    .fillColor(Color.argb(mFillAlpha, 255, 255, 0));// 设置多边形填充颜色
            // 添加覆盖物
            mPolygon = (Polygon) mBaiduMap.addOverlay(ooPolygon);
        }
    }
    // 功能：判断点是否在多边形内
    // 方法：求解通过该点的水平线与多边形各边的交点
    // 结论：单边交点为奇数，成立!
    //参数：
    // POINT p   指定的某个点
    // LPPOINT ptPolygon 多边形的各个顶点坐标（首末点可以不一致）
    public  boolean PtInPolygon(LatLng point, List<LatLng> APoints) {
        int nCross = 0;
        for (int i = 0; i < APoints.size(); i++)   {
            LatLng p1 = APoints.get(i);
            LatLng p2 = APoints.get((i + 1) % APoints.size());
            // 求解 y=p.y 与 p1p2 的交点
            if ( p1.longitude == p2.longitude)      // p1p2 与 y=p0.y平行
                continue;
            if ( point.longitude <  Math.min(p1.longitude, p2.longitude))   // 交点在p1p2延长线上
                continue;
            if ( point.longitude >= Math.max(p1.longitude, p2.longitude))   // 交点在p1p2延长线上
                continue;
            // 求交点的 X 坐标 --------------------------------------------------------------
            double x = (double)(point.longitude - p1.longitude) * (double)(p2.latitude - p1.latitude) / (double)(p2.longitude - p1.longitude) + p1.latitude;
            if ( x > point.latitude )
                nCross++; // 只统计单边交点
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }

    @Override
    protected void onPause() {
        System.out.println("百度onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        System.out.println("百度onResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        System.out.println("百度onDestroy");
        if(mPanoramaView!=null){
            mPanoramaView.destroy();
        }
        super.onDestroy();
    }
}
