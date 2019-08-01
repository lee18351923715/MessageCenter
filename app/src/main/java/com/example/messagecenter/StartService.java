package com.example.messagecenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 广播接收器
 */
public class StartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context,"您已收到一条信息，请尽快查收！",Toast.LENGTH_SHORT).show();
    }
}
