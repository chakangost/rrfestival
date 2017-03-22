package kr.rrcoporation.rrfestival.festival.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import net.daum.mf.map.api.MapView;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.callback.FragmentContainerBottomCallback;

public class MapFragment extends CommonFragment {

    private static FragmentContainerBottomCallback fragmentContainerBottomCallback;

    public void setPopulationFragmentCallback(FragmentContainerBottomCallback fragmentContainerBottomCallback) {
        MapFragment.fragmentContainerBottomCallback = fragmentContainerBottomCallback;
    }

    private LinearLayout rootLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_map, null);
        MapView mapView = new MapView(getActivity());
        mapView.setDaumMapApiKey(getString(R.string.daum_api_key));
        ViewGroup mapViewContainer = (ViewGroup) rootLayout.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        return rootLayout;
    }

}
