package pl.edu.amu.usos.fragment;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.edu.amu.usos.DateTimePickerDialog;
import pl.edu.amu.usos.MainActivity;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.content.Reminder;
import pl.edu.amu.usos.service.NotificationService;
import pl.edu.amu.usos.widget.ReminderWidget;

public class ReminderFragment extends BaseFragment implements DateTimePickerDialog.OnDateSetListener {

    public static Fragment newInstance() {
        return new ReminderFragment();
    }

    @InjectView(R.id.reminder_title)
    TextView mTitle;

    @InjectView(R.id.reminder_details)
    TextView mDetails;
    @InjectView(R.id.start_date)
    Button mStartBtn;
    @InjectView(R.id.notif_date)
    Button mNotifBtn;
    @InjectView(R.id.google_checkbox)
    CheckBox mCalendarBox;

    private long mStartDate = 0;
    private long mNotifDate = 0;
    private SimpleDateFormat mDateFormat;

    @OnClick(R.id.start_date)
    void onStartDateClick() {
        MainActivity activtiy = MainActivity.fromActivity(getActivity());
        DateTimePickerDialog picker = DateTimePickerDialog.newInstance(R.id.start_date, mStartDate);
        picker.setListener(this);
        picker.show(activtiy.getSupportFragmentManager(), null);
    }

    @OnClick(R.id.notif_date)
    void onNotifDateClick() {
        MainActivity activtiy = MainActivity.fromActivity(getActivity());
        DateTimePickerDialog picker = DateTimePickerDialog.newInstance(R.id.notif_date, mNotifDate);
        picker.setListener(this);
        picker.show(activtiy.getSupportFragmentManager(), null);
    }

    @OnClick(R.id.add_button)
    void onAddClick() {
        String title = mTitle.getText().toString();
        String details = mDetails.getText().toString();
        Reminder reminder = new Reminder(getActivity(), title, details, mStartDate, mNotifDate);
        reminder.save();

        Toast.makeText(getActivity(), "Dodano przypomnienie", Toast.LENGTH_SHORT).show();
        mTitle.setText("");
        mDetails.setText("");

        mStartDate = System.currentTimeMillis();
        mNotifDate = 0;
        refreshButtons();

        Intent intent = new Intent(NotificationService.ACTION_SET_ALARM);
        getActivity().startService(intent);

        final boolean hasIcs = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        if (hasIcs && mAppPrefs.isCalendarEnable()) {
            addEvent(reminder.getStartDate().getTime(), reminder.title, reminder.details);
        }

        updateWidget();
    }

    private void updateWidget() {
        ComponentName thisWidget = new ComponentName(getActivity(),
                ReminderWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
        int[] ids = appWidgetManager.getAppWidgetIds(thisWidget);
        Intent intent = new Intent(getActivity(), ReminderWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        getActivity().sendBroadcast(intent);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void addEvent(final long start, final String title, final String description) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver cr = getActivity().getContentResolver();
                ContentValues values = new ContentValues();
                values.put(Events.DTSTART, start);
                values.put(Events.DTEND, start + 60L * 60L * 1000L);
                values.put(Events.TITLE, title);
                values.put(Events.DESCRIPTION, description);
                values.put(Events.EVENT_TIMEZONE, "Europe/Warsaw");

                final Set<String> ids = mAppPrefs.getCalendarIds();
                for (String calId : ids) {
                    values.put(Events.CALENDAR_ID, calId);
                    Uri uri = cr.insert(Events.CONTENT_URI, values);
                    Log.d("ttt", "Event added: " + uri.toString() + " to calendar: " + calId);
                }
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        mStartDate = System.currentTimeMillis();
        final boolean hasIcs = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        mCalendarBox.setEnabled(hasIcs && mAppPrefs.isCalendarEnable());

        refreshButtons();
    }

    @Override
    public void onDateSet(int id, long time) {
        switch (id) {
            case R.id.start_date:
                mStartDate = time;
                break;
            case R.id.notif_date:
                mNotifDate = time;
                break;
        }

        refreshButtons();
    }

    private void refreshButtons() {
        String date = mDateFormat.format(new Date(mStartDate));
        mStartBtn.setText("Start: " + date);

        if (mNotifDate > 0) {
            String notifDate = mDateFormat.format(new Date(mNotifDate));
            mNotifBtn.setText("Przypomnienie: " + notifDate);
        } else {
            mNotifBtn.setText("Przypomnienie: ---");
        }
    }
}
