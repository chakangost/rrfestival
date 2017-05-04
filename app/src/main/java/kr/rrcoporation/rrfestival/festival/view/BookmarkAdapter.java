package kr.rrcoporation.rrfestival.festival.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.util.List;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;

public class BookmarkAdapter extends BaseAdapter {

    private Context        context;
    private List<BodyItem> list;
    private BookmarkHolder holder;
    public  RequestManager mGlideRequestManager;

    public BookmarkAdapter(Context context, List<BodyItem> list) {
        this.context = context;
        this.list = list;
        mGlideRequestManager = Glide.with(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.bookmark_list_item, parent, false);
            holder = new BookmarkHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.phone = (TextView) convertView.findViewById(R.id.phone);
            holder.thumnail = (ImageView) convertView.findViewById(R.id.thumnail);
            convertView.setTag(holder);
        } else {
            holder = (BookmarkHolder) convertView.getTag();
        }

        holder.title.setText(list.get(position).getTel());
        holder.address.setText(list.get(position).getAddr1());
        holder.phone.setText(list.get(position).getTel());
        mGlideRequestManager.load(list.get(position).getFirstimage()).into(holder.thumnail);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    class BookmarkHolder {
        TextView title;
        TextView address;
        TextView phone;
        ImageView thumnail;
    }
}
