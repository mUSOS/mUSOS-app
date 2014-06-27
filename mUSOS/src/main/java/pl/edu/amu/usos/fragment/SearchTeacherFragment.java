package pl.edu.amu.usos.fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.adapter.SearchAdapter;
import pl.edu.amu.usos.api.model.TeacherSearchItem;
import pl.edu.amu.usos.loaders.SearchTeacherTask;

import static com.google.common.base.Preconditions.checkNotNull;

public class SearchTeacherFragment extends BaseFragment implements LoaderCallbacks<List<TeacherSearchItem>>,
        AdapterView.OnItemClickListener {

    public static final String ARG_QUERY = "arg_query";

    private static final int LOADER_ID = 3;
    private String mQuery;

    public static Fragment newInstance(String query) {
        final Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        Fragment fragment = new SearchTeacherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectView(R.id.empty_label)
    View mEmptyLabel;
    @InjectView(R.id.progress_bar)
    View mProgress;
    @InjectView(R.id.teachers_list_view)
    ListView mListView;

    private SearchAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = checkNotNull(getArguments());
        mQuery = args.getString(ARG_QUERY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SearchAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        getLoaderManager().initLoader(SearchTeacherFragment.LOADER_ID, null, this).forceLoad();
    }

    @Override
    public Loader<List<TeacherSearchItem>> onCreateLoader(int id, Bundle args) {
        showProgress(true);
        return new SearchTeacherTask(getActivity(), mQuery);
    }

    @Override
    public void onLoadFinished(Loader<List<TeacherSearchItem>> loader, List<TeacherSearchItem> data) {
        showProgress(false);
        if (data != null) {
            mAdapter.swapData(data);
        }

        boolean isEmpty = data == null || data.size() == 0;
        if (isEmpty) {
            showProgress(false);
            mEmptyLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<TeacherSearchItem>> loader) {
    }

    private void showProgress(boolean progress) {
        mListView.setVisibility(progress ? View.GONE : View.VISIBLE);
        mProgress.setVisibility(progress ? View.VISIBLE : View.GONE);
        mEmptyLabel.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String teacherId = mAdapter.getTeacherId(position);
        DialogFragment fragment = TeacherDetailFragment.newInstance(teacherId);
        fragment.show(getChildFragmentManager(), "TeacherDetail");
    }
}
