package dev.gymgrid.app;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends Activity {
    private YearMonth displayed = YearMonth.now();
    private TextView monthTitle;
    private TextView summary;
    private MonthView monthView;
    private YearView yearView;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(GridPainter.BACKGROUND);
        getWindow().setNavigationBarColor(GridPainter.BACKGROUND);
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(GridPainter.BACKGROUND);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(24), dp(28), dp(24), dp(32));
        scroll.addView(page);

        TextView eyebrow = label("YOUR TRAINING, ONE DAY AT A TIME", 12, GridPainter.GREEN, true);
        page.addView(eyebrow);
        TextView title = label("GymGrid", 36, GridPainter.TEXT, true);
        title.setPadding(0, dp(7), 0, dp(8));
        page.addView(title);
        summary = label("", 16, GridPainter.SUBTEXT, false);
        page.addView(summary);

        LinearLayout heading = new LinearLayout(this);
        heading.setGravity(Gravity.CENTER_VERTICAL);
        heading.setPadding(0, dp(28), 0, dp(12));
        page.addView(heading);
        monthTitle = label("", 22, GridPainter.TEXT, true);
        heading.addView(monthTitle, new LinearLayout.LayoutParams(0, dp(48), 1));
        Button prev = arrow("‹");
        Button next = arrow("›");
        heading.addView(prev, new LinearLayout.LayoutParams(dp(48), dp(48)));
        heading.addView(next, new LinearLayout.LayoutParams(dp(48), dp(48)));
        prev.setOnClickListener(v -> changeMonth(-1));
        next.setOnClickListener(v -> changeMonth(1));

        monthView = new MonthView();
        page.addView(monthView, new LinearLayout.LayoutParams(-1, dp(340)));
        TextView hint = label("Tap a day to check or uncheck it.", 14, GridPainter.SUBTEXT, false);
        hint.setPadding(0, dp(12), 0, dp(20));
        page.addView(hint);

        TextView yearTitle = label("Past year", 22, GridPainter.TEXT, true);
        page.addView(yearTitle);
        yearView = new YearView();
        page.addView(yearView, new LinearLayout.LayoutParams(-1, dp(96)));
        TextView legend = label("Unchecked  ▪     ■  Checked", 12, GridPainter.SUBTEXT, false);
        page.addView(legend);
        setContentView(scroll);
        refresh();
    }

    @Override protected void onResume() {
        super.onResume();
        if (summary != null) refresh();
    }

    private void changeMonth(int delta) {
        displayed = displayed.plusMonths(delta);
        refresh();
    }

    private void refresh() {
        Set<String> days = WorkoutStore.days(this);
        int streak = WorkoutStore.streak(days);
        summary.setText(days.size() + " workouts logged  ·  " + streak + " day streak");
        monthTitle.setText(displayed.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())));
        monthView.invalidate();
        yearView.invalidate();
    }

    private TextView label(String value, int sp, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) view.setTypeface(null, android.graphics.Typeface.BOLD);
        return view;
    }

    private Button arrow(String symbol) {
        Button button = new Button(this);
        button.setText(symbol);
        button.setTextColor(GridPainter.TEXT);
        button.setTextSize(24);
        button.setAllCaps(false);
        button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(GridPainter.MUTED));
        return button;
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + .5f);
    }

    private final class MonthView extends View {
        MonthView() { super(MainActivity.this); }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            GridPainter.drawMonth(canvas, WorkoutStore.days(MainActivity.this), displayed,
                    0, 0, getWidth(), getHeight(), true);
        }

        @Override public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() != MotionEvent.ACTION_UP) return true;
            float header = getHeight() * .15f;
            if (event.getY() < header) return true;
            int offset = displayed.atDay(1).getDayOfWeek().getValue() - 1;
            int rows = (offset + displayed.lengthOfMonth() + 6) / 7;
            int col = Math.min(6, (int) (event.getX() / (getWidth() / 7f)));
            int row = Math.min(rows - 1, (int) ((event.getY() - header) /
                    ((getHeight() - header) / rows)));
            int day = row * 7 + col - offset + 1;
            if (day >= 1 && day <= displayed.lengthOfMonth()) {
                LocalDate date = displayed.atDay(day);
                if (!date.isAfter(LocalDate.now())) {
                    WorkoutStore.toggle(MainActivity.this, date);
                    performClick();
                    refresh();
                }
            }
            return true;
        }

        @Override public boolean performClick() {
            super.performClick();
            return true;
        }
    }

    private final class YearView extends View {
        YearView() { super(MainActivity.this); }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            GridPainter.drawYear(canvas, WorkoutStore.days(MainActivity.this),
                    0, dp(12), getWidth(), getHeight() - dp(18));
        }
    }
}
