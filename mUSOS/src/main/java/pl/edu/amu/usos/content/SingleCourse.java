package pl.edu.amu.usos.content;

import android.content.ContentValues;
import android.content.Context;
import android.provider.CalendarContract.Events;

import com.orm.SugarRecord;

import java.util.Calendar;

public class SingleCourse extends SugarRecord<SingleCourse> {

    public static final int EVERY_WEEK = 0;
    public static final int EVERY_ODD_WEEK = 1;
    public static final int EVERY_EVEN_WEEK = 2;

    public String name;
    public int day;
    public int mode;
    public long start;
    public long end;
    public String course_id;
    public String building_name;
    public long building_id;
    public String room_number;
    public long room_id;

    public SingleCourse(Context context) {
        super(context);
    }

    public SingleCourse(Context context, String name, int day, int mode, long start, long end,
                        String course_id, String building_name, long building_id,
                        String room_number, long room_id) {
        super(context);
        this.name = name;
        this.day = day;
        this.mode = mode;
        this.start = start;
        this.end = end;
        this.course_id = course_id;
        this.building_name = building_name;
        this.building_id = building_id;
        this.room_number = room_number;
        this.room_id = room_id;
    }

    public ContentValues getContentValues(String calendarId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start);

        final ContentValues cv = new ContentValues();

        final int startDay = calendar.get(Calendar.DAY_OF_WEEK);
        final int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int startMinute = calendar.get(Calendar.MINUTE);

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, startDay);
        calendar.set(Calendar.HOUR_OF_DAY, startHour);
        calendar.set(Calendar.MINUTE, startMinute);
        final int week = calendar.get(Calendar.WEEK_OF_YEAR);
        switch (this.mode) {
            case EVERY_WEEK:
                cv.put(Events.RRULE, "FREQ=WEEKLY;COUNT=10;WKST=MO");
                break;
            case EVERY_ODD_WEEK:
                if (week % 2 == 0) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                }
                cv.put(Events.RRULE, "FREQ=WEEKLY;COUNT=5;WKST=MO;INTERVAL=2");
                break;
            case EVERY_EVEN_WEEK:
                if (week % 2 != 0) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                }
                cv.put(Events.RRULE, "FREQ=WEEKLY;COUNT=5;WKST=MO;INTERVAL=2");
        }

        cv.put(Events.TITLE, this.name);
        cv.put(Events.CALENDAR_ID, calendarId);
        cv.put(Events.EVENT_TIMEZONE, "Europe/Warsaw");
        cv.put(Events.DTSTART, calendar.getTimeInMillis());
        cv.put(Events.DTEND, calendar.getTimeInMillis() + (end - start));

        return cv;
    }

}
