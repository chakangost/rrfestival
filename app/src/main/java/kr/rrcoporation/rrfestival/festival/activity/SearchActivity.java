package kr.rrcoporation.rrfestival.festival.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.ArrayList;
import java.util.List;
import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.application.CommonAppCheck;
import kr.rrcoporation.rrfestival.festival.callback.SearchResultCallback;
import kr.rrcoporation.rrfestival.festival.util.Util;

public class SearchActivity extends CommonFragmentActivity implements GoogleApiClient.ConnectionCallbacks, TextView.OnEditorActionListener, View.OnClickListener {

    private SQLiteDatabase localDB;
    private List<String>           latelyKeywords   = new ArrayList<>();
    private List<AutoCompleteData> autoCompleteList = new ArrayList<>();
    private LayoutInflater  inflater;
    private Intent          intent;
    private Location        currentLoaction;
    private GoogleApiClient apiClient;
    private LocationManager locationManager;
    private TextView        tvListComment;
    private boolean latelyKeywordClick = false;
    private static SearchResultCallback searchResultCallback;
    private boolean latelyItemClicked = false;
    private boolean isExistMsg = true;
    private ListView   lvResult;
    private EditText   etKeyword;
    private Button     btnKeywordDel;
    private Button     btnSearch;
    private FrameLayout     btnBack;
    private ImageView  ivKeywrodUnderbar;
    private ImageView  ivNotFound;
    private TextView   tvNotFound;

