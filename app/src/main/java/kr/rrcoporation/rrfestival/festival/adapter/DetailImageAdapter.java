package kr.rrcoporation.rrfestival.festival.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.model.DetailImage;

/**
 * Created by fimtrus on 2017. 5. 17..
 */

public class DetailImageAdapter extends PagerAdapter {

    private Context                mContext;
    private ArrayList<DetailImage> mImageList;
    private LayoutInflater         mInflater;
    public  RequestManager         mGlideRequestManager;


    private DetailImageAdapter() {

    }
    public static DetailImageAdapter newInstance(Context context, ArrayList<DetailImage> list) {
        DetailImageAdapter adapter = new DetailImageAdapter();
        adapter.mContext = context;
        adapter.mImageList = list;
        adapter.mInflater = LayoutInflater.from(context);
        adapter.mGlideRequestManager = Glide.with(context);
        return adapter;
    }

    @Override
    public int getCount() {

        if(mImageList == null) {
            return 0;
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        int infinityPosition = position % mImageList.size();
        ImageView view = (ImageView) mInflater.inflate(R.layout.view_image, container, false);
        mGlideRequestManager.load(mImageList.get(infinityPosition).getOriginimgurl()).thumbnail(0.5f).error(R.drawable.thum_nodata).into(view);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
