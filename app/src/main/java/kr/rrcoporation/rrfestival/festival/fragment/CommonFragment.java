package kr.rrcoporation.rrfestival.festival.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.util.RRCommonDialog;
import kr.rrcoporation.rrfestival.festival.util.Util;

public class CommonFragment extends Fragment {
    private RRCommonDialog networkErrorDialog = null;

    private void initNetworkDialog() {

        if ( networkErrorDialog == null ) {

            networkErrorDialog = RRCommonDialog.newInstance(RRCommonDialog.TYPE_ALERT_DIALOG, (Context) getActivity());
            networkErrorDialog.setTitle(R.string.network_connect_fail)
                    .setMessage(R.string.network_connect_retry)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ( !networkErrorDialog.isShow() ) {

                    networkErrorDialog.show(getFragmentManager(), "");
                }
            }
        });

    }

    protected boolean isNetwork() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobile = false;
        if (!Util.isTablet(getActivity())) {
            isMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        }
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isMobile || isWifi;
    }

    void showNetworkRetryDialog() {
        initNetworkDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("test", " Attach");
    }
}
