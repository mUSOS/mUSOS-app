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
import pl.edu.amu.usos.adapter.GroupAdapter;
import pl.edu.amu.usos.content.SingleGroup;
import pl.edu.amu.usos.loaders.GroupsLoader;

public class GroupFragment extends BaseFragment implements LoaderCallbacks<List<SingleGroup>>, AdapterView.OnItemClickListener {

    private static final int LOADER_ID = 0;
    private GroupAdapter mAdapter;

    @InjectView(R.id.list_view)
    ListView mListView;
    @InjectView(R.id.progress_bar)
    View mProgressBar;

    public static Fragment newInstance() {
        return new GroupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.group_layout, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new GroupAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mListView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public Loader<List<SingleGroup>> onCreateLoader(int id, Bundle args) {
        showProgress(true);
        return new GroupsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<SingleGroup>> loader, List<SingleGroup> data) {
        showProgress(false);
        mAdapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<SingleGroup>> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final SingleGroup group = (SingleGroup) mAdapter.getItem(position);
        DialogFragment fragment = SingleGroupFragment.newInstance(group.groupName,
                group.getStudents());

        fragment.show(getChildFragmentManager(), "group");
    }
}
