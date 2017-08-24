package kr.rrcoporation.rrfestival.festival.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import java.util.ArrayList;
import devlight.io.library.ntb.NavigationTabBar;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.callback.FragmentContainerBottomCallback;
import kr.rrcoporation.rrfestival.festival.fragment.FavFragment;
import kr.rrcoporation.rrfestival.festival.fragment.MapFragment;
import kr.rrcoporation.rrfestival.festival.fragment.RandomFingerFragment;
import kr.rrcoporation.rrfestival.festival.fragment.SettingFragment;
import kr.rrcoporation.rrfestival.festival.view.FreezingViewPager;

public class FragmentContainerActivity extends CommonFragmentActivity implements FragmentContainerBottomCallback {

    private boolean backFlag;
    private Handler backHandler;
    private int MAX_PAGE=4;
    private FreezingViewPager viewPager;
    private Fragment[] fragments = {new RandomFingerFragment(), new FavFragment(), new MapFragment(), new SettingFragment()};
    private int[] bottomBarImgs = {R.drawable.ic_second, R.drawable.ic_first, R.drawable.ic_fourth, R.drawable.ic_third};
    private String[] bottomBarStrs = {"Random", "Favorite", "Map", "Setting"};
    private NavigationTabBar navigationTabBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        backHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    backFlag = false;
                }
            }
        };
        initView();
    }

    private void initView() {
        ContainerAdapter adapter = new ContainerAdapter(getSupportFragmentManager());
        viewPager = (FreezingViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setPagingEnabled(false);

        MapFragment pf = new MapFragment();
        pf.setPopulationFragmentCallback(this);

        final String[] colors = getResources().getStringArray(R.array.default_preview);
        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();

        // .selectedIcon(getResources().getDrawable(R.drawable.ic_sixth))
        // .badgeTitle("NTB") // 알림 뱃지
        // under add setting
        for (int i = 0; i < bottomBarStrs.length; i++) {
            models.add(new NavigationTabBar.Model.Builder(ContextCompat.getDrawable(getApplicationContext(), bottomBarImgs[i]), Color.parseColor(colors[i]))
                    .title(bottomBarStrs[i]).badgeTitle(null).build());
        }
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
    }

    @Override
    public void SetOnCurrentFragment(int status) {
        viewPager.setCurrentItem(status);
    }

    @Override
    public void setVisibilityBottomTabBar(boolean status) {}

    @Override
    public void OnBottomCallBback(int status) {
        navigationTabBar.setViewPager(viewPager, status);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            if (!backFlag) {
                Toast.makeText(this, R.string.exit_text, Toast.LENGTH_SHORT).show();
                backFlag = true;
                backHandler.sendEmptyMessageDelayed(0, 2500);
            } else {
                super.onBackPressed();
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        }
    }

    private class ContainerAdapter extends FragmentPagerAdapter {

        public ContainerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < 0 || MAX_PAGE <= position) {
                return null;
            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }
}