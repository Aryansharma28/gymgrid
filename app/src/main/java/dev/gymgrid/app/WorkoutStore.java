package dev.gymgrid.app;

import android.content.Context;
import android.content.SharedPreferences;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

final class WorkoutStore {
    private static final String PREFS = "gymgrid_days";
    private static final String KEY = "completed";
    private static final String ANSWERED = "answered";

    private WorkoutStore() {}

    static Set<String> days(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return new HashSet<>(prefs.getStringSet(KEY, new HashSet<>()));
    }

    static Set<String> answered(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Set<String> result = new HashSet<>(prefs.getStringSet(ANSWERED, new HashSet<>()));
        // Days saved by the first version were all explicit Yes answers.
        result.addAll(days(context));
        return result;
    }

    static boolean isDone(Context context, LocalDate date) {
        return days(context).contains(date.toString());
    }

    static boolean hasAnswer(Context context, LocalDate date) {
        return answered(context).contains(date.toString());
    }

    static void setAnswer(Context context, LocalDate date, boolean done) {
        if (date.isAfter(LocalDate.now())) return;
        Set<String> days = days(context);
        Set<String> answered = answered(context);
        String key = date.toString();
        if (done) days.add(key);
        else days.remove(key);
        answered.add(key);
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putStringSet(KEY, days).putStringSet(ANSWERED, answered).apply();
        GymWidgetProvider.refreshAll(context);
    }

    static void toggle(Context context, LocalDate date) {
        setAnswer(context, date, !isDone(context, date));
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
