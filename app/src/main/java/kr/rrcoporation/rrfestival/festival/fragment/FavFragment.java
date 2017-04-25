package kr.rrcoporation.rrfestival.festival.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;
import kr.rrcoporation.rrfestival.festival.store.MyBookmarkStore;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import kr.rrcoporation.rrfestival.festival.view.BookmarkAdapter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class FavFragment extends CommonFragment {

    private LinearLayout rootLayout;
    private Subscription subscription;
    private List<BodyItem> bookmarkList;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_fav, null);
        listView = (ListView) rootLayout.findViewById(R.id.listview);
        observeBookmarkStore();
        ApiAction.getInstance().fetchBookmarks();
        return rootLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void observeBookmarkStore() {
        subscription = MyBookmarkStore.getInstance().observe().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Void aVoid) {
                bookmarkList = MyBookmarkStore.getInstance().getBookmarks();
                BookmarkAdapter bookmarkAdapter = new BookmarkAdapter(getActivity(), bookmarkList);
                listView.setAdapter(bookmarkAdapter);
            }
        });
    }

}
