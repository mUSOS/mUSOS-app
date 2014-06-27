package pl.edu.amu.usos.fragment;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.api.model.Course;
import pl.edu.amu.usos.loaders.CourseLoader;

public class IncomingFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<Course>> {

    private static final int LOADER_ID = 1;

    public static BaseFragment newInstance() {
        return new IncomingFragment();
    }

    @InjectView(R.id.subject_label)
    TextView mSubjectLabel;
    @InjectView(R.id.start_label)
    TextView mStartLabel;
    @InjectView(R.id.end_label)
    TextView mEndLabel;

    private DateFormat mDateFormat = new SimpleDateFormat("HH:mm - EEEE, dd MMMM yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.incoming_class_layout, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mAppPrefs.needCourseRefresh()) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            setCourseData(mAppPrefs.getCourseName(), mAppPrefs.getCourseStart(),
                    mAppPrefs.getCourseEnd());
        }
    }

    private void setCourseData(String name, Date start, Date end) {
        mSubjectLabel.setText(name);
        mStartLabel.setText(mDateFormat.format(start));
        mEndLabel.setText(mDateFormat.format(end));
    }

    @Override
    public Loader<List<Course>> onCreateLoader(int id, Bundle args) {
        return new CourseLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Course>> loader, List<Course> courses) {
        if (courses == null) {
            checkForToken();
        } else {
            long now = new Date().getTime();
            for (Course course : courses) {
                if (now < course.startTime.getTime()) {
                    setCourseData(course.getCourseName(), course.startTime, course.endTime);
                    mAppPrefs.edit().putCourse(course).commit();
                    break;
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Course>> loader) {

    }
}
