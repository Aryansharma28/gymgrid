package dev.gymgrid.app;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends Activity {
    private static final int BG = Color.rgb(7, 20, 13);
    private static final int SURFACE = Color.rgb(17, 39, 26);
    private static final int SURFACE_ALT = Color.rgb(23, 48, 33);
    private static final int OUTLINE = Color.rgb(43, 75, 51);
    private static final int WHITE = Color.rgb(239, 249, 240);
    private static final int MUTED = Color.rgb(154, 183, 163);
    private static final int GREEN = GridPainter.GREEN;
    private static final int CORAL = GridPainter.MUTED;
    private YearMonth displayed = YearMonth.now();
    private TextView monthTitle;
    private TextView heroDate;
    private TextView heroStatus;
    private TextView dockStatus;
    private TextView yesButton;
    private TextView noButton;
    private TextView daysValue;
    private TextView streakValue;
    private MonthView monthView;
    private YearView yearView;
    private HeroArtwork heroArtwork;
    private ImageView heroIcon;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        configureWindow();
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(BG);
        root.setOnApplyWindowInsetsListener((view, insets) -> {
            int top;
            int bottom;
            if (Build.VERSION.SDK_INT >= 30) {
                android.graphics.Insets bars = insets.getInsets(WindowInsets.Type.systemBars());
                top = bars.top;
                bottom = bars.bottom;
            } else {
                top = insets.getSystemWindowInsetTop();
                bottom = insets.getSystemWindowInsetBottom();
            }
            view.setPadding(0, top, 0, bottom);
            return insets;
        });

        LinearLayout screen = new LinearLayout(this);
        screen.setOrientation(LinearLayout.VERTICAL);
        root.addView(screen, new FrameLayout.LayoutParams(-1, -1));
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setClipToPadding(false);
        scroll.setVerticalScrollBarEnabled(false);
        screen.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(20), dp(22), dp(20), dp(28));
        scroll.addView(content);

        buildHeader(content);
        buildHero(content);
        buildStats(content);
        buildMonth(content);
        buildYear(content);
        buildGuide(content);
        buildDock(screen);
        setContentView(root);
        if (Build.VERSION.SDK_INT >= 30) {
            android.view.WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) controller.setSystemBarsAppearance(0,
                    android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                            | android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        }
        root.requestApplyInsets();
        refresh(false);
    }

    @Override protected void onResume() {
        super.onResume();
        if (heroStatus != null) refresh(false);
    }

    private void configureWindow() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= 29) getWindow().setNavigationBarContrastEnforced(false);
        if (Build.VERSION.SDK_INT >= 30) {
            getWindow().setDecorFitsSystemWindows(false);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }

    private void buildHeader(LinearLayout content) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        content.addView(row, margins(-1, dp(42), 0, 0, 0, dp(34)));
        ImageView mark = new ImageView(this);
        mark.setImageResource(R.drawable.gym_dumbbell);
        mark.setBackground(round(SURFACE_ALT, 13, OUTLINE));
        mark.setPadding(dp(7), dp(7), dp(7), dp(7));
        row.addView(mark, new LinearLayout.LayoutParams(dp(42), dp(42)));
        TextView name = text("GymGrid", 19, WHITE, true);
        row.addView(name, margins(0, -2, dp(11), 0, 0, 0));
        TextView privateLabel = text("PRIVATE BY DESIGN", 10, MUTED, true);
        privateLabel.setLetterSpacing(.08f);
        privateLabel.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        row.addView(privateLabel, new LinearLayout.LayoutParams(0, -2, 1));

        TextView eyebrow = text("THE DAILY PRACTICE", 11, GREEN, true);
        eyebrow.setLetterSpacing(.16f);
        content.addView(eyebrow);
        TextView headline = text("Every day\nadds up.", 42, WHITE, true);
        headline.setLineSpacing(-dp(4), 1f);
        content.addView(headline, margins(-1, -2, 0, dp(9), 0, 0));
        TextView subtitle = text("One honest answer. A stronger habit.", 15, MUTED, false);
        content.addView(subtitle, margins(-1, -2, 0, dp(6), 0, dp(27)));
    }

    private void buildHero(LinearLayout content) {
        FrameLayout card = new FrameLayout(this);
        card.setBackground(round(Color.rgb(18, 54, 34), 30, Color.rgb(50, 105, 64)));
        card.setClipToOutline(true);
        card.setElevation(dp(5));
        content.addView(card, new LinearLayout.LayoutParams(-1, dp(190)));
        heroArtwork = new HeroArtwork();
        card.addView(heroArtwork, new FrameLayout.LayoutParams(-1, -1));
        heroIcon = new ImageView(this);
        heroIcon.setImageResource(R.drawable.gym_dumbbell);
        heroIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        heroIcon.setContentDescription("Playful green dumbbell illustration");
        FrameLayout.LayoutParams iconLayout = new FrameLayout.LayoutParams(
                dp(164), dp(164), Gravity.RIGHT | Gravity.BOTTOM);
        iconLayout.setMargins(0, 0, -dp(5), -dp(8));
        card.addView(heroIcon, iconLayout);
        LinearLayout words = new LinearLayout(this);
        words.setOrientation(LinearLayout.VERTICAL);
        words.setPadding(dp(23), dp(23), dp(23), dp(21));
        card.addView(words, new FrameLayout.LayoutParams(-1, -1));
        heroDate = text("", 11, Color.rgb(168, 226, 180), true);
        heroDate.setLetterSpacing(.10f);
        words.addView(heroDate);
        TextView question = text("Did you train\ntoday?", 27, WHITE, true);
        question.setLineSpacing(-dp(2), 1f);
        words.addView(question, margins(-1, -2, 0, dp(15), 0, 0));
        heroStatus = text("Ready when you are", 13, WHITE, true);
        heroStatus.setPadding(dp(12), dp(7), dp(12), dp(7));
        words.addView(heroStatus, new LinearLayout.LayoutParams(-2, -2));
    }

    private void buildStats(LinearLayout content) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        content.addView(row, margins(-1, dp(94), 0, dp(13), 0, dp(30)));
        LinearLayout daysCard = statCard("DAYS TRAINED");
        daysValue = text("0", 29, WHITE, true);
        daysCard.addView(daysValue);
        row.addView(daysCard, new LinearLayout.LayoutParams(0, -1, 1));
        LinearLayout streakCard = statCard("CURRENT STREAK");
        streakValue = text("0", 29, GREEN, true);
        streakCard.addView(streakValue);
        row.addView(streakCard, margins(0, -1, dp(12), 0, 0, 0));
        LinearLayout.LayoutParams first = (LinearLayout.LayoutParams) daysCard.getLayoutParams();
        first.width = 0;
        first.weight = 1;
        daysCard.setLayoutParams(first);
        LinearLayout.LayoutParams second = (LinearLayout.LayoutParams) streakCard.getLayoutParams();
        second.width = 0;
        second.weight = 1;
        streakCard.setLayoutParams(second);
    }

    private LinearLayout statCard(String title) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(18), dp(12), dp(18), dp(12));
        card.setBackground(round(SURFACE, 23, OUTLINE));
        TextView label = text(title, 10, MUTED, true);
        label.setLetterSpacing(.1f);
        card.addView(label);
        return card;
    }

    private void buildMonth(LinearLayout content) {
        LinearLayout card = sectionCard();
        content.addView(card);
        LinearLayout heading = new LinearLayout(this);
        heading.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(heading, new LinearLayout.LayoutParams(-1, dp(48)));
        monthTitle = text("", 21, WHITE, true);
        heading.addView(monthTitle, new LinearLayout.LayoutParams(0, -2, 1));
        TextView previous = navButton("‹");
        TextView next = navButton("›");
        heading.addView(previous, new LinearLayout.LayoutParams(dp(40), dp(40)));
        heading.addView(next, margins(dp(40), dp(40), dp(7), 0, 0, 0));
        previous.setOnClickListener(v -> changeMonth(-1));
        next.setOnClickListener(v -> changeMonth(1));
        monthView = new MonthView();
        monthView.setContentDescription("Monthly gym calendar. Tap a past day to change its answer.");
        card.addView(monthView, margins(-1, dp(304), 0, dp(7), 0, dp(15)));
        LinearLayout legend = new LinearLayout(this);
        legend.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(legend);
        legendItem(legend, GREEN, "YES");
        legendItem(legend, GridPainter.MUTED, "NO");
        legendItem(legend, GridPainter.UNANSWERED, "UNANSWERED");
    }

    private void buildYear(LinearLayout content) {
        LinearLayout card = sectionCard();
        content.addView(card, margins(-1, -2, 0, dp(14), 0, 0));
        TextView title = text("The long view", 21, WHITE, true);
        card.addView(title);
        TextView description = text("Your last 365 days, one square at a time.", 13, MUTED, false);
        card.addView(description, margins(-1, -2, 0, dp(3), 0, dp(15)));
        yearView = new YearView();
        yearView.setContentDescription("Year workout grid. Green days are workouts.");
        card.addView(yearView, new LinearLayout.LayoutParams(-1, dp(78)));
    }

    private void buildGuide(LinearLayout content) {
        LinearLayout card = sectionCard();
        content.addView(card, margins(-1, -2, 0, dp(14), 0, 0));
        card.addView(text("Put GymGrid on your home screen", 20, WHITE, true));
        TextView intro = text("Long press an empty space · Widgets · GymGrid", 13, MUTED, false);
        card.addView(intro, margins(-1, -2, 0, dp(8), 0, dp(11)));
        card.addView(text("01  Tap YES or NO to log today", 14, WHITE, true));
        card.addView(text("02  Long press the widget to resize it", 14, WHITE, true),
                margins(-1, -2, 0, dp(9), 0, 0));
        card.addView(text("03  Tap the grid to open this app", 14, WHITE, true),
                margins(-1, -2, 0, dp(9), 0, 0));
    }

    private void buildDock(LinearLayout screen) {
        LinearLayout dock = new LinearLayout(this);
        dock.setOrientation(LinearLayout.VERTICAL);
        dock.setPadding(dp(20), dp(13), dp(20), dp(15));
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(Color.rgb(15, 36, 24));
        shape.setCornerRadii(new float[]{dp(28), dp(28), dp(28), dp(28), 0, 0, 0, 0});
        shape.setStroke(dp(1), OUTLINE);
        dock.setBackground(shape);
        dock.setElevation(dp(14));
        screen.addView(dock, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout labelRow = new LinearLayout(this);
        labelRow.setGravity(Gravity.CENTER_VERTICAL);
        dock.addView(labelRow, margins(-1, dp(27), 0, 0, 0, dp(7)));
        TextView today = text("TODAY'S ANSWER", 11, MUTED, true);
        today.setLetterSpacing(.1f);
        labelRow.addView(today, new LinearLayout.LayoutParams(0, -2, 1));
        dockStatus = text("READY", 11, MUTED, true);
        dockStatus.setLetterSpacing(.08f);
        labelRow.addView(dockStatus);
        LinearLayout actions = new LinearLayout(this);
        dock.addView(actions, new LinearLayout.LayoutParams(-1, dp(56)));
        yesButton = actionButton("YES, I TRAINED");
        noButton = actionButton("NO, NOT TODAY");
        actions.addView(yesButton, new LinearLayout.LayoutParams(0, -1, 1));
        actions.addView(noButton, margins(0, -1, dp(10), 0, 0, 0));
        LinearLayout.LayoutParams noLayout = (LinearLayout.LayoutParams) noButton.getLayoutParams();
        noLayout.width = 0;
        noLayout.weight = 1;
        noButton.setLayoutParams(noLayout);
        yesButton.setContentDescription("Yes, I trained today");
        noButton.setContentDescription("No, I did not train today");
        yesButton.setOnClickListener(v -> answerToday(true, yesButton));
        noButton.setOnClickListener(v -> answerToday(false, noButton));
    }

    private void answerToday(boolean done, View button) {
        button.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        button.animate().scaleX(.96f).scaleY(.96f).setDuration(80).withEndAction(() ->
                button.animate().scaleX(1f).scaleY(1f).setDuration(230)
                        .setInterpolator(new OvershootInterpolator(1.5f)).start()).start();
        WorkoutStore.setAnswer(this, LocalDate.now(), done);
        refresh(true);
        animateAnswer(done);
    }

    private void animateAnswer(boolean done) {
        if (!ValueAnimator.areAnimatorsEnabled()) return;
        heroArtwork.pulse(done);
        heroIcon.animate().cancel();
        heroIcon.setPivotX(heroIcon.getWidth() * .5f);
        heroIcon.setPivotY(heroIcon.getHeight() * .55f);
        heroIcon.animate().translationY(done ? -dp(20) : dp(7))
                .rotation(done ? -14f : 9f).scaleX(1.08f).scaleY(1.08f)
                .setDuration(170).setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> heroIcon.animate().translationY(0).rotation(0)
                        .scaleX(1f).scaleY(1f).setDuration(300)
                        .setInterpolator(new OvershootInterpolator(1.7f)).start()).start();
        heroStatus.animate().cancel();
        heroStatus.setScaleX(.87f);
        heroStatus.setScaleY(.87f);
        heroStatus.animate().scaleX(1f).scaleY(1f).setDuration(320)
                .setInterpolator(new OvershootInterpolator(1.8f)).start();
    }

    private void changeMonth(int amount) {
        displayed = displayed.plusMonths(amount);
        refresh(false);
    }

    private void refresh(boolean animate) {
        Set<String> days = WorkoutStore.days(this);
        LocalDate today = LocalDate.now();
        heroDate.setText(today.format(DateTimeFormatter.ofPattern(
                "EEEE · d MMMM", Locale.getDefault())).toUpperCase(Locale.getDefault()));
        boolean answered = WorkoutStore.hasAnswer(this, today);
        boolean done = days.contains(today.toString());
        daysValue.setText(Integer.toString(days.size()));
        streakValue.setText(Integer.toString(WorkoutStore.streak(this)));
        monthTitle.setText(displayed.format(
                DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())));
        heroStatus.setText(!answered ? "Ready when you are" : done
                ? "Workout logged  ✓" : "No workout today");
        heroStatus.setTextColor(answered ? BG : WHITE);
        heroStatus.setBackground(round(!answered ? Color.rgb(48, 84, 58)
                : done ? GREEN : CORAL, 20, 0));
        dockStatus.setText(!answered ? "READY" : done ? "YES  ✓" : "NO  ✓");
        dockStatus.setTextColor(done ? GREEN : answered ? CORAL : MUTED);
        yesButton.setBackground(buttonBackground(done ? GREEN : Color.rgb(32, 78, 47), GREEN));
        yesButton.setTextColor(done ? BG : Color.rgb(194, 245, 205));
        noButton.setBackground(buttonBackground(answered && !done
                ? CORAL : Color.rgb(79, 45, 41), CORAL));
        noButton.setTextColor(answered && !done ? BG : Color.rgb(255, 194, 174));
        heroArtwork.setActive(done, animate);
        monthView.invalidate();
        yearView.invalidate();
    }

    private LinearLayout sectionCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(17), dp(18), dp(20));
        card.setBackground(round(SURFACE, 28, OUTLINE));
        return card;
    }

    private TextView actionButton(String label) {
        TextView view = text(label, 13, WHITE, true);
        view.setGravity(Gravity.CENTER);
        view.setClickable(true);
        view.setFocusable(true);
        view.setLetterSpacing(.03f);
        return view;
    }

    private TextView navButton(String label) {
        TextView view = text(label, 27, WHITE, false);
        view.setGravity(Gravity.CENTER);
        view.setBackground(buttonBackground(SURFACE_ALT, OUTLINE));
        view.setClickable(true);
        view.setFocusable(true);
        view.setContentDescription(label.equals("‹") ? "Previous month" : "Next month");
        return view;
    }

    private void legendItem(LinearLayout row, int color, String label) {
        View square = new View(this);
        square.setBackground(round(color, 4, 0));
        row.addView(square, new LinearLayout.LayoutParams(dp(12), dp(12)));
        TextView word = text(label, 10, MUTED, true);
        row.addView(word, margins(-2, -2, dp(5), 0, dp(14), 0));
    }

    private TextView text(String value, int sp, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setFontFeatureSettings("kern");
        view.setTypeface(android.graphics.Typeface.create(
                bold ? "sans-serif-medium" : "sans-serif", android.graphics.Typeface.NORMAL));
        return view;
    }

    private GradientDrawable round(int color, int radius, int stroke) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        if (stroke != 0) drawable.setStroke(dp(1), stroke);
        return drawable;
    }

    private RippleDrawable buttonBackground(int fill, int stroke) {
        GradientDrawable base = round(fill, 19, stroke);
        return new RippleDrawable(ColorStateList.valueOf(Color.rgb(149, 220, 164)), base, null);
    }

    private LinearLayout.LayoutParams margins(int width, int height,
                                               int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(left, top, right, bottom);
        return params;
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + .5f);
    }

    private final class HeroArtwork extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float glow = 0f;
        private float burst = 0f;
        private int burstColor = GREEN;
        HeroArtwork() { super(MainActivity.this); }

        void setActive(boolean active, boolean animate) {
            float target = active ? 1f : 0f;
            if (!animate) { glow = target; invalidate(); return; }
            ValueAnimator animator = ValueAnimator.ofFloat(glow, target);
            animator.setDuration(320);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(value -> {
                glow = (float) value.getAnimatedValue();
                invalidate();
            });
            animator.start();
        }

        void pulse(boolean trained) {
            burstColor = trained ? GREEN : CORAL;
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(480);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(value -> {
                burst = (float) value.getAnimatedValue();
                invalidate();
            });
            animator.start();
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float w = getWidth(), h = getHeight();
            paint.setShader(new RadialGradient(w * .94f, h * .12f, w * .8f,
                    new int[]{Color.argb((int) (105 + glow * 65), 55, 194, 99),
                            Color.argb(0, 26, 91, 43)}, null, Shader.TileMode.CLAMP));
            canvas.drawRect(0, 0, w, h, paint);
            paint.setShader(null);
            for (int i = 0; i < 3; i++) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(dp(1));
                paint.setColor(Color.argb(36 + (int) (glow * 18), 178, 239, 188));
                canvas.drawCircle(w * .92f, h * .17f, dp(41 + i * 32), paint);
            }
            if (burst > 0f && burst < 1f) {
                float centerX = w * .79f;
                float centerY = h * .54f;
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(dp(3));
                paint.setColor(Color.argb((int) (190 * (1f - burst)),
                        Color.red(burstColor), Color.green(burstColor), Color.blue(burstColor)));
                canvas.drawCircle(centerX, centerY, dp(28 + 85 * burst), paint);
                paint.setStrokeWidth(dp(1.5f));
                canvas.drawCircle(centerX, centerY, dp(10 + 115 * burst), paint);
            }
            paint.setStyle(Paint.Style.FILL);
        }
    }

    private final class MonthView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private LocalDate pulseDate;
        private float pulse;
        MonthView() { super(MainActivity.this); setClickable(true); }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Set<String> yes = WorkoutStore.days(MainActivity.this);
            Set<String> answered = WorkoutStore.answered(MainActivity.this);
            int offset = displayed.atDay(1).getDayOfWeek().getValue() - 1;
            int rows = (offset + displayed.lengthOfMonth() + 6) / 7;
            float head = dp(31);
            float cellW = getWidth() / 7f;
            float cellH = (getHeight() - head) / rows;
            String[] names = {"M", "T", "W", "T", "F", "S", "S"};
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(android.graphics.Typeface.create("sans-serif-medium", 0));
            paint.setTextSize(dp(11));
            paint.setColor(MUTED);
            for (int col = 0; col < 7; col++) {
                canvas.drawText(names[col], (col + .5f) * cellW, dp(17), paint);
            }
            for (int day = 1; day <= displayed.lengthOfMonth(); day++) {
                int position = offset + day - 1;
                int col = position % 7;
                int row = position / 7;
                LocalDate date = displayed.atDay(day);
                float centerX = (col + .5f) * cellW;
                float centerY = head + (row + .5f) * cellH;
                float size = Math.min(cellW, cellH) * .77f;
                if (date.equals(pulseDate)) size *= 1 + .17f * (float) Math.sin(Math.PI * pulse);
                float l = centerX - size / 2, t = centerY - size / 2;
                float r = centerX + size / 2, b = centerY + size / 2;
                int color = GridPainter.dayColor(date, yes, answered);
                paint.setColor(color);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(l, t, r, b, dp(12), dp(12), paint);
                if (date.equals(LocalDate.now())) {
                    paint.setColor(color == CORAL ? CORAL : Color.rgb(134, 224, 160));
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(dp(1.5f));
                    canvas.drawRoundRect(l - dp(2), t - dp(2), r + dp(2), b + dp(2),
                            dp(14), dp(14), paint);
                }
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(color == GREEN || color == CORAL ? BG
                        : date.isAfter(LocalDate.now()) ? MUTED : WHITE);
                paint.setTextSize(Math.min(dp(14), size * .37f));
                paint.setTypeface(android.graphics.Typeface.create("sans-serif-medium", 0));
                canvas.drawText(Integer.toString(day), centerX,
                        centerY - (paint.ascent() + paint.descent()) / 2f, paint);
            }
            paint.setTextAlign(Paint.Align.LEFT);
        }

        @Override public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) return true;
            if (event.getAction() != MotionEvent.ACTION_UP) return true;
            float head = dp(31);
            if (event.getY() < head || event.getX() < 0 || event.getX() >= getWidth()) return true;
            int offset = displayed.atDay(1).getDayOfWeek().getValue() - 1;
            int rows = (offset + displayed.lengthOfMonth() + 6) / 7;
            int col = Math.min(6, (int) (event.getX() / (getWidth() / 7f)));
            int row = Math.min(rows - 1, (int) ((event.getY() - head) /
                    ((getHeight() - head) / rows)));
            int day = row * 7 + col - offset + 1;
            if (day < 1 || day > displayed.lengthOfMonth()) return true;
            LocalDate date = displayed.atDay(day);
            if (date.isAfter(LocalDate.now())) return true;
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            WorkoutStore.toggle(MainActivity.this, date);
            pulseDate = date;
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(300);
            animator.addUpdateListener(value -> {
                pulse = (float) value.getAnimatedValue();
                invalidate();
            });
            animator.start();
            performClick();
            refresh(true);
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
                    WorkoutStore.answered(MainActivity.this),
                    0, dp(5), getWidth(), getHeight() - dp(10));
        }
    }
}
