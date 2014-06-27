package pl.edu.amu.usos.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.List;

import pl.edu.amu.usos.R;
import pl.edu.amu.usos.content.Reminder;
import pl.edu.amu.usos.helper.IntentHelper;

public class NotificationService extends IntentService {

    public static final String ACTION_SET_ALARM = "pl.edu.amu.usos.ACTION_SET_ALARM";
    public static final String ACTION_FIRE_ALARM = "pl.edu.amu.usos.ACTION_FIRE_ALARM";

    public static final String EXTRA_ID = "extra_id";
    private static final int REQUEST_FIRE = 0;
    private static final int NOTIFICATION_ID = 0;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String action = intent.getAction();
        if (ACTION_SET_ALARM.equals(action)) {
            setIncomingAlarm();
        } else if (ACTION_FIRE_ALARM.equals(action)) {
            long id = intent.getLongExtra(EXTRA_ID, -1);
            showNotification(id);
        }
    }

    private void setIncomingAlarm() {
        final String time = String.valueOf(System.currentTimeMillis());
        List<Reminder> reminders = Reminder.findWithQuery(Reminder.class, "Select * from Reminder where " +
                "notification > ? ORDER BY notification ASC LIMIT 1", time);
        if (reminders.size() == 1) {
            Reminder reminder = reminders.get(0);
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(NotificationReceiver.START_ALARM);
            intent.putExtra(EXTRA_ID, reminder.getId());
            PendingIntent pi = PendingIntent.getBroadcast(this, REQUEST_FIRE, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            am.set(AlarmManager.RTC_WAKEUP, reminder.notification, pi);
        }
    }

    private void showNotification(long id) {
        Reminder reminder = Reminder.findById(Reminder.class, id);
        if (reminder != null) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String time = dateFormat.format(reminder.getStartDate());

            Intent intent = IntentHelper.getDashboardIntent();
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            Notification notification = builder.setAutoCancel(true)
                    .setContentTitle(time + " - " + reminder.title)
                    .setContentText(reminder.details)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .setWhen(reminder.notification)
                    .build();

            nm.notify(NOTIFICATION_ID, notification);
        }

        setIncomingAlarm();
    }
}
