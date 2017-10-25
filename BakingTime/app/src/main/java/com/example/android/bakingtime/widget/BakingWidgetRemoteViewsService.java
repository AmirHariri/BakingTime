package com.example.android.bakingtime.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;


/**
 * Created by Amir on 10/24/2017.
 */

public class BakingWidgetRemoteViewsService extends RemoteViewsService {

    private static final String LOG_TAG = BakingWidgetRemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.e(LOG_TAG, "onGetViewFactory: " + "Service called");
        return new BakingWidgetRemoteViewsFactory(this.getApplicationContext(),intent) ;
    }



}
