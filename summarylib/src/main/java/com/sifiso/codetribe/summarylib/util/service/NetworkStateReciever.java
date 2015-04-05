package com.sifiso.codetribe.summarylib.util.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkStateReciever extends BroadcastReceiver {
    public NetworkStateReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       NetUtil.getConnectivityStatusString(intent,context);

    }
}
