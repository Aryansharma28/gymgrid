package dev.gymgrid.app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

public class YearWidgetProvider extends AppWidgetProvider {
    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id : ids) GymWidgetProvider.update(context, manager, id, true);
    }

    @Override public void onAppWidgetOptionsChanged(Context context,
            AppWidgetManager manager, int id, android.os.Bundle options) {
        GymWidgetProvider.update(context, manager, id, true);
    }
}
