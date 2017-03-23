package kr.rrcoporation.rrfestival.festival.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import kr.rrcoporation.rrfestival.festival.util.helper.FragmentHelper;

public class FragmentBase extends Fragment {
    protected FragmentHelper mHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new FragmentHelper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHelper.dismissDialog();
    }

    public void finish(int resultCode, Intent resultIntent) {
        mHelper.finish(resultCode, resultIntent);
    }
}
