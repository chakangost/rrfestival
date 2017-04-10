package kr.rrcoporation.rrfestival.festival.fragment;

import android.Manifest;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
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
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.callback.FragmentContainerBottomCallback;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;
import kr.rrcoporation.rrfestival.festival.model.FestivalResult;
import kr.rrcoporation.rrfestival.festival.transaction.ApiManager;
import kr.rrcoporation.rrfestival.festival.transaction.ApiService;
import kr.rrcoporation.rrfestival.festival.util.Util;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragment extends CommonFragment implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener{

    private double lat;
    private double lng;
    private MapPoint mapPoint;
    private static FragmentContainerBottomCallback fragmentContainerBottomCallback;
    private String serviceKey = "n4HqoC9EFsrq1stLyXelZtz4GPjTgjinWix/IT93c9Vr3bP+WA+zgOirr0AmIaGnSGkCiWgHV0YajENvv9vY6w==";
    private MapView mapView;
    private static BodyItem[] bodyItems;
    private List<BodyItem> currentDrawerItems = new ArrayList<>();
    private Gson gson = new Gson();

    public void setPopulationFragmentCallback(FragmentContainerBottomCallback fragmentContainerBottomCallback) {
        MapFragment.fragmentContainerBottomCallback = fragmentContainerBottomCallback;
    }

    private LinearLayout rootLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_map, null);
        initPermission();
        return rootLayout;
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

        new TedPermission(getActivity())
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("지도 서비스를 사용하기 위해서는 위치 접근 권한이 필요해요")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
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
        Log.i("eunho", "initMapSetting");
    }

    private void initView(BodyItem[] items, MapView mapView) {
        bodyItems = items;
        SystemClock.sleep(1000);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
//        mapView.setMapCenterPoint(mapPoint, true);
        Log.i("eunho", "initView");
//        currentScreenMarker();

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

    private void currentScreenMarker() {
        MapPointBounds visibleBounds = mapView.getMapPointBounds();
        currentDrawerItems = filterOnlyVisibleFestival(visibleBounds, bodyItems);
        MapPOIItem marker;
        for (BodyItem item : currentDrawerItems) {
            marker = new MapPOIItem();
            marker.setItemName(item.getTitle());
            marker.setTag(item.getContentid());
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(item.getMapy(), item.getMapx()));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
            mapView.addPOIItem(marker);
        }
    }

    private void fetchFestivalData(final MapView mapView) {
        ApiService apiService = ApiManager.apiService;
        final Observable<FestivalResult> festivalData = apiService.fetchFestivalData(serviceKey, "", "ETC", "AppTesting", "json", "2000", "A02", "A0207");
        festivalData.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FestivalResult>() {
                    @Override
                    public void onNext(FestivalResult festivalResult) {
                        Util.setSharedPreference(getContext(), "FESTIVAL_LIST", gson.toJson(festivalResult));
                        initView(festivalResult.getResponse().getBody().getItems().getItem(), mapView);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Log.i("eunho", "onCurrentLocationUpdateCancelled");
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Log.i("eunho", "onCurrentLocationUpdateFailed");
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
        Log.i("eunho", "onCurrentLocationDeviceHeadingUpdate");
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        Log.i("eunho", "onCurrentLocationUpdate");
        lat = mapPoint.getMapPointGeoCoord().latitude;
        lng = mapPoint.getMapPointGeoCoord().longitude;
        this.mapPoint = mapPoint;
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
        Log.i("eunho", "onDraggablePOIItemMoved");
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        Log.i("eunho", "onCalloutBalloonOfPOIItemTouched");
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        Log.i("eunho", "onCalloutBalloonOfPOIItemTouched");
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        Log.i("eunho", "onPOIItemSelected");
        Toast.makeText(getActivity(), "" + mapPOIItem.getTag(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        Log.i("eunho", "onMapViewMoveFinished");
//        currentScreenMarker();
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }

    private List<BodyItem> filterOnlyVisibleFestival(MapPointBounds visibleBounds, BodyItem[] items) {
        List<BodyItem> festivalItems = new LinkedList<>(Arrays.asList(items));
        List<BodyItem> visiblePoints = new ArrayList<>();
        MapPoint latLng;
        for (BodyItem item : festivalItems) {
            latLng = MapPoint.mapPointWithGeoCoord(item.getMapy(), item.getMapx());
            if (!visibleBounds.contains(latLng)) {
                continue;
            }
            visiblePoints.add(item);
        }
        return visiblePoints;
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        Log.i("eunho", "onMapViewDragEnded");
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        Log.i("eunho", "onMapViewDragStarted");
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        Log.i("eunho", "onMapViewLongPressed");
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        Log.i("eunho", "onMapViewDoubleTapped");
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        Log.i("eunho", "onMapViewSingleTapped");
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        Log.i("eunho", "onMapViewZoomLevelChanged");
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        Log.i("eunho", "onMapViewCenterPointMoved");
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        Log.i("eunho", "onMapViewInitialized");
        if (Util.getSharedPreference(getContext(), "FESTIVAL_LIST").equals("")) {
            fetchFestivalData(mapView);
        } else {
            String festivalGsonStr = Util.getSharedPreference(getContext(), "FESTIVAL_LIST");
            FestivalResult festivalItem = gson.fromJson(festivalGsonStr, FestivalResult.class);
            initView(festivalItem.getResponse().getBody().getItems().getItem(), mapView);
        }
    }
}