    public void setSearchResultCallback(SearchResultCallback searchResultCallback) {
        SearchActivity.searchResultCallback = searchResultCallback;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_search_view);
        initView();
    }

    private void initView() {
        initResource();
        initListener();
        initSetting();
    }

    private void initResource() {
        lvResult = (ListView) findViewById(R.id.lvSearchResult);
        etKeyword = (EditText) findViewById(R.id.etKeyword);
        btnKeywordDel = (Button) findViewById(R.id.btnKeywordDel);
        btnBack = (FrameLayout) findViewById(R.id.back_frame_btn);
        btnSearch = (Button) findViewById(R.id.btnFavoriteSearch);
        ivKeywrodUnderbar = (ImageView) findViewById(R.id.ivKeywordUnderbar);
        ivNotFound = (ImageView) findViewById(R.id.ivNotFound);
        tvNotFound = (TextView) findViewById(R.id.tvNotFound);
    }

    private void initListener() {
        btnBack.setOnClickListener(this);
        btnKeywordDel.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable et) {
                if (et.length() > 0) {
                    if (et.toString().matches("[^가-힣\\x20]")) {
                        return;
                    }
                    LatLng curLatLng = getCurrentLocation();
                    if (curLatLng == null) {
                        return;
                    }

                    AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build();
                    btnKeywordDel.setVisibility(View.VISIBLE);
                    btnSearch.setVisibility(View.VISIBLE);
                    ivKeywrodUnderbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.mainColor));

                    LatLngBounds latLngBounds = Util.boundsWithCenterAndLatLngDistance(curLatLng, 12000, 12000);
                    PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(apiClient, et.toString(),
                            latLngBounds, autocompleteFilter);

                    result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
                        @Override
                        public void onResult(@NonNull AutocompletePredictionBuffer result) {
                            if (result == null) {
                                if (lvResult.getHeaderViewsCount() == 0) {
                                    lvResult.addHeaderView(tvListComment);
                                }
                                return;
                            }

                            if (result.getStatus().isSuccess()) {
                                CharacterStyle style = new CharacterStyle() {
                                    @Override
                                    public void updateDrawState(TextPaint tp) {
                                    }
                                };
                                autoCompleteList.clear();

                                for (AutocompletePrediction prediction : result) {
                                    autoCompleteList.add(new AutoCompleteData(prediction.getPlaceId(), prediction.getPrimaryText(style).toString(),
                                            prediction.getSecondaryText(style).toString()));
                                }

                                SearchResultAdapter adapter = new SearchResultAdapter(autoCompleteList);
                                lvResult.setAdapter(adapter);

                                if (!isExistMsg && autoCompleteList.size() > 0) {
                                    tvListComment.setText("혹시 여기를 찾으세요?");
                                    lvResult.setVisibility(View.VISIBLE);
                                    ivNotFound.setVisibility(View.GONE);
                                    tvNotFound.setVisibility(View.GONE);
                                    isExistMsg = true;
                                }
                                if (isExistMsg && autoCompleteList.size() == 0) {
                                    lvResult.setVisibility(View.GONE);
                                    ivNotFound.setVisibility(View.VISIBLE);
                                    tvNotFound.setVisibility(View.VISIBLE);
                                    isExistMsg = false;
                                }
                            }

                            if (latelyKeywordClick) {
                                getPlaceDetailInfo(autoCompleteList.get(0).getId());
                            }

                            result.release();
                        }
                    });
                } else {
                    ivKeywrodUnderbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.lightgray));
                    btnKeywordDel.setVisibility(View.GONE);
                    btnSearch.setVisibility(View.GONE);
                    getLatelyKeywords();

                    autoCompleteList.clear();
                    lvResult.setVisibility(View.VISIBLE);
                    ivNotFound.setVisibility(View.GONE);
                    tvNotFound.setVisibility(View.GONE);
                    isExistMsg = true;
                }
            }
        });
    }

    private void initSetting() {
        getActManager().addActivity(this);
        localDB = openOrCreateDatabase("local", Context.MODE_PRIVATE, null);
        createKeywordTable();
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        apiClient.connect();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        intent = getIntent();

        tvListComment = new TextView(this);
        float dp = getResources().getDisplayMetrics().density;
        tvListComment.setPadding((int) dp * 16, (int) dp * 24, 0, 0);
        tvListComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tvListComment.setTextColor(Color.parseColor("#BDBDBD"));
        lvResult.setDivider(null);
        getLatelyKeywords();
        etKeyword.requestFocus();
        etKeyword.setOnEditorActionListener(this);

        setLocationManager();
        checkIfLocationServicesEnabled();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_frame_btn:
                onBackPressed();
                break;
            case R.id.btnKeywordDel:
                etKeyword.setText("");
                break;
            case R.id.btnFavoriteSearch:
                if (autoCompleteList.size() > 0) {
                    getPlaceDetailInfo(autoCompleteList.get(0).getId());
                    insertLatelyKeyword(autoCompleteList.get(0).getName());
                }
                break;

        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH && autoCompleteList.size() > 0) {
            getPlaceDetailInfo(autoCompleteList.get(0).getId());
            insertLatelyKeyword(autoCompleteList.get(0).getName());
        }
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("PREV", "FAVORITE_SEARCH");
        setResult(100, intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        apiClient.disconnect();
        if (localDB != null && localDB.isOpen()) {
            localDB.close();
        }
    }

    private LatLng getCurrentLocation() {
        if (currentLoaction == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                currentLoaction = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (currentLoaction == null) {
                    currentLoaction = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
        }

        if (currentLoaction == null) {
            Toast.makeText(SearchActivity.this, "위치정보를 가져오는데 실패했습니다. GPS(내위치)를 활성화 시켜주세요.", Toast.LENGTH_SHORT).show();
            return null;
        }

        return new LatLng(currentLoaction.getLatitude(), currentLoaction.getLongitude());
    }

    private void setLocationManager() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (CommonAppCheck.checkLocationPermission(this)) {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        }
    }

    private LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLoaction = location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    private void createKeywordTable() {
        String query = "CREATE TABLE IF NOT EXISTS latest_keyword (idx integer primary key autoincrement, keyword text)";
        try {
            localDB.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SearchResultAdapter extends BaseAdapter {

        private List<AutoCompleteData> list = new ArrayList<>();
        private SearchResultHolder holder;

        public SearchResultAdapter(List<AutoCompleteData> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.place_search_result_item, parent, false);
                holder = new SearchResultHolder();

                holder.tvName = (TextView) convertView.findViewById(R.id.tvSearchedPlaceName);
                holder.tvAddress = (TextView) convertView.findViewById(R.id.tvSearchedPlaceAddress);

                convertView.setTag(holder);
            } else {
                holder = (SearchResultHolder) convertView.getTag();
            }

            holder.tvName.setText(list.get(position).getName());
            holder.tvAddress.setText(list.get(position).getAddress());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentLoaction == null) {
                        Toast.makeText(SearchActivity.this, "위치정보를 가져오는데 실패했습니다. GPS(내위치)를 활성화 시켜주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    getPlaceDetailInfo(list.get(position).getId());
                    insertLatelyKeyword(list.get(position).getName());
                }
            });

            return convertView;
        }

        private class SearchResultHolder {
            TextView tvName;
            TextView tvAddress;
        }
    }

    private class LatelyKeywordAdapter extends BaseAdapter {

        private LatelyKeywordHolder holder;
        private List<String> list = new ArrayList<>();

        public LatelyKeywordAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lately_keyword_list_item, parent, false);
                holder = new LatelyKeywordHolder();

                holder.tvKeyword = (TextView) convertView.findViewById(R.id.tvLatelyKeyword);
                holder.btnDel = (Button) convertView.findViewById(R.id.btnLatelyKeywordDel);

                convertView.setTag(holder);
            } else {
                holder = (LatelyKeywordHolder) convertView.getTag();
            }

            holder.tvKeyword.setText(list.get(position));
            holder.btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String delQuery = "DELETE FROM latest_keyword WHERE keyword='" + holder.tvKeyword.getText().toString() + "'";
                    localDB.execSQL(delQuery);
                    list.remove(position);
                    notifyDataSetChanged();
                    if (list.size() == 0) {
                        lvResult.removeHeaderView(tvListComment);
                    }
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (latelyItemClicked) {
                        return;
                    }
                    latelyItemClicked = true;

                    LatLng curLatLng = getCurrentLocation();
                    if ( curLatLng != null) {

                        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build();
                        LatLngBounds latLngBounds = Util.boundsWithCenterAndLatLngDistance(curLatLng, 12000, 12000);
                        PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(apiClient, list.get(position),
                                latLngBounds, autocompleteFilter);

                        result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
                            @Override
                            public void onResult(@NonNull AutocompletePredictionBuffer result) {
                                if (result == null) {
                                    return;
                                }
                                if (result.getStatus().isSuccess()) {
                                    getPlaceDetailInfo(result.get(0).getPlaceId());
                                    latelyItemClicked = false;
                                }
                                result.release();
                            }
                        });
                    }
                }
            });

            return convertView;
        }

        private class LatelyKeywordHolder {
            TextView tvKeyword;
            Button btnDel;
        }
    }

    private static class AutoCompleteData {
        private String id;
        private String name;
        private String address;

        public AutoCompleteData(String id, String name, String address) {
            this.id = id;
            this.name = name;
            this.address = address;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    private void getPlaceDetailInfo(String placeId) {
        Places.GeoDataApi.getPlaceById(apiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place myPlace = places.get(0);
                            LatLng myLatlng = myPlace.getLatLng();

                            if (intent.getStringExtra("searchType").equals("main")) {
                                if (searchResultCallback != null) {
                                    searchResultCallback.onSearchResult(myPlace.getLatLng().latitude, myPlace.getLatLng().longitude, myPlace.getName().toString());
                                }
                                finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                                intent.putExtra("LAT", myLatlng.latitude);
                                intent.putExtra("LNG", myLatlng.longitude);
                                intent.putExtra("TITLE", myPlace.getName().toString());
                                startActivity(intent);
                            }
                        }
                        places.release();
                    }
                });
    }

    private void getLatelyKeywords() {
        latelyKeywords.clear();
        String query = "SELECT DISTINCT keyword FROM latest_keyword ORDER BY idx DESC";
        Cursor cursor = localDB.rawQuery(query, null);

        while (cursor.moveToNext()) {
            latelyKeywords.add(cursor.getString(cursor.getColumnIndex("keyword")));
        }

        LatelyKeywordAdapter keywordAdapter = new LatelyKeywordAdapter(latelyKeywords);

        if (latelyKeywords.size() == 0) {
            tvListComment.setText("");
            if (lvResult.getHeaderViewsCount() > 0) {
                lvResult.removeHeaderView(tvListComment);
            }
        } else {
            tvListComment.setText("최근 검색지");
            if (lvResult.getHeaderViewsCount() == 0) {
                lvResult.addHeaderView(tvListComment);
            }
        }
        lvResult.setAdapter(keywordAdapter);
    }

    private void insertLatelyKeyword(String keyword) {
        String proccessdKeyword = keyword;
        if(keyword.contains("'")){
            proccessdKeyword = keyword.replace("'", "''");
        }
        String query = "INSERT INTO latest_keyword (keyword) VALUES('" + proccessdKeyword + "')";
        localDB.execSQL(query);
    }

    private void checkIfLocationServicesEnabled() {
        LocationRequest request = new LocationRequest()
                .setInterval(1000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(apiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(SearchActivity.this, 20001);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
}
