package com.sifiso.codetribe.summarylib.util;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aubreyM on 2014/08/28.
 */
public class TimerUtil {

    public interface TimerListener {
        public void onRefreshData();
    }

    static TimerListener listener;
    static Timer timer;
    static final long FIVE_MINUTES = 5 * 60 * 1000;

    public static void startTimer(TimerListener timerListener) {
        //
        Log.d("TimerUtil", "########## Websocket Session Timer starting .....");
        listener = timerListener;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e("TimerUtil", "########## about to disconnect websocket session");

                listener.onRefreshData();
            }
        }, 0, FIVE_MINUTES);
    }

    public static void killTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            Log.w("TimerUtil", "########## Websocket Session Timer KILLED");
        }
    }
}
