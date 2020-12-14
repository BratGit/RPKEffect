package com.example.rpkeffect.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.rpkeffect.Fragments.FirstProviderFragment;
import com.example.rpkeffect.Interfaces.JsonInfoListener;
import com.example.rpkeffect.R;
import com.example.rpkeffect.Utils.JsonInfo;

public class HappyLogsWidget extends AppWidgetProvider implements JsonInfoListener {
    int mSuccess, mFail, mTotal;
    private static final String TAG = "myLog";
    RemoteViews widgetView;
    AppWidgetManager appWidgetManager;
    int[] id;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
            this.appWidgetManager = appWidgetManager;
            id = appWidgetIds;
        }
    }

    private void updateWidget(Context ctx, AppWidgetManager appWidgetManager, int widgetId) {
        widgetView = new RemoteViews(ctx.getPackageName(),
                R.layout.widget_happylogs);

        getJson();
        widgetView.setTextViewText(R.id.success, String.valueOf(mSuccess));
        widgetView.setTextViewText(R.id.fail, String.valueOf(mFail));
        widgetView.setTextViewText(R.id.total, String.valueOf(mTotal));
        Intent updateIntent = new Intent(ctx, HappyLogsWidget.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                new int[] { widgetId });
        PendingIntent pIntent = PendingIntent.getBroadcast(ctx, widgetId, updateIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.refresh, pIntent);
        appWidgetManager.updateAppWidget(widgetId, widgetView);
    }

    private void getJson() {
        JsonInfo info = new JsonInfo();
        info.setListener(this);
        info.execute("https://effect-gifts.ru/api/?action=getHappyLogs");
    }

    @Override
    public void onStartListener() {

    }

    @Override
    public void onFinishListener(int success, int fail, int total) {
        mSuccess = success;
        mFail = fail;
        mTotal = total;
        widgetView.setTextViewText(R.id.success, String.valueOf(mSuccess));
        widgetView.setTextViewText(R.id.fail, String.valueOf(mFail));
        widgetView.setTextViewText(R.id.total, String.valueOf(mTotal));
        appWidgetManager.updateAppWidget(id, widgetView);
    }
}
