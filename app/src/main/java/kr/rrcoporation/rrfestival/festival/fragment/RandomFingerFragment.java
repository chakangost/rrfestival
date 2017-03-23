package kr.rrcoporation.rrfestival.festival.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.wenchao.cardstack.CardStack;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.view.CardsDataAdapter;

public class RandomFingerFragment extends CommonFragment {

    private RelativeLayout     rootLayout;
    private CardStack        mCardStack;
    private CardsDataAdapter mCardAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_random_finger, null);
        mCardStack = (CardStack) rootLayout.findViewById(R.id.container);
        mCardStack.setContentResource(R.layout.festival_detail);

        mCardStack.setStackMargin(20);

        mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext());
        mCardAdapter.add("test1");
        mCardAdapter.add("test2");
        mCardAdapter.add("test3");
        mCardAdapter.add("test4");
        mCardAdapter.add("test5");
        mCardAdapter.add("test6");
        mCardAdapter.add("test7");

        mCardStack.setAdapter(mCardAdapter);

        return rootLayout;
    }
}
