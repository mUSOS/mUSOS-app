package pl.edu.amu.usos.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.content.SingleCourse;
import pl.edu.amu.usos.helper.EntityHelper;

import static com.google.common.base.Preconditions.checkArgument;

public class SubjectDetailsFragment extends DialogFragment {

    private static final String ARG_ID = "arg_id";

    @InjectView(R.id.subject_title)
    TextView mSubjectTitle;
    @InjectView(R.id.subject_type)
    TextView mSubjectType;
    @InjectView(R.id.subject_start)
    TextView mSubjectStart;
    @InjectView(R.id.subject_duration)
    TextView mSubjectDuration;
    @InjectView(R.id.subject_day)
    TextView mSubjectDay;
    @InjectView(R.id.subject_frequency)
    TextView mSubjectFrequency;
    @InjectView(R.id.subject_room)
    TextView mSubjectRoom;
    @InjectView(R.id.subject_building)
    TextView mSubjectBuilding;

    @OnClick(R.id.dismiss_btn)
    void onDismissClick() {
        getDialog().dismiss();
    }

    private long mCourseId;
    private SingleCourse mCourse;
    private SimpleDateFormat mDayFormat;
    private SimpleDateFormat mTimeFormat;

    public static DialogFragment newInstance(long id) {
        checkArgument(id > 0);
        DialogFragment fragment = new SubjectDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.subject_details_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCourseId = getArguments().getLong(ARG_ID);
        mCourse = SingleCourse.findById(SingleCourse.class, mCourseId);
        mDayFormat = new SimpleDateFormat("EEEE");
        mTimeFormat = new SimpleDateFormat("HH:mm");

        bindData();
    }

    private void bindData() {
        final String title = mCourse.name;
        final Date date = new Date(mCourse.start);
        final String day = mDayFormat.format(date);
        final String time = mTimeFormat.format(date);
        final long duration = (mCourse.end - mCourse.start) / 1000 / 60;

        getDialog().setTitle("Informacje");
        mSubjectTitle.setText(title.substring(0, title.indexOf("-")));
        mSubjectType.setText(title.substring(title.lastIndexOf("-") + 2));
        mSubjectRoom.setText(mCourse.room_number);
        mSubjectBuilding.setText(mCourse.building_name);
        mSubjectDay.setText(day);
        mSubjectDuration.setText(duration + " minut");
        mSubjectFrequency.setText(EntityHelper.getFrequencyString(mCourse.mode));
        mSubjectStart.setText(time);
    }

}
