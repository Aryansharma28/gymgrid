package dev.gymgrid.app;

import android.content.Context;
import android.content.SharedPreferences;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

final class WorkoutStore {
    private static final String PREFS = "gymgrid_days";
    private static final String KEY = "completed";

    private WorkoutStore() {}

    static Set<String> days(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return new HashSet<>(prefs.getStringSet(KEY, new HashSet<>()));
    }

    static boolean isDone(Context context, LocalDate date) {
        return days(context).contains(date.toString());
    }

    static void toggle(Context context, LocalDate date) {
        Set<String> days = days(context);
        String key = date.toString();
        if (!days.add(key)) days.remove(key);
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putStringSet(KEY, days).apply();
        GymWidgetProvider.refreshAll(context);
    }

    static int streak(Set<String> days) {
        LocalDate cursor = LocalDate.now();
        if (!days.contains(cursor.toString())) cursor = cursor.minusDays(1);
        int count = 0;
        while (days.contains(cursor.toString())) {
            count++;
            cursor = cursor.minusDays(1);
        }
        return count;
    }
}
