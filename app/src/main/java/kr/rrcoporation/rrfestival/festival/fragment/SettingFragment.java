package kr.rrcoporation.rrfestival.festival.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.util.Util;

public class SettingFragment extends CommonFragment{

    private RelativeLayout rootLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_setting, null);
        initialize();
        return rootLayout;
    }

    private void initialize() {
        TextView versionTextView = (TextView) rootLayout.findViewById(R.id.textview_version);
        versionTextView.setText(String.format(getString(R.string.version_name), Util.getAppVersion()));
    }
}
