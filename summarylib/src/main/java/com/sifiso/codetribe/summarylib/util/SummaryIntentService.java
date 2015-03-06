package com.sifiso.codetribe.summarylib.util;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.sifiso.codetribe.summarylib.model.Article;
import com.sifiso.codetribe.summarylib.model.Category;
import com.sifiso.codetribe.summarylib.model.ResponseData;
import com.sifiso.codetribe.summarylib.util.bean.DataUtil;

import android.os.ResultReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SummaryIntentService extends IntentService {
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;


    public interface SummaryIntentServiceListener {
        public void onReceive();
    }

    public SummaryIntentService() {
        super("SummaryIntentService");

    }

    Gson gson = new Gson();
    static String LOG = SummaryIntentService.class.getSimpleName();

    @Override
    protected void onHandleIntent(Intent intent) {
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        int categoryID = intent.getIntExtra("categoryID", 2);
        Bundle b = new Bundle();
        Log.d(LOG, "Beginning the download " + categoryID);

        receiver.send(STATUS_RUNNING, b.EMPTY);
        try {
            ResponseData resp = new ResponseData();

            resp.setArticles(DataUtil.getArticles(categoryID, null, getApplicationContext()));

            if (resp != null /*&& categories != null*/) {
                Log.d(LOG, "int = : " + DataUtil.getArticles(categoryID, null, getApplicationContext()).size());

                b.putSerializable("article", DataUtil.getArticles(categoryID, null, getApplicationContext()));

                receiver.send(STATUS_FINISHED, b);
            }
        } catch (Exception e) {
            b.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, b);

        }

    }


}
