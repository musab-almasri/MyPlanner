package com.myplanner.app;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "PlannerWidget")
public class PlannerWidgetPlugin extends Plugin {

    @PluginMethod
    public void update(PluginCall call) {
        Context context = getContext();
        String data = call.getString("data", "{}");
        context.getSharedPreferences("planner_widget", Context.MODE_PRIVATE)
            .edit().putString("data", data).apply();

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, PlannerWidgetProvider.class));
        if (ids != null && ids.length > 0) {
            manager.updateAppWidget(ids, PlannerWidgetProvider.build(context));
        }
        call.resolve();
    }
}
