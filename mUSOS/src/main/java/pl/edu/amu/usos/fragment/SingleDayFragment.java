package pl.edu.amu.usos.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.adapter.DayAdapter;
import pl.edu.amu.usos.content.Reminder;
import pl.edu.amu.usos.content.SingleCourse;

public class SingleDayFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static final String ARG_DATE = "arg_date";

    @InjectView(R.id.list_view)
    ListView mListView;
    @InjectView(R.id.empty_view)
    View mEmptyView;

    private DayAdapter mAdapter;

    public static Fragment newInstance(long time) {
        Fragment fragment = new SingleDayFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE, time);
        fragment.setArguments(args);
        return fragment;
    }

    private Calendar mCalendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mCalendar = Calendar.getInstance();
        long time = args.getLong(ARG_DATE);
        mCalendar.setTimeInMillis(time);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.single_day_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int day = mCalendar.get(Calendar.DAY_OF_WEEK);
        final int week = mCalendar.get(Calendar.WEEK_OF_YEAR);
        final boolean isEven = week % 2 == 0;

        List<SingleCourse> singleCourses = new ArrayList<SingleCourse>();
        if (!isHoliday()) {
            singleCourses = Reminder.findWithQuery(SingleCourse.class, "Select * from " +
                "SINGLE_COURSE where day = ? AND (mode = ? OR mode = ?) ORDER BY start ASC",
                String.valueOf(day), String.valueOf(SingleCourse.EVERY_WEEK),
                String.valueOf(isEven ? SingleCourse.EVERY_EVEN_WEEK : SingleCourse.EVERY_ODD_WEEK));
        }

        mAdapter = new DayAdapter(getActivity(), singleCourses);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        boolean isEmpty = singleCourses.size() == 0;
        mListView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        mEmptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private boolean isHoliday() {
        final int month = mCalendar.get(Calendar.MONTH);
        if (month != Calendar.MAY) {
            return false;
        }

        final int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        if (day != 1 && day != 3) {
            return false;
        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final SingleCourse item = (SingleCourse) mAdapter.getItem(position);
        DialogFragment fragment = SubjectDetailsFragment.newInstance(item.getId());
        fragment.show(getChildFragmentManager(), null);
    }
}
