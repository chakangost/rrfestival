package kr.rrcoporation.rrfestival.festival.util.helper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;

import kr.rrcoporation.rrfestival.festival.model.ExtraConstants;
import kr.rrcoporation.rrfestival.festival.util.BaseHelper;
import kr.rrcoporation.rrfestival.festival.util.FlowParameters;

public class FragmentHelper extends BaseHelper {
    private Fragment mFragment;

    public FragmentHelper(Fragment fragment) {
        super(fragment.getContext().getApplicationContext(),
              (FlowParameters) fragment.getArguments().getParcelable(ExtraConstants.EXTRA_FLOW_PARAMS));
        mFragment = fragment;
    }

    public static Bundle getFlowParamsBundle(FlowParameters params) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ExtraConstants.EXTRA_FLOW_PARAMS, params);
        return bundle;
    }

    public void finish(int resultCode, Intent intent) {
        finishActivity(mFragment.getActivity(), resultCode, intent);
    }

    @Override
    public void showLoadingDialog(String message) {
        dismissDialog();
        ContextThemeWrapper context =
                new ContextThemeWrapper(mFragment.getContext(), getFlowParams().themeId);
        mProgressDialog = ProgressDialog.show(context, "", message, true);
    }

    public void startIntentSenderForResult(IntentSender sender, int requestCode)
            throws IntentSender.SendIntentException {
        mFragment.startIntentSenderForResult(sender, requestCode, null, 0, 0, 0, null);
    }
}
