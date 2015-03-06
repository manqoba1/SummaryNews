package com.sifiso.codetribe.summarylib.sql;

import android.content.AsyncTaskLoader;
import android.content.Context;


import android.util.Log;

import com.google.gson.Gson;
import com.sifiso.codetribe.summarylib.model.ResponseData;
import com.sifiso.codetribe.summarylib.util.bean.DataUtil;

public class SummaryDataLoader extends AsyncTaskLoader<ResponseData> {


    private Gson gson;
    private Context context;
    static final String LOG = SummaryDataLoader.class.getSimpleName();

    public SummaryDataLoader(Context context) {
        super(context);
        this.context = context;
        gson = new Gson();
        // TODO Auto-generated constructor stub
    }

    @Override
    public ResponseData loadInBackground() {
        Log.d(LOG, "Loading data on Loader");
        ResponseData resp = new ResponseData();
        resp.setArticles(SummaryProvider.getArticles(context.getContentResolver()));
        resp.setCategories(SummaryProvider.getCategoryList(context.getContentResolver()));
        Log.d(LOG, "int = : " + SummaryProvider.getCategoryList(context.getContentResolver()).size());

        return resp;


    }
}
