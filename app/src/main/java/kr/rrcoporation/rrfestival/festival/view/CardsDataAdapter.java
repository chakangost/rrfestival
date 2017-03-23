package kr.rrcoporation.rrfestival.festival.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import kr.rrcoporation.rrfestival.festival.R;

public class CardsDataAdapter extends ArrayAdapter<String> {

    public CardsDataAdapter(Context context) {
        super(context, R.layout.festival_detail);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        TextView v = (TextView)(contentView.findViewById(R.id.content));
        v.setText(getItem(position));
        return contentView;
    }


}
