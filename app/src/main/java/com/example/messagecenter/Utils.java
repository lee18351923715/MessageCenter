package com.example.messagecenter;

import android.content.ContentValues;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
//    public static int dp2px(Context context, float dp) {
//    return (int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
//    }
//
//    /**
//     * 获得屏幕宽度
//     *
//     * @param context
//     * @return
//     */
//    public static int getScreenWidth(Context context) {
//
//        if(context!=null){
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Log.d("信息", "getScreenWidth: ");
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics( outMetrics);
//        return outMetrics .widthPixels ;
//        }
//        return 0;
//    }


    public static ContentValues set(String title, String message, int flag, int type){
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("message",message);
        values.put("flag", flag);
        values.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString());
        values.put("type",type);
        return values;
    }

}
