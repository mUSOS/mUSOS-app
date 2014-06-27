package pl.edu.amu.usos.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pl.edu.amu.usos.AppConsts;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.content.AppPreferences;

public class IncomingWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        AppPreferences appPrefs = new AppPreferences(context);
        DateFormat mDateFormat = new SimpleDateFormat("HH:mm - EEEE, dd MMMM yyyy");

        Intent intent = new Intent(AppConsts.DASHBOARD_ACTION);
        PendingIntent pi = PendingIntent.getActivity(context, 1337, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        for (int id : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_incoming_layout);
            views.setTextViewText(R.id.subject_label, appPrefs.getCourseName());
            views.setTextViewText(R.id.start_label, mDateFormat.format(appPrefs.getCourseStart()));
            views.setTextViewText(R.id.end_label, mDateFormat.format(appPrefs.getCourseEnd()));
            views.setOnClickPendingIntent(R.id.widget_layout, pi);
            appWidgetManager.updateAppWidget(id, views);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
