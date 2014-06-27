package pl.edu.amu.usos.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import pl.edu.amu.usos.AppConsts;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.content.Reminder;

public class ReminderWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        DateFormat mDateFormat = new SimpleDateFormat("HH:mm - EEEE, dd MMMM yyyy");

        List<Reminder> reminders = Reminder.findWithQuery(Reminder.class, "Select * from Reminder where " +
                "start > ? ORDER BY start ASC LIMIT 1", String.valueOf(System.currentTimeMillis()));

        Intent intent = new Intent(AppConsts.DASHBOARD_ACTION);
        PendingIntent pi = PendingIntent.getActivity(context, 1337, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        for (int id : appWidgetIds) {
            if (reminders.size() == 1) {
                Reminder remidner = reminders.get(0);
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reminder_widget_layout);
                views.setTextViewText(R.id.subject_label, remidner.title);
                views.setTextViewText(R.id.start_label, remidner.details);
                views.setTextViewText(R.id.end_label, mDateFormat.format(remidner.getStartDate()));
                views.setOnClickPendingIntent(R.id.widget_layout, pi);
                appWidgetManager.updateAppWidget(id, views);
            } else {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reminder_widget_layout);
                views.setTextViewText(R.id.subject_label, "Brak przypomnienia");
                views.setTextViewText(R.id.start_label, "");
                views.setTextViewText(R.id.end_label, "");
                views.setOnClickPendingIntent(R.id.widget_layout, pi);
                appWidgetManager.updateAppWidget(id, views);
            }
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
