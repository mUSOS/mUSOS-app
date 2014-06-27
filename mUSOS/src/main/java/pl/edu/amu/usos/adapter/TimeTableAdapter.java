package pl.edu.amu.usos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;
import java.util.Date;

import pl.edu.amu.usos.fragment.SingleDayFragment;

public class TimeTableAdapter extends FragmentStatePagerAdapter {

    private Date mStartDate;
    private Calendar mCalendar;

    public TimeTableAdapter(FragmentManager fm, Date startDate) {
        super(fm);
        mStartDate = startDate;
        mCalendar = Calendar.getInstance();
    }

    @Override
    public Fragment getItem(int position) {
        mCalendar.setTime(mStartDate);
        mCalendar.add(Calendar.DAY_OF_YEAR, position);
        return SingleDayFragment.newInstance(mCalendar.getTimeInMillis());
    }

    @Override
    public int getCount() {
        return 100;
    }
}
