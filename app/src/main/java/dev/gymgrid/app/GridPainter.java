package dev.gymgrid.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;

final class GridPainter {
    static final int BACKGROUND = Color.rgb(12, 31, 23);
    static final int MUTED = Color.rgb(48, 73, 59);
    static final int GREEN = Color.rgb(67, 204, 117);
    static final int TEXT = Color.rgb(236, 247, 237);
    static final int SUBTEXT = Color.rgb(158, 184, 165);
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private GridPainter() {}

    static Bitmap widget(Set<String> days, boolean year, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(BACKGROUND);
        float pad = width * (year ? .055f : .08f);
        text(canvas, "GYMGRID", pad, height * .16f, width * .052f, GREEN, true);
        int streak = WorkoutStore.streak(days);
        text(canvas, streak + " day streak", pad,
                height * .27f, width * .047f, TEXT, false);
        if (year) {
            drawYear(canvas, days, pad, height * .39f, width - 2 * pad, height * .43f);
        } else {
            YearMonth month = YearMonth.now();
            drawMonth(canvas, days, month, pad, height * .39f,
                    width - 2 * pad, height * .50f, false);
        }
        return bitmap;
    }

    static void drawMonth(Canvas canvas, Set<String> days, YearMonth month,
                          float left, float top, float width, float height, boolean labels) {
        int offset = month.atDay(1).getDayOfWeek().getValue() - 1;
        int rows = (offset + month.lengthOfMonth() + 6) / 7;
        float header = labels ? height * .15f : 0f;
        float cellW = width / 7f;
        float cellH = (height - header) / rows;
        if (labels) {
            String[] names = {"M", "T", "W", "T", "F", "S", "S"};
            for (int i = 0; i < 7; i++) {
                text(canvas, names[i], left + i * cellW + cellW * .40f,
                        top + header * .74f, Math.min(cellW * .34f, 15f), SUBTEXT, false);
            }
        }
        float inset = Math.min(cellW, cellH) * .13f;
        for (int day = 1; day <= month.lengthOfMonth(); day++) {
            int position = offset + day - 1;
            LocalDate date = month.atDay(day);
            int color = date.isAfter(LocalDate.now()) ? Color.rgb(26, 48, 35)
                    : days.contains(date.toString()) ? GREEN : MUTED;
            float x = left + (position % 7) * cellW + inset;
            float y = top + header + (position / 7) * cellH + inset;
            float size = Math.min(cellW, cellH) - 2 * inset;
            rectangle(canvas, x, y, x + size, y + size, size * .18f, color);
            if (labels && cellW >= 31f) {
                text(canvas, Integer.toString(day), x + size * .28f,
                        y + size * .69f, size * .42f, color == GREEN ? BACKGROUND : TEXT, false);
            }
        }
    }

    static void drawYear(Canvas canvas, Set<String> days, float left, float top,
                         float width, float height) {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(364);
        start = start.minusDays(start.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
        int weeks = (int) ((today.toEpochDay() - start.toEpochDay()) / 7) + 1;
        float cell = Math.min(width / weeks, height / 7f);
        float gap = Math.max(1f, cell * .18f);
        for (int week = 0; week < weeks; week++) {
            for (int row = 0; row < 7; row++) {
                LocalDate date = start.plusDays((long) week * 7 + row);
                int color = date.isAfter(today) ? Color.rgb(26, 48, 35)
                        : days.contains(date.toString()) ? GREEN : MUTED;
                float x = left + week * (width / weeks) + gap / 2;
                float y = top + row * (height / 7f) + gap / 2;
                rectangle(canvas, x, y, x + cell - gap, y + cell - gap, cell * .16f, color);
            }
        }
    }

    static void text(Canvas canvas, String value, float x, float baseline,
                     float size, int color, boolean bold) {
        paint.setColor(color);
        paint.setTextSize(size);
        paint.setTypeface(bold ? android.graphics.Typeface.DEFAULT_BOLD
                : android.graphics.Typeface.DEFAULT);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(value, x, baseline, paint);
    }

    static void rectangle(Canvas canvas, float l, float t, float r, float b,
                          float radius, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(l, t, r, b), radius, radius, paint);
    }
}
