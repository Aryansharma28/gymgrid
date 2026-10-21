package dev.gymgrid.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import java.time.LocalDate;

public class GymWidgetProvider extends AppWidgetProvider {
    private static final String YES = "dev.gymgrid.app.ACTION_YES_TODAY";
    private static final String NO = "dev.gymgrid.app.ACTION_NO_TODAY";

    @Override public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (YES.equals(action) || NO.equals(action)) {
            WorkoutStore.setAnswer(context, LocalDate.now(), YES.equals(action));
        }
    }

    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id : ids) update(context, manager, id, false);
    }

    @Override public void onAppWidgetOptionsChanged(Context context,
            AppWidgetManager manager, int id, android.os.Bundle options) {
        update(context, manager, id, false);
    }

    static void update(Context context, AppWidgetManager manager, int id, boolean year) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        boolean yes = WorkoutStore.isDone(context, LocalDate.now());
        boolean answered = WorkoutStore.hasAnswer(context, LocalDate.now());
        views.setImageViewBitmap(R.id.widget_image,
                GridPainter.widget(WorkoutStore.days(context), WorkoutStore.answered(context),
                        year, year ? 900 : 480, year ? 175 : 250));
        views.setTextViewText(R.id.widget_status,
                answered ? (yes ? "Today: YES" : "Today: NO") : "Today: ready");
        views.setTextColor(R.id.widget_status,
                yes ? GridPainter.GREEN : answered ? GridPainter.SUBTEXT : GridPainter.TEXT);
        views.setTextViewText(R.id.widget_yes, yes ? "✓ YES" : "YES");
        views.setTextViewText(R.id.widget_no, answered && !yes ? "✓ NO" : "NO");
        views.setViewVisibility(R.id.widget_streak, year ? View.VISIBLE : View.GONE);
        if (year) views.setTextViewText(R.id.widget_streak,
                WorkoutStore.streak(context) + " DAY STREAK");
        Intent open = new Intent(context, MainActivity.class);
        PendingIntent pending = PendingIntent.getActivity(context, 0, open,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_image, pending);
        views.setOnClickPendingIntent(R.id.widget_title, pending);
        views.setOnClickPendingIntent(R.id.widget_status, pending);
        Intent yesIntent = new Intent(context, GymWidgetProvider.class).setAction(YES);
        Intent noIntent = new Intent(context, GymWidgetProvider.class).setAction(NO);
        views.setOnClickPendingIntent(R.id.widget_yes,
                PendingIntent.getBroadcast(context, 101, yesIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
        views.setOnClickPendingIntent(R.id.widget_no,
                PendingIntent.getBroadcast(context, 102, noIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
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
