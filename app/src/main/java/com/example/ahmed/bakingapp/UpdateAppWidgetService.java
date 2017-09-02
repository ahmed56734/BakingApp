package com.example.ahmed.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ahmed on 7/2/17.
 */

public class UpdateAppWidgetService extends IntentService {

    public UpdateAppWidgetService() {
        super("UpdateAppWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        AppWidgetManager appWidgetManager =  AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsListWidgetProvider.class));
        IngredientsListWidgetProvider.updateIngredientsWidget(this, appWidgetManager, appWidgetIds);
    }

    public static void startActionUpdateIngredientsWidget(Context context){
        Intent intent = new Intent(context, UpdateAppWidgetService.class);
        context.startService(intent);
    }
}
