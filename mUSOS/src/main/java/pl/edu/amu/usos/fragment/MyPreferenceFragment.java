package pl.edu.amu.usos.fragment;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.provider.CalendarContract.Calendars;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import pl.edu.amu.usos.R;
import pl.edu.amu.usos.content.AppPreferences;
import pl.edu.amu.usos.preference.PreferenceFragment;

public class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    public static Fragment newInstance() {
        return new MyPreferenceFragment();
    }

    private AppPreferences mAppPreferences;
    private List<CalendarItem> mCalendars;

    private static class CalendarItem {

        public int id;
        public String name;
        public String accountName;

        private CalendarItem(int id, String name, String accountName) {
            this.id = id;
            this.name = name;
            this.accountName = accountName;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAppPreferences = new AppPreferences(getActivity());
        addPreferencesFromResource(R.xml.preferences);

        final PreferenceCategory group = (PreferenceCategory) getPreferenceScreen()
                .findPreference("calendar_key");

        final boolean hasIcs = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        if (hasIcs) {
            addCalendarPrefs(group);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean isEnable = (Boolean) newValue;
        final String id = preference.getKey();

        if (isEnable) {
            mAppPreferences.edit().putCalendarId(id).commit();
        } else {
            mAppPreferences.edit().removeCalendarId(id).commit();
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void addCalendarPrefs(PreferenceCategory group) {
        mCalendars = getCalendars();

        CheckBoxPreference preference;
        for (CalendarItem item : mCalendars) {
            preference = new CheckBoxPreference(getActivity());
            final String strId = String.valueOf(item.id);
            preference.setKey(strId);
            preference.setTitle(item.name);
            preference.setSummary(item.accountName);
            preference.setOnPreferenceChangeListener(this);
            preference.setChecked(mAppPreferences.isIdEnable(strId));
            group.addPreference(preference);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private List<CalendarItem> getCalendars() {
        List<CalendarItem> results = new ArrayList<CalendarItem>();
        final ContentResolver cr = getActivity().getContentResolver();
        final Cursor c = cr.query(Calendars.CONTENT_URI, new String[]{Calendars._ID, Calendars.NAME,
                        Calendars.ACCOUNT_NAME},
                null, null, null);

        try {
            if (c != null) {
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    final int id = c.getInt(0);
                    final String name = c.getString(1);
                    final String account = c .getString(2);
                    results.add(new CalendarItem(id, name, account));
                }
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return results;
    }
}
