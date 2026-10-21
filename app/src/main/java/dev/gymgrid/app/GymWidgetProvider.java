package dev.gymgrid.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class GymWidgetProvider extends AppWidgetProvider {
    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id : ids) update(context, manager, id, false);
    }

    static void update(Context context, AppWidgetManager manager, int id, boolean year) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setImageViewBitmap(R.id.widget_image,
                GridPainter.widget(WorkoutStore.days(context), year,
                        year ? 900 : 480, year ? 480 : 480));
        Intent open = new Intent(context, MainActivity.class);
        PendingIntent pending = PendingIntent.getActivity(context, 0, open,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_image, pending);
        manager.updateAppWidget(id, views);
    }

    static void refreshAll(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] months = manager.getAppWidgetIds(new ComponentName(context, GymWidgetProvider.class));
        int[] years = manager.getAppWidgetIds(new ComponentName(context, YearWidgetProvider.class));
        for (int id : months) update(context, manager, id, false);
        for (int id : years) update(context, manager, id, true);
    }
}
