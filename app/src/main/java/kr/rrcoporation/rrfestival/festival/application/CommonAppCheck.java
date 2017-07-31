package kr.rrcoporation.rrfestival.festival.application;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class CommonAppCheck {

    public static void permissionCheckListener(Context context) {
        new TedPermission(context)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("GPS 접근 권한이 필요합니다.")
                .setDeniedMessage("거부 하셨습니다.\n하지만 [설정] > [권한] 에서 GPS 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    public static void permissionSMSCheckListener(Context context) {
        new TedPermission(context)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("SMS 접근 권한이 필요합니다.")
                .setDeniedMessage("거부 하셨습니다.\n하지만 [설정] > [권한] 에서 SMS 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.READ_SMS)
                .check();
    }

    private static PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        }
    };

    public static boolean isGooglePlayServiceInstalled(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    public static void installPlayService(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Google Play Services")
                .setMessage("구글 플래이 서비스를 설치 혹은 업데이트 하셔야 서비스 이용이 가능합니다. 설치 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("설치", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        try {
                            // 허니콤 이상에서 스토어를 통하여 플레이 서비스를 설치하도록 유도.
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.gms"));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            intent.setPackage("com.android.vending");
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            try {
                                // 허니콤 미만에서 스토어를 통하여 플레이 서비스를 설치하도록 유도.
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                intent.setPackage("com.android.vending");
                                context.startActivity(intent);
                            }
                            // 마켓 앱이 없다면 마켓 웹 페이지로 이동시켜주도록한다.
                            catch (ActivityNotFoundException f) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.gms"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                context.startActivity(intent);
                            }
                        }
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).create().show();
    }

    public static boolean checkLocationPermission(Context context) {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = -1;
        if (context != null) {
            res = context.checkCallingOrSelfPermission(permission);
        }
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
