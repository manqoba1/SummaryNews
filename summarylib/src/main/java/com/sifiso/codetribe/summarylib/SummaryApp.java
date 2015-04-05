package com.sifiso.codetribe.summarylib;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sifiso.codetribe.summarylib.toolbox.BaseVolley;
import com.sifiso.codetribe.summarylib.toolbox.BohaVolley;
import com.sifiso.codetribe.summarylib.util.SummaryIntentService;
import com.sifiso.codetribe.summarylib.util.bean.ArticleReceiver;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by CodeTribe1 on 2015-02-13.
 */
public class SummaryApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        DisplayImageOptions defaultOptions =
                new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .showImageOnFail(getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher))
                        .showImageOnLoading(getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher))
                        .build();

        File cacheDir = StorageUtils.getCacheDirectory(this, true);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .memoryCache(new LruMemoryCache(16 * 1024 * 1024))
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        if (!BaseVolley.checkNetworkOnDevice(getApplicationContext())) {
           // return;
        }
        BohaVolley.getRequestQueue(getApplicationContext());

        ImageLoader.getInstance().init(config);
    }
}
