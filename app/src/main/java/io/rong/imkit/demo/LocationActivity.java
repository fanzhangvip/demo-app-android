package io.rong.imkit.demo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;

import io.rong.message.LocationMessage;

/**
 * Created by DragonJ on 14/11/21.
 */

public class LocationActivity extends MapActivity implements TencentLocationListener {

    MapView mMapView;
    Button mButton = null;
    LocationMessage mMsg;
    Handler mHandler;

    @Override
    /**
     *显示地图，启用内置缩放控件，并用MapController控制地图的中心点及Zoom级别
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poisearchdemo);

        if(getIntent().hasExtra("location")){
            mMsg = getIntent().getParcelableExtra("location");
        }


        mMapView = (MapView) findViewById(android.R.id.widget_frame);
        mHandler = new Handler();
        mButton = (Button) this.findViewById(android.R.id.button1);
        if(mMsg!=null)
            mButton.setVisibility(View.GONE);

        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mMsg != null)
                    DemoContext.getInstance().getLastLocationCallback().onSuccess(mMsg);
                else
                    DemoContext.getInstance().getLastLocationCallback().onFailure("定位失败");

                DemoContext.getInstance().setLastLocationCallback(null);

                finish();
            }
        });

        mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件

        if(mMsg==null){
        GeoPoint point = new GeoPoint((int) (39.90923 * 1E6), (int) (116.397428 * 1E6)); // 用给定的经纬度构造一个GeoPoint，单位是微度

        mMapView.getController().setCenter(point);
        mMapView.getController().setZoom(18);
        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager.getInstance(this).requestLocationUpdates(request,this);}
        else {
            GeoPoint point = new GeoPoint((int) (mMsg.getLat() * 1E6), (int) (mMsg.getLng() * 1E6)); // 用给定的经纬度构造一个GeoPoint，单位是微度

            mMapView.getController().setCenter(point);
            mMapView.getController().setZoom(18);
        }

    }


    @Override
    public void onLocationChanged(final TencentLocation tencentLocation, int code, String s) {
        if (TencentLocation.ERROR_OK == code) {
            Toast.makeText(this, "定位成功", Toast.LENGTH_SHORT).show();

            Uri uri = Uri.parse("http://apis.map.qq.com/ws/staticmap/v2").buildUpon()
                    .appendQueryParameter("size", "240*240")
                    .appendQueryParameter("key", "7JYBZ-4Y3W4-JMUU7-DJHQU-NOYH7-SRBBU")
                    .appendQueryParameter("zoom", "14")
                    .appendQueryParameter("center", tencentLocation.getLatitude() + "," + tencentLocation.getLongitude()).build();

            mMsg = LocationMessage.obtain(tencentLocation.getLatitude(), tencentLocation.getLongitude(), tencentLocation.getProvince() + tencentLocation.getCity() + tencentLocation.getAddress(), uri);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    GeoPoint point = new GeoPoint((int) (tencentLocation.getLatitude() * 1E6), (int) (tencentLocation.getLongitude() * 1E6)); // 用给定的经纬度构造一个GeoPoint，单位是微度
                    mMapView.getController().setCenter(point);
                }
            });

        } else {
            Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s2) {

    }

    @Override
    protected void onDestroy() {

        if(DemoContext.getInstance().getLastLocationCallback() != null)
            DemoContext.getInstance().getLastLocationCallback().onFailure("失败");

        DemoContext.getInstance().setLastLocationCallback(null);
        TencentLocationManager.getInstance(this).removeUpdates(this);

        super.onDestroy();
    }
}

