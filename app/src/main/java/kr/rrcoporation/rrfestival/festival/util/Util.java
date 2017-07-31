package kr.rrcoporation.rrfestival.festival.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import kr.rrcoporation.rrfestival.festival.application.RRApplication;

public class Util {

    private static final double           ASSUMED_INIT_LATLNG_DIFF = 1.0;
    private static final float            ACCURACY                 = 0.01f;

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    /**
     * @Method : clearPreference
     * @param context
     * @return : void
     * @brief : 내부저장소 모든 데이타 삭제하는 메소드
     * @date : 2016. 4. 21.
     * @author : eunhokim
     */
    public static void clearPreference(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * @Method : removeSharedPreference
     * @param context
     * @param key
     * @return : void
     * @brief : 내부저장소 특정 키 값 삭제하는 메소드
     * @date : 2016. 4. 21.
     * @author : eunhokim
     */
    public static void removeSharedPreference(Context context, String key) {
        SharedPreferences ctsSPref = context.getSharedPreferences("cts_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor ctsSPrefEdit = ctsSPref.edit();
        ctsSPrefEdit.remove(key);
        ctsSPrefEdit.commit();
    }

    /**
     * @Method : getPreference
     * @param context
     * @param key
     * @return : boolean
     * @brief : 내부저장소 값 불러오는 메소드
     * @date : 2016. 4. 21.
     * @author : eunhokim
     */
    public static boolean getBooleanPreference(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("cts_pref", Activity.MODE_PRIVATE);
        boolean result = pref.getBoolean(key, false);
        return result;
    }

    /**
     * @Method : getSharedPreference
     * @param context
     * @param key
     * @return : String
     * @brief : 내부저장소 값 불러오는 메소드
     * @date : 2016. 4. 21.
     * @author : eunhokim
     */
    public static String getSharedPreference(Context context, String key) {
        SharedPreferences ctsSPref = context.getSharedPreferences("cts_pref", Context.MODE_PRIVATE);
        return ctsSPref.getString(key, "");
    }

    /**
     * @Method : setPreference
     * @param context
     * @param name
     * @param value
     * @return : void
     * @brief : 내부저장소 값 저장하는 메소드
     * @date : 2016. 4. 21.
     * @author : eunhokim
     */
    public static void setSharedPreference(Context context, String name, Object value) {
        SharedPreferences pref = context.getSharedPreferences("cts_pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (value instanceof Boolean) {
            editor.putBoolean(name, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(name, (Float) value);
        } else if (value instanceof Integer) {
            editor.putInt(name, (Integer) value);
        } else if (value instanceof String) {
            editor.putString(name, (String) value);
        } else if (value instanceof Long) {
            editor.putLong(name, (Long) value);
        }
        editor.commit();
    }

    public static LatLngBounds boundsWithCenterAndLatLngDistance(LatLng center, float latDistanceInMeters, float lngDistanceInMeters) {
        latDistanceInMeters /= 2;
        lngDistanceInMeters /= 2;
        LatLngBounds.Builder builder = LatLngBounds.builder();
        float[] distance = new float[1];
        {
            boolean foundMax = false;
            double foundMinLngDiff = 0;
            double assumedLngDiff = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude, center.longitude + assumedLngDiff, distance);
                float distanceDiff = distance[0] - lngDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLngDiff = assumedLngDiff;
                        assumedLngDiff *= 2;
                    } else {
                        double tmp = assumedLngDiff;
                        assumedLngDiff += (assumedLngDiff - foundMinLngDiff) / 2;
                        foundMinLngDiff = tmp;
                    }
                } else {
                    assumedLngDiff -= (assumedLngDiff - foundMinLngDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - lngDistanceInMeters) > lngDistanceInMeters * ACCURACY);
            LatLng east = new LatLng(center.latitude, center.longitude + assumedLngDiff);
            builder.include(east);
            LatLng west = new LatLng(center.latitude, center.longitude - assumedLngDiff);
            builder.include(west);
        }
        {
            boolean foundMax = false;
            double foundMinLatDiff = 0;
            double assumedLatDiffNorth = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude + assumedLatDiffNorth, center.longitude, distance);
                float distanceDiff = distance[0] - latDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLatDiff = assumedLatDiffNorth;
                        assumedLatDiffNorth *= 2;
                    } else {
                        double tmp = assumedLatDiffNorth;
                        assumedLatDiffNorth += (assumedLatDiffNorth - foundMinLatDiff) / 2;
                        foundMinLatDiff = tmp;
                    }
                } else {
                    assumedLatDiffNorth -= (assumedLatDiffNorth - foundMinLatDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
            LatLng north = new LatLng(center.latitude + assumedLatDiffNorth, center.longitude);
            builder.include(north);
        }
        {
            boolean foundMax = false;
            double foundMinLatDiff = 0;
            double assumedLatDiffSouth = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude - assumedLatDiffSouth, center.longitude, distance);
                float distanceDiff = distance[0] - latDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLatDiff = assumedLatDiffSouth;
                        assumedLatDiffSouth *= 2;
                    } else {
                        double tmp = assumedLatDiffSouth;
                        assumedLatDiffSouth += (assumedLatDiffSouth - foundMinLatDiff) / 2;
                        foundMinLatDiff = tmp;
                    }
                } else {
                    assumedLatDiffSouth -= (assumedLatDiffSouth - foundMinLatDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
            LatLng south = new LatLng(center.latitude - assumedLatDiffSouth, center.longitude);
            builder.include(south);
        }
        return builder.build();
    }

    /**
     * null 검사
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;
        return (obj instanceof String) && "".equals(((String) obj).trim());
    }

    /**
     * obj가 String 일 경우 obj를 그대로 return.<br>
     * obj가 String 이 아닐 경우 obj의 class name 을 return.
     * @param obj - null 인지 확인할 객체
     * @param str - obj가 null 인경우 대채할 문자열
     * @return obj가 null 일 경우 str을 return.<br>
     */
    public static String nvl(Object obj, String str) {

        String result = null;

        if (obj == null) {
            result = str;
        } else if (obj instanceof String) {
            if (((String)obj).trim().equals("")) {
                result = str;
            } else {
                result = obj.toString();
            }
        } else {
            result = obj.getClass().getName();
        }

        return result;
    }

    public static String getAppVersion() {
        try {
            PackageInfo info = RRApplication.getGlobalApplicationContext().getPackageManager().getPackageInfo(RRApplication.getGlobalApplicationContext().getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
