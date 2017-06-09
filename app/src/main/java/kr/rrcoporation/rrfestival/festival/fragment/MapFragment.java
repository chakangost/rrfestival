package kr.rrcoporation.rrfestival.festival.fragment;

import android.Manifest;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.callback.FragmentContainerBottomCallback;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;
import kr.rrcoporation.rrfestival.festival.model.FestivalResult;
import kr.rrcoporation.rrfestival.festival.store.MyFestivalStore;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import kr.rrcoporation.rrfestival.festival.util.Util;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MapFragment extends CommonFragment implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener, View.OnClickListener {

    private static FragmentContainerBottomCallback fragmentContainerBottomCallback;
    private static MapView                         mapView;
    private static List<BodyItem>                  bodyItems;
    private        Subscription subscription;
    private static MapFragment  mapFragment;
    private Gson gson = new Gson();

    public void setPopulationFragmentCallback(FragmentContainerBottomCallback fragmentContainerBottomCallback) {
        MapFragment.fragmentContainerBottomCallback = fragmentContainerBottomCallback;
    }

    private LinearLayout rootLayout;

    public static void newInstance(double lat, double lng, String flag) {
        mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putDouble("mapY", lat);
        args.putDouble("mapX", lng);
        args.putString("flag", flag);
        mapFragment.setArguments(args);

        if (fragmentContainerBottomCallback != null) {
            fragmentContainerBottomCallback.OnBottomCallBback(2);
            fragmentContainerBottomCallback.SetOnCurrentFragment(2);
        }

        if (mapFragment !=null && mapFragment.getArguments() != null) {
            try {
                if (mapFragment.getArguments().getString("flag").equals("FROM_FINGER")) {
                    mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord((double)mapFragment.getArguments().get("mapY"),
                            (double)mapFragment.getArguments().get("mapX")), 3, true);
                }
                mapFragment.setArguments(null);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_map, null);
        initView();
        observeFestivalStore();
        initPermission();
        return rootLayout;
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        if (Util.getSharedPreference(getContext(), "FESTIVAL_LIST").equals("")) {
            ApiAction.getInstance().fetchFestivals();
        } else {
            String festivalGsonStr = Util.getSharedPreference(getContext(), "FESTIVAL_LIST");
            FestivalResult festivalItem = gson.fromJson(festivalGsonStr, FestivalResult.class);
            initViewData(new LinkedList<>(Arrays.asList(festivalItem.getResponse().getBody().getItems().getItem())), mapView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void observeFestivalStore() {
        subscription = MyFestivalStore.getInstance().observe().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Void aVoid) {
                List<BodyItem> festivals = MyFestivalStore.getInstance().getFestivals();
                FestivalResult festivalResult = MyFestivalStore.getInstance().getFestivalResult();
                Util.setSharedPreference(getContext(), "FESTIVAL_LIST", gson.toJson(festivalResult));
                initViewData(festivals, mapView);
            }
        });
    }

    private void initView() {
        rootLayout.findViewById(R.id.zoom_in_btn).setOnClickListener(this);
        rootLayout.findViewById(R.id.zoom_out_btn).setOnClickListener(this);
    }

    private void initPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionGranted() {
                initMapSetting();
            }
        };

        new TedPermission(getActivity()).setPermissionListener(permissionlistener).setRationaleMessage("지도 서비스를 사용하기 위해서는 위치 접근 권한이 필요해요").setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.").setPermissions(Manifest.permission.ACCESS_FINE_LOCATION).check();
    }

    private void initMapSetting() {
        mapView = new MapView(getActivity());
        mapView.setDaumMapApiKey(getString(R.string.daum_api_key));
        ViewGroup mapViewContainer = (ViewGroup) rootLayout.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setShowCurrentLocationMarker(true);
        mapView.MAX_ZOOM_LEVEL = -5.0F;
    }

    private void initViewData(List<BodyItem> items, MapView mapView) {
        bodyItems = items;
        SystemClock.sleep(1000);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        MapPOIItem marker;
        for (BodyItem item : bodyItems) {
            marker = new MapPOIItem();
            marker.setItemName(item.getTitle());
            marker.setTag(item.getContentid());
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(item.getMapy(), item.getMapx()));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
            mapView.addPOIItem(marker);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zoom_in_btn:
                mapView.setZoomLevel(mapView.getZoomLevel() - 1, true);
                break;
            case R.id.zoom_out_btn:
                mapView.setZoomLevel(mapView.getZoomLevel() + 1, true);
                break;
        }
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    }
}
