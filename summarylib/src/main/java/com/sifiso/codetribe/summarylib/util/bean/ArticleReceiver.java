package com.sifiso.codetribe.summarylib.util.bean;


import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class ArticleReceiver extends ResultReceiver {
    private Receiver mReceiver;



    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle bundle);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }


    public ArticleReceiver(Handler handler) {
        super(handler);
    }
}
