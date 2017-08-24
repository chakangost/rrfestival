package kr.rrcoporation.rrfestival.festival.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import java.util.List;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.activity.FestivalDetailActivity;
import kr.rrcoporation.rrfestival.festival.model.BodyItem;
import kr.rrcoporation.rrfestival.festival.model.ExtraConstants;
import kr.rrcoporation.rrfestival.festival.store.MyBookmarkStore;
import kr.rrcoporation.rrfestival.festival.transaction.ApiAction;
import kr.rrcoporation.rrfestival.festival.view.BookmarkAdapter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class FavFragment extends CommonFragment implements AdapterView.OnItemClickListener, SwipeMenuListView.OnMenuItemClickListener {

    private LinearLayout      rootLayout;
    private Subscription      subscription;
    private List<BodyItem>    bookmarkList;
    private SwipeMenuListView listView;
    private BookmarkAdapter   bookmarkAdapter;
    private SQLiteDatabase    db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_fav, null);
        if (db == null) {
            db = getActivity().openOrCreateDatabase("FESTIVALS.db", Context.MODE_PRIVATE, null);
        }
        listView = (SwipeMenuListView) rootLayout.findViewById(R.id.listview);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView.setMenuCreator(creator);
        listView.setOnItemClickListener(this);
        listView.setOnMenuItemClickListener(this);
        observeBookmarkStore();
        return rootLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ApiAction.getInstance().fetchBookmarks();
    }

    private void observeBookmarkStore() {
        subscription = MyBookmarkStore.getInstance().observe().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Void aVoid) {
                bookmarkList = MyBookmarkStore.getInstance().getBookmarks();
                bookmarkAdapter = new BookmarkAdapter(getActivity(), bookmarkList);
                listView.setAdapter(bookmarkAdapter);
            }
        });
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            case 0:
                Toast.makeText(getActivity(), "취소", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                deleteBookmark(bookmarkList.get(position).getContentid());
                bookmarkList.remove(position);
                bookmarkAdapter.notifyDataSetChanged();
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), FestivalDetailActivity.class);
        BodyItem item = bookmarkList.get(position);
        intent.putExtra(ExtraConstants.EXTRA_CONTENT_TYPE_ID, item.getContenttypeid());
        intent.putExtra(ExtraConstants.EXTRA_CONTENT_ID, item.getContentid());
        startActivity(intent);
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
            openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
            openItem.setWidth(dp2px(90));
            openItem.setTitle("취소");
            openItem.setTitleSize(18);
            openItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(openItem);

            SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
            deleteItem.setWidth(dp2px(90));
            deleteItem.setIcon(R.drawable.ic_delete);
            menu.addMenuItem(deleteItem);
        }
    };

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void deleteBookmark(int contentId) {
        String tableName = "festival";
        String str1 = "delete from " + tableName + " where contentid = " + contentId + ";";

        db.beginTransaction();
        db.execSQL(new StringBuilder(str1).toString());
        db.setTransactionSuccessful();
        db.endTransaction();

        ApiAction.getInstance().fetchBookmarks();
        Toast.makeText(getActivity(), "삭제됨", Toast.LENGTH_SHORT).show();
    }
}