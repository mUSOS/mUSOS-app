package pl.edu.amu.usos.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String START_ALARM = "pl.edu.amu.usos.START_ALARM_B";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (START_ALARM.equals(action)) {
            long id = intent.getLongExtra(NotificationService.EXTRA_ID, -1);
            Intent serviceIntent = new Intent(NotificationService.ACTION_FIRE_ALARM);
            serviceIntent.putExtra(NotificationService.EXTRA_ID, id);
            context.startService(serviceIntent);
        }
    }
}
