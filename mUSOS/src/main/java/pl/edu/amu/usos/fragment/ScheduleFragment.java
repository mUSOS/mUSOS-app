package pl.edu.amu.usos.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.adapter.TimeTableAdapter;
import pl.edu.amu.usos.content.AppPreferences;
import pl.edu.amu.usos.content.SingleCourse;
import pl.edu.amu.usos.loaders.TimeTableTask;

public class ScheduleFragment extends BaseFragment implements TimeTableTask.OnTimeTableListener, ViewPager.OnPageChangeListener {

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private class SubjectTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = getProgressDialog();
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            final Set<String> calendarIds = mAppPrefs.getCalendarIds();
            final ContentResolver cr = getActivity().getContentResolver();
            List<SingleCourse> courses = SingleCourse.listAll(SingleCourse.class);

            for (String calId : calendarIds) {
                for (SingleCourse course : courses) {
                    cr.insert(CalendarContract.Events.CONTENT_URI, course.getContentValues(calId));
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            Toast.makeText(getActivity(), "Dodano zajęcia do kalendarza", Toast.LENGTH_LONG).show();
        }
    }

    @InjectView(R.id.progress_bar)
    View mProgressBar;
    @InjectView(R.id.view_pager)
    ViewPager mViewPager;
    @InjectView(R.id.date_label)
    TextView mDateLabel;

    private AlertDialog mDialog;

    private Calendar mCalendar;
    private Date mStartDate;
    private SimpleDateFormat mDateFormat;
    private TimeTableAdapter mAdapter;
    private AppPreferences mAppPrefs;

    public static Fragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.schedule_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCalendar = Calendar.getInstance();
        mAppPrefs = new AppPreferences(getActivity());
        mStartDate = mCalendar.getTime();
        mDateFormat = new SimpleDateFormat("EEEE - dd/MM/yyyy");
        setHasOptionsMenu(true);
        mAdapter = new TimeTableAdapter(getChildFragmentManager(), mStartDate);
        if (mAppPrefs.needTimeTableRefresh()) {
            showProgress(true);
            new TimeTableTask(getActivity(), this).execute();
        } else {
            showTimeTable();
        }
    }

    private void showTimeTable() {
        showProgress(false);
        mDateLabel.setText(mDateFormat.format(mStartDate));
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mViewPager.setVisibility(show ? View.GONE : View.VISIBLE);
        mDateLabel.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onTimeTableLoaded() {
        showTimeTable();
        mAppPrefs.edit().setTimeTableSync(System.currentTimeMillis()).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final boolean hasIcs = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        if (hasIcs) {
            inflater.inflate(R.menu.schedule_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            new SubjectTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mCalendar.setTime(mStartDate);
        mCalendar.add(Calendar.DAY_OF_YEAR, position);
        mDateLabel.setText(mDateFormat.format(mCalendar.getTime()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private AlertDialog getProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(R.string.sync_title);
        dialog.setMessage(getString(R.string.sync_msg));
        return dialog;
    }
}
