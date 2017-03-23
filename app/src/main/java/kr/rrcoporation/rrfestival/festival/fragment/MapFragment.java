package kr.rrcoporation.rrfestival.festival.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.callback.FragmentContainerBottomCallback;
import kr.rrcoporation.rrfestival.festival.model.FestivalResult;
import kr.rrcoporation.rrfestival.festival.transaction.ApiManager;
import kr.rrcoporation.rrfestival.festival.transaction.ApiService;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragment extends CommonFragment implements MapView.MapViewEventListener, MapView.POIItemEventListener{

    private static FragmentContainerBottomCallback fragmentContainerBottomCallback;
    private String serviceKey = "n4HqoC9EFsrq1stLyXelZtz4GPjTgjinWix/IT93c9Vr3bP+WA+zgOirr0AmIaGnSGkCiWgHV0YajENvv9vY6w==";

    public void setPopulationFragmentCallback(FragmentContainerBottomCallback fragmentContainerBottomCallback) {
        MapFragment.fragmentContainerBottomCallback = fragmentContainerBottomCallback;
    }

    private LinearLayout rootLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_map, null);
        initMapSetting();
        fetchFestivalData();
        return rootLayout;
    }

    private void initMapSetting() {
        MapView mapView = new MapView(getActivity());
        mapView.setDaumMapApiKey(getString(R.string.daum_api_key));
        ViewGroup mapViewContainer = (ViewGroup) rootLayout.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
    }

    private void fetchFestivalData() {
        ApiService apiService = ApiManager.apiService;
        Observable<FestivalResult> festivalData = apiService.fetchFestivalData(serviceKey, "1", "ETC", "AppTesting", "json", "1000", "A02");
        festivalData.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FestivalResult>() {
                    @Override
                    public void onNext(FestivalResult festivalResult) {
                        Gson gson = new Gson();
                        Toast.makeText(getActivity(), "result : " + gson.toJson(festivalResult.getResponse().getBody().getItems().getItem()[0]), Toast.LENGTH_SHORT).show();
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
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
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

    @Override
    public void onMapViewInitialized(MapView mapView) {
    }
}
