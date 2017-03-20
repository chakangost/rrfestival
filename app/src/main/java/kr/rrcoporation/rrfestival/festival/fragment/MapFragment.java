package kr.rrcoporation.rrfestival.festival.fragment;

import kr.rrcoporation.rrfestival.festival.callback.FragmentContainerBottomCallback;

public class MapFragment extends CommonFragment {

    private static FragmentContainerBottomCallback fragmentContainerBottomCallback;

    public void setPopulationFragmentCallback(FragmentContainerBottomCallback fragmentContainerBottomCallback) {
        MapFragment.fragmentContainerBottomCallback = fragmentContainerBottomCallback;
    }

}
