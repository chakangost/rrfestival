package kr.rrcoporation.rrfestival.festival.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

public class Util {

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
}
