package pl.edu.amu.usos.content;

import android.content.Context;

import com.orm.SugarRecord;

import java.util.Date;

public class Reminder extends SugarRecord<Reminder> {

    public String title;
    public String details;
    public long start;
    public long notification;

    public Reminder(Context context) {
        super(context);
    }

    public Reminder(Context context, String title, String details, long startDate,
                    long notifDate) {
        super(context);
        this.title = title;
        this.details = details;
        this.start = startDate;
        this.notification = notifDate;
    }

    public Date getStartDate() {
        return new Date(start);
    }

    public Date getNotifDate() {
        return new Date(notification);
    }
}
