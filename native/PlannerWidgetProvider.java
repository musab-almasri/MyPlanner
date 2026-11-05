package com.myplanner.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlannerWidgetProvider extends AppWidgetProvider {

    private static final int[] ROWS = {
        R.id.w_row0, R.id.w_row1, R.id.w_row2, R.id.w_row3, R.id.w_row4
    };

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id : ids) {
            manager.updateAppWidget(id, build(context));
        }
    }

    static RemoteViews build(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.planner_widget);

        String title = "My Planner";
        String empty = "Open the app";
        String[] lines = new String[0];

        try {
            String raw = context.getSharedPreferences("planner_widget", Context.MODE_PRIVATE)
                .getString("data", null);
            if (raw != null) {
                JSONObject root = new JSONObject(raw);
                empty = root.optString("empty", empty);
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
                JSONArray days = root.optJSONArray("days");
                if (days != null) {
                    for (int i = 0; i < days.length(); i++) {
                        JSONObject day = days.getJSONObject(i);
                        if (today.equals(day.optString("d"))) {
                            title = day.optString("label", title);
                            JSONArray ev = day.optJSONArray("ev");
                            int n = ev == null ? 0 : ev.length();
                            int shown = n > ROWS.length ? ROWS.length - 1 : n;
                            int total = n > ROWS.length ? ROWS.length : n;
                            lines = new String[total];
                            for (int j = 0; j < shown; j++) {
                                JSONObject e = ev.getJSONObject(j);
                                String time = e.optString("t", "");
                                String name = e.optString("n", "");
                                lines[j] = time.length() > 0 ? time + "  " + name : "• " + name;
                            }
                            if (n > ROWS.length) {
                                lines[total - 1] = "+" + (n - shown);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            lines = new String[0];
        }

        views.setTextViewText(R.id.w_title, title);
        for (int i = 0; i < ROWS.length; i++) {
            if (i < lines.length) {
                views.setTextViewText(ROWS[i], lines[i]);
                views.setViewVisibility(ROWS[i], View.VISIBLE);
            } else {
                views.setViewVisibility(ROWS[i], View.GONE);
            }
        }
        views.setTextViewText(R.id.w_empty, empty);
        views.setViewVisibility(R.id.w_empty, lines.length == 0 ? View.VISIBLE : View.GONE);

        Intent launch = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (launch != null) {
            PendingIntent pi = PendingIntent.getActivity(
                context, 0, launch, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.widget_root, pi);
        }
        return views;
    }
}
