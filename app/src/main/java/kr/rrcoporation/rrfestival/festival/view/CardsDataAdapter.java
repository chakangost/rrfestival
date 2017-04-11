package kr.rrcoporation.rrfestival.festival.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.util.List;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;

public class CardsDataAdapter extends ArrayAdapter<String> {

    private Context        context;
    private List<BodyItem> festivals;
    public  RequestManager mGlideRequestManager;

    public CardsDataAdapter(Context context, List<BodyItem> festivals) {
        super(context, R.layout.festival_detail);
        this.context = context;
        this.festivals = festivals;
        mGlideRequestManager = Glide.with(context);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        View v = contentView;
        if (v == null) {
            v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.festival_detail, parent, false);
        }

        BodyItem item = festivals.get(position);

        TextView title = (TextView) v.findViewById(R.id.content);
        TextView address = (TextView) v.findViewById(R.id.address);
        TextView tel = (TextView) v.findViewById(R.id.tel);
        final ImageView image = (ImageView) v.findViewById(R.id.image);

        mGlideRequestManager.load(item.getFirstimage()).thumbnail(0.1f).error(R.mipmap.ic_launcher).into(image);

        title.setText(item.getTitle());
        address.setText(item.getAddr1());
        tel.setText(item.getTel());

        return contentView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return festivals.size();
    }

}
