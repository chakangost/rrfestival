package kr.rrcoporation.rrfestival.festival.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import kr.rrcoporation.rrfestival.festival.R;
import kr.rrcoporation.rrfestival.festival.callback.OnRRCommonDialogClickCallBack;

public class RRCommonDialog extends DialogFragment {

    public static final int TYPE_ALERT_DIALOG = 1;
    public static final int TYPE_CUSTOM_DIALOG = 2;

    private        View    layout;
    private static Context context;
    private boolean dialogIsEnable = false;
    private boolean isShow = false;

    private OnRRCommonDialogClickCallBack onLimeCommonDialogClickCallBack;

    private String                          title;
    private String                          message;
    private String                          positiveButtonText;
    private String                          negativeButtonText;
    private DialogInterface.OnClickListener positiveClickListener;
    private DialogInterface.OnClickListener negativeClickListener;

    public void setOnCTSDialogClickCallBack(OnRRCommonDialogClickCallBack onLimeCommonDialogClickCallBack) {
        this.onLimeCommonDialogClickCallBack = onLimeCommonDialogClickCallBack;
    }

    public static RRCommonDialog newInstance(int layout, Activity activity) {
        RRCommonDialog f = new RRCommonDialog();
        Bundle args = new Bundle();
        args.putInt("layout", layout);
        context = activity;
        f.setArguments(args);
        return f;
    }
    public static RRCommonDialog newInstance(int type, Context context) {
        RRCommonDialog f = new RRCommonDialog();
        Bundle args = new Bundle();
        args.putInt("type", type);
        RRCommonDialog.context = context;
        f.setArguments(args);
        return f;
    }

    public int getResource() {
        return getArguments().getInt("layout");
    }

    public RRCommonDialog setTitle (String title) {

        this.title = title;
        return this;
    }

    public RRCommonDialog setTitle (int textResource) {
        this.title = context.getResources().getString(textResource);
        return this;
    }

    public RRCommonDialog setMessage (String message) {

        this.message = message;
        return this;
    }

    public RRCommonDialog setMessage (int textResource) {
        this.message = context.getResources().getString(textResource);
        return this;
    }


    public RRCommonDialog setPositiveButton (String text, DialogInterface.OnClickListener listener) {
        this.positiveButtonText = text;
        this.positiveClickListener = listener;
        return this;
    }
    public RRCommonDialog setNegativeButton (String text, DialogInterface.OnClickListener listener) {
        this.negativeButtonText = text;
        this.negativeClickListener = listener;
        return this;
    }
    public RRCommonDialog setPositiveButton (int textResource, DialogInterface.OnClickListener listener) {
        this.positiveButtonText = context.getResources().getString(textResource);
        this.positiveClickListener = listener;
        return this;
    }
    public RRCommonDialog setNegativeButton (int textResource, DialogInterface.OnClickListener listener) {
        this.negativeButtonText = context.getResources().getString(textResource);
        this.negativeClickListener = listener;
        return this;
    }

    public RRCommonDialog setLayout ( int _layout ) {
        LayoutInflater mLayoutInflater = ((Activity) context).getLayoutInflater();
        this.layout = mLayoutInflater.inflate(_layout, null);

        return this;
    }
    public RRCommonDialog setLayout ( View _layout ) {
        LayoutInflater mLayoutInflater = ((Activity) context).getLayoutInflater();
        this.layout = _layout;

        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = null;

        int type = getArguments().getInt("type");

        if ( type == 0 ) {
            type = TYPE_CUSTOM_DIALOG;
        }


        switch (type) {
            case TYPE_ALERT_DIALOG :

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle( !"".equals(this.title) ? this.title : "")
                        .setMessage( !"".equals(this.message) ? this.message : "");

                if ( positiveButtonText != null ) {

                    builder.setPositiveButton(positiveButtonText, this.positiveClickListener);
                }

                if ( negativeButtonText != null ) {

                    builder.setNegativeButton(negativeButtonText, this.negativeClickListener);
                }

                final Dialog dialogPointer = dialog = builder.create();
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                dialog.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialogPointer.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                    }
                });

                break;
            case TYPE_CUSTOM_DIALOG :

                if ( this.layout == null ) {

                    LayoutInflater mLayoutInflater = ((Activity) context).getLayoutInflater();
                    layout = mLayoutInflater.inflate(getResource(), null);
                }

                dialog = new Dialog(getActivity(), R.style.RRCommonDialog) {
                    @Override
                    public void show() {
                        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                        this.getWindow().getDecorView().setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                        super.show();
                        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                    }
                };
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(layout);
                break;
        }


        dialogIsEnable = true;
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShow = false;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        isShow = true;
        return super.show(transaction, tag);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        isShow = true;
        super.show(manager, tag);
    }

    public boolean isShow() {

        return this.isShow;
    }

    public View getLayout() {
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (onLimeCommonDialogClickCallBack != null) {
            onLimeCommonDialogClickCallBack.onClickListener(layout);
        }
    }

    public boolean getIsEnable() {
        return dialogIsEnable;
    }
}
