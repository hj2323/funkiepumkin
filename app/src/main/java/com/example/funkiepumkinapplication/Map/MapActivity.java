package com.example.funkiepumkinapplication.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.funkiepumkinapplication.MainActivity;
import com.example.funkiepumkinapplication.Map.DRvIntetface.LoadMore;
import com.example.funkiepumkinapplication.R;
import com.example.funkiepumkinapplication.event.EventActivity;
import com.example.funkiepumkinapplication.member.LoginActivity;
import com.example.funkiepumkinapplication.refrige.Refrige;
import com.example.funkiepumkinapplication.refrige.RefrigeActivity;
import com.example.funkiepumkinapplication.refrige.RefrigeAdapter;
import com.example.funkiepumkinapplication.stamp.StampActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private RecyclerView recyclerView;
    //????????????????????? ????????? ????????????????????????
    //private MapStaticRvAdapter mapStaticRvAdapter;

    //????????? ???????????? ????????? ?????? ????????????????????????
    ArrayList<MapDynamicRvModel> items = new ArrayList();
    MapDynamicRvAdapter mapDynamicRvAdapter;
    RecyclerView drv;

    static RequestQueue requestQueue;

    //????????????
    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private List<Marker> markerList = new ArrayList<Marker>();

    //?????????
    private BottomNavigationView bottomNavigationView;
    //????????? ???????????????
    FloatingActionButton fab;
    int memberId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //???????????????
        Intent memberIntent = getIntent();
        memberId = memberIntent.getIntExtra("memberId",0);

        fab=findViewById(R.id.fab);
        if(memberId>0) {//????????? ????????????, memberId??? ???????????? ???????????????
            fab.setVisibility(View.GONE);
        }else{//???????????? ????????????, memberId??? ???????????? ???????????????

            fab.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
        //?????????
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_stamp:
                        Intent stampIntent = new Intent(getApplicationContext(), StampActivity.class);
                        stampIntent.putExtra("memberId",memberId);
                        startActivity(stampIntent);
                        finish();
                        break;
                    case R.id.action_home:
                        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        mainIntent.putExtra("memberId",memberId);
                        startActivity(mainIntent);
                        finish();
                        break;
                    case R.id.action_refridge:
                        Intent refrigeIntent = new Intent(getApplicationContext(), RefrigeActivity.class);
                        refrigeIntent.putExtra("memberId",memberId);
                        startActivity(refrigeIntent);
                        finish();
                        break;
                    case R.id.action_event:
                        Intent eventIntent = new Intent(getApplicationContext(), EventActivity.class);
                        eventIntent.putExtra("memberId",memberId);
                        startActivity(eventIntent);
                        finish();
                        break;
                }
            }
        });


        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



//         Static RecyclerView ????????????
//        ArrayList<MapStaticRvModel> item = new ArrayList<>();
//        item.add(new MapStaticRvModel(R.drawable.fp_mainlogo, "??????"));
//        item.add(new MapStaticRvModel(R.drawable.fp_mainlogo, "??????"));
//        item.add(new MapStaticRvModel(R.drawable.fp_mainlogo, "??????"));
//        item.add(new MapStaticRvModel(R.drawable.fp_mainlogo, "??????"));
//        item.add(new MapStaticRvModel(R.drawable.fp_mainlogo, "??????"));
//
//        recyclerView = findViewById(R.id.rv_1);
//        mapStaticRvAdapter = new MapStaticRvAdapter(item);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(mapStaticRvAdapter);

//        items.add(new MapDynamicRvModel("???????????? ????????????", "", "","",""));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ????????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ????????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ????????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ????????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ????????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));
//        items.add(new MapDynamicRvModel("???????????? ?????????"));


        drv = findViewById(R.id.rv_2);
        drv.setLayoutManager(new LinearLayoutManager(this));

//        mapDynamicRvAdapter = new MapDynamicRvAdapter(drv, this, items);
//        drv.setAdapter(mapDynamicRvAdapter);

        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(getApplicationContext());
        }

        MapListRequest();

        //?????? ?????????????????? ?????? ????????? ????????????
//        mapDynamicRvAdapter.setLoadMore(new LoadMore() {
//            @Override
//            public void onLoadMore() {
//                if(items.size()<=10){
//                    items.add(null);
//                    mapDynamicRvAdapter.notifyItemInserted(items.size()-1);
//                    new Handler().postDelayed(new Runnable(){
//
//                        @Override
//                        public void run() {
//                            items.remove(items.size()-1);
//                            mapDynamicRvAdapter.notifyItemRemoved(items.size());
//
//                            int index = items.size();
//                            int end = index+10;
//                            for(int i=index; i<end; i++){
//                                String name = UUID.randomUUID().toString();
//                                MapDynamicRvModel item = new MapDynamicRvModel(name);
//                                items.add(item);
//                            }
//                            mapDynamicRvAdapter.notifyDataSetChanged();
//                            mapDynamicRvAdapter.setLoaded();
//                        }
//                    }, 4000);
//
//                }else
//                    Toast.makeText(MapActivity.this, "???????????? ???????????????.", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    public void MapListRequest(){
        String url="http://192.168.75.44:8899/shop/shopListBody";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) { //???????????? ??????
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("??????", error.getMessage());
                    }
                }
        ){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void processResponse(String response){
        Gson gson = new Gson();
        Shops[] shops = gson.fromJson(response, Shops[].class);

        for(int i=0; i<shops.length; i++){
            items.add(new MapDynamicRvModel(shops[i].getShopName(),shops[i].getShopAddress(),
                    shops[i].getShopTel()));
            mapDynamicRvAdapter = new MapDynamicRvAdapter(drv, this, items);
            drv.setAdapter(mapDynamicRvAdapter);

            //????????? ??? ??????
            Marker marker = new Marker();
            marker.setPosition(new LatLng(Double.parseDouble(shops[i].getShopLat()), Double.parseDouble(shops[i].getShopLng())));
            marker.setIcon(OverlayImage.fromResource(R.drawable.ic_baseline_location_red));
            marker.setAnchor(new PointF(0.5f, 1.0f));
            marker.setMap(naverMap);
            markerList.add(marker);
        }
    }

    //?????? ????????? ???????????? ??????
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

       locationSource = new FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

//        naverMap.addOnCameraChangeListener((NaverMap.OnCameraChangeListener) this);
//        naverMap.addOnCameraIdleListener((NaverMap.OnCameraIdleListener) this);
//
//        LatLng mapCenter = naverMap.getCameraPosition().target;
//        //fetchStoreSale(mapCenter.latitude, mapCenter.longitude);
    }



    //???????????? ?????? ?????? ?????? ???????????? ??????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case ACCESS_LOCATION_PERMISSION_REQUEST_CODE:
                locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }
}