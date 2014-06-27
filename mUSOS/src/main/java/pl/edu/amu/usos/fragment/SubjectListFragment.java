package pl.edu.amu.usos.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.adapter.SubjectAdapter;
import pl.edu.amu.usos.content.SingleCourse;
import pl.edu.amu.usos.loaders.TimeTableTask;

public class SubjectListFragment extends BaseFragment implements TimeTableTask.OnTimeTableListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.list_view)
    ListView mListView;
    @InjectView(R.id.progress_bar)
    View mProgressBar;

    private SubjectAdapter mAdapter;

    public static Fragment newInstance() {
        return new SubjectListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.subject_list_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mAppPrefs.needTimeTableRefresh()) {
            showProgress(true);
            new TimeTableTask(getActivity(), this).execute();
        } else {
            showProgress(false);
            loadSubjects();
        }
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mListView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void loadSubjects() {
        List<SingleCourse> courses = SingleCourse.findWithQuery(SingleCourse.class,
                "Select * from SINGLE_COURSE ORDER BY name ASC");
        mAdapter = new SubjectAdapter(getActivity(), courses);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onTimeTableLoaded() {
        if (isAdded()) {
            showProgress(false);
            loadSubjects();
            mAppPrefs.edit().setTimeTableSync(System.currentTimeMillis()).commit();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long course_id = mAdapter.getItemId(position);
        DialogFragment fragment = SubjectDetailsFragment.newInstance(course_id);
        fragment.show(getChildFragmentManager(), null);
    }
}
