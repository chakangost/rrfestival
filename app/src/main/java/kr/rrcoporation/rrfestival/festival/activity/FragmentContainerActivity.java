package kr.rrcoporation.rrfestival.festival.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
    private Fragment[] fragments = {new MapFragment(), new FavFragment(), new RandomFingerFragment(), new SettingFragment()};
    private int[] bottomBarImgs = {R.drawable.tab_map_on, R.drawable.tab_like_on, R.drawable.tab_popu_on, R.drawable.tab_sett_on};
    private ImageView bottomMapImg;
    private ImageView bottomFavoriteImg;
    private ImageView bottomPopulationImg;
    private ImageView bottomSettingImg;
    private TextView bottomMapTv;
    private TextView bottomFavoriteTv;
    private TextView bottomPopulationTv;
    private TextView bottomSettingTv;
    private LinearLayout bottomLayout;

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
        bottomMapImg = (ImageView) findViewById(R.id.bottom_map_img);
        bottomFavoriteImg = (ImageView) findViewById(R.id.bottom_favorite_img);
        bottomPopulationImg = (ImageView) findViewById(R.id.bottom_population_img);
        bottomSettingImg = (ImageView) findViewById(R.id.bottom_setting_img);
        bottomMapTv = (TextView) findViewById(R.id.bottom_map_tv);
        bottomFavoriteTv = (TextView) findViewById(R.id.bottom_favorite_tv);
        bottomPopulationTv = (TextView) findViewById(R.id.bottom_population_tv);
        bottomSettingTv = (TextView) findViewById(R.id.bottom_setting_tv);
        bottomLayout = (LinearLayout) findViewById(R.id.group_bottom_layout);

        int curFragment = getIntent().getIntExtra("FRAGMENT_ORDER", 0);
        ContainerAdapter adapter = new ContainerAdapter(getSupportFragmentManager());
        viewPager = (FreezingViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(curFragment);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setPagingEnabled(false);

        MapFragment pf = new MapFragment();
        pf.setPopulationFragmentCallback(this);
    }

    @Override
    public void setVisibilityBottomTabBar(boolean status) {
        if (status) {
            bottomLayout.setVisibility(View.VISIBLE);
        } else {
            bottomLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void SetOnCurrentFragment(int status) {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void OnBottomCallBback() {
        setActivedBottomTab(bottomMapImg, bottomMapTv, 0);
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

    private void setActivedBottomTab(ImageView imageView, TextView textView, int status) {
        bottomMapImg.setBackground(getResources().getDrawable(R.drawable.tab_map_off));
        bottomFavoriteImg.setBackground(getResources().getDrawable(R.drawable.tab_like_off));
        bottomPopulationImg.setBackground(getResources().getDrawable(R.drawable.tab_popu_off));
        bottomSettingImg.setBackground(getResources().getDrawable(R.drawable.tab_sett_off));
        imageView.setBackground(getResources().getDrawable(bottomBarImgs[status]));

        bottomMapTv.setTextColor(getResources().getColor(R.color.darkdeepgray));
        bottomFavoriteTv.setTextColor(getResources().getColor(R.color.darkdeepgray));
        bottomPopulationTv.setTextColor(getResources().getColor(R.color.darkdeepgray));
        bottomSettingTv.setTextColor(getResources().getColor(R.color.darkdeepgray));
        textView.setTextColor(getResources().getColor(R.color.navy));
    }
}
